package com.boll.tyelauncher.biz.globalconfig;

package com.toycloud.launcher.biz.globalconfig;

import android.content.Context;
import android.text.TextUtils;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.iflytek.cbg.common.utils.ListUtils;
import com.iflytek.easytrans.core.utils.common.ArrayUtils;
import com.iflytek.easytrans.core.utils.log.LogUtils;
import com.toycloud.launcher.api.model.Constants;
import com.toycloud.launcher.api.service.OriginalHttpHelper;
import com.toycloud.launcher.application.LauncherApplication;
import com.toycloud.launcher.model.CityConfigModel;
import com.toycloud.launcher.model.CityModel;
import com.toycloud.launcher.model.ProvinceCityModel;
import com.toycloud.launcher.util.SharepreferenceUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityInfoUpgradeManager {
    private static final int CITY_INFO_UPDATE_STATUS_DOWNLOADING = 2;
    private static final int CITY_INFO_UPDATE_STATUS_IDLE = 0;
    private static final int CITY_INFO_UPDATE_STATUS_REQUESTING = 1;
    private static final int PROVINCE_INIT_STATUS_COMPLETE = 2;
    private static final int PROVINCE_INIT_STATUS_LOADING = 1;
    private static final int PROVINCE_INIT_STATUS_NONE = 0;
    private static final String TAG = "CityInfoUpgradeManager";
    private static CityInfoUpgradeManager mInstance;
    private MyCallback mCallBack;
    private Map<String, List<CityModel>> mCitisDatasMap = new HashMap();
    private Call<ResponseBody> mCityUpdateCall;
    private List<CityModel> mProvinceDatas = new ArrayList();
    /* access modifiers changed from: private */
    public int mProvinceInitStatus = 0;
    /* access modifiers changed from: private */
    public int mUpdateStatus = 0;

    public interface OnInitProvinceDataListener {
        void onLoadComplete(boolean z);
    }

    public static CityInfoUpgradeManager getInstance() {
        if (mInstance == null) {
            synchronized (CityInfoUpgradeManager.class) {
                if (mInstance == null) {
                    mInstance = new CityInfoUpgradeManager();
                }
            }
        }
        return mInstance;
    }

    private CityInfoUpgradeManager() {
    }

    public int getLoadProvinceStatus() {
        return this.mProvinceInitStatus;
    }

    public List<CityModel> getProvinceList() {
        return this.mProvinceDatas;
    }

    public List<CityModel> getCityList(String provinceCode) {
        if (this.mCitisDatasMap == null || this.mCitisDatasMap.isEmpty()) {
            return null;
        }
        return this.mCitisDatasMap.get(provinceCode);
    }

    public void loadData(Context context, OnInitProvinceDataListener listener) {
        InputStream is;
        long startTime = System.currentTimeMillis();
        if (this.mProvinceInitStatus != 2 || listener == null) {
            this.mProvinceInitStatus = 1;
            this.mProvinceDatas.clear();
            this.mCitisDatasMap.clear();
            File remoteFile = new File(context.getFilesDir(), Constants.REMOTE_CITY_CONFIG_NAME);
            try {
                if (remoteFile.exists()) {
                    is = new FileInputStream(remoteFile);
                    LogUtils.d(TAG, "loadData: 加载本地json文件");
                } else {
                    is = LauncherApplication.getContext().getAssets().open("city.json");
                    LogUtils.d(TAG, "loadData: 加载assets文件夹下的json文件");
                }
                byte[] fileContent = new byte[is.available()];
                int currentPosition = 0;
                byte[] buf = new byte[1024];
                while (true) {
                    int len = is.read(buf);
                    if (len == -1) {
                        break;
                    }
                    System.arraycopy(buf, 0, fileContent, currentPosition, len);
                    currentPosition += len;
                }
                is.close();
                String content = new String(fileContent, 0, fileContent.length, Charset.forName("UTF-8"));
                LogUtils.d(TAG, "loadData: content:" + content);
                ProvinceCityModel provinceCityModel = (ProvinceCityModel) new GsonBuilder().create().fromJson(content, ProvinceCityModel.class);
                if (provinceCityModel == null) {
                    if (listener != null) {
                        listener.onLoadComplete(false);
                    }
                    LogUtils.d(TAG, "loadData: 反序列化数据模型为空，删除后台下载的数据");
                    if (remoteFile.exists()) {
                        remoteFile.delete();
                    }
                    this.mProvinceInitStatus = 0;
                    return;
                }
                SharepreferenceUtil.putInt(Constants.SP_NAME_LOCAL_CITY_CONFIG_VERSION, provinceCityModel.versionCode);
                LogUtils.d(TAG, "loadData: 更新本地资源版本号缓存为 versionCode:" + provinceCityModel.versionCode);
                if (!ListUtils.isEmpty((List) provinceCityModel.response)) {
                    for (CityModel cityModel : provinceCityModel.response) {
                        if (cityModel.getLevel().equals("1")) {
                            this.mProvinceDatas.add(cityModel);
                        } else if (cityModel.getLevel().equals("2")) {
                            if (this.mCitisDatasMap.get(cityModel.getParentid()) == null) {
                                List<CityModel> list = new ArrayList<>();
                                list.add(cityModel);
                                this.mCitisDatasMap.put(cityModel.getParentid(), list);
                            } else {
                                List<CityModel> list2 = this.mCitisDatasMap.get(cityModel.getParentid());
                                list2.add(cityModel);
                                this.mCitisDatasMap.put(cityModel.getParentid(), list2);
                            }
                        }
                    }
                }
                if (listener != null) {
                    listener.onLoadComplete(true);
                }
                this.mProvinceInitStatus = 2;
                LogUtils.d(TAG, "loadData: 加载地市信息耗时：" + (System.currentTimeMillis() - startTime));
            } catch (IOException e) {
                LogUtils.d(TAG, "loadData: 加载数据失败：" + e.getMessage());
                if (listener != null) {
                    listener.onLoadComplete(false);
                }
                this.mProvinceInitStatus = 0;
            } catch (JsonSyntaxException e2) {
                LogUtils.d(TAG, "loadData: 加载数据失败2：" + e2.getMessage());
                if (listener != null) {
                    listener.onLoadComplete(false);
                    if (remoteFile.exists()) {
                        remoteFile.delete();
                    }
                }
                this.mProvinceInitStatus = 0;
            }
        } else {
            LogUtils.d(TAG, "loadData: 数据已经加载完成,直接返回");
            listener.onLoadComplete(true);
        }
    }

    public void checkAndUpgrade(Context context, String cityConfig) {
        LogUtils.d(TAG, "checkAndUpgrade: 地市更新信息配置：" + cityConfig);
        if (context == null) {
            LogUtils.d(TAG, "checkAndUpgrade: context 为空，无法执行更新");
        } else if (TextUtils.isEmpty(cityConfig)) {
            LogUtils.d(TAG, "checkAndUpgrade: 地市信息更新配置为空，无法执行更新检查");
        } else if (this.mUpdateStatus == 1) {
            LogUtils.d(TAG, "checkAndUpgrade: 正在请求地市信息更新中... 忽略本次请求");
        } else if (this.mUpdateStatus == 2) {
            LogUtils.d(TAG, "checkAndUpgrade: 正在下载地市信息...  忽略本次请求");
        } else {
            try {
                int localCityVersion = SharepreferenceUtil.getInt(Constants.SP_NAME_LOCAL_CITY_CONFIG_VERSION, 1);
                LogUtils.d(TAG, "checkAndUpgrade: 本地地市信息版本号：" + localCityVersion);
                CityConfigModel model = (CityConfigModel) new GsonBuilder().create().fromJson(cityConfig, CityConfigModel.class);
                if (model == null) {
                    LogUtils.d(TAG, "checkAndUpgrade: 配置的地市更新信息反序列化结果为空，无法执行更新");
                } else if (!model.needUpdateConfig(localCityVersion) || TextUtils.isEmpty(model.url)) {
                    LogUtils.d(TAG, "checkAndUpgrade: " + (TextUtils.isEmpty(model.url) ? "地市信息下载地址为空，无法更新" : "无需更新地市信息"));
                } else {
                    this.mUpdateStatus = 1;
                    this.mCityUpdateCall = OriginalHttpHelper.getHttpService().downloadFileSync(model.url);
                    if (this.mCallBack == null) {
                        this.mCallBack = new MyCallback(context.getFilesDir().getAbsolutePath(), model.versionCode);
                    }
                    this.mCityUpdateCall.enqueue(this.mCallBack);
                }
            } catch (JsonSyntaxException e) {
                LogUtils.d(TAG, "checkAndUpgrade: 地市更新信息配置json反序列化失败：" + e.getMessage());
            }
        }
    }

    public void cancel() {
        if (this.mCityUpdateCall != null && !this.mCityUpdateCall.isCanceled()) {
            this.mCityUpdateCall.cancel();
        }
    }

    private class MyCallback implements Callback<ResponseBody> {
        private String fileSavePath;
        private int updatingVersionCode;

        public MyCallback(String savePath, int versionCode) {
            this.fileSavePath = savePath;
            this.updatingVersionCode = versionCode;
        }

        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                int unused = CityInfoUpgradeManager.this.mUpdateStatus = 2;
                LogUtils.d(CityInfoUpgradeManager.TAG, "onResponse: 更新地市信息，网络请求成功，开始写文件");
                if (CityInfoUpgradeManager.this.writeFileToSDCard(this.fileSavePath, response.body())) {
                    LogUtils.d(CityInfoUpgradeManager.TAG, "onResponse: 更新地市信息文件成功，下次即可使用");
                    SharepreferenceUtil.putInt(Constants.SP_NAME_LOCAL_CITY_CONFIG_VERSION, this.updatingVersionCode);
                    LogUtils.d(CityInfoUpgradeManager.TAG, "onResponse: 更新本地地市信息版本记录成功，当前版本为：" + this.updatingVersionCode);
                    int unused2 = CityInfoUpgradeManager.this.mProvinceInitStatus = 0;
                } else {
                    LogUtils.d(CityInfoUpgradeManager.TAG, "onResponse: 更新地市信息文件失败");
                }
                int unused3 = CityInfoUpgradeManager.this.mUpdateStatus = 0;
                return;
            }
            int unused4 = CityInfoUpgradeManager.this.mUpdateStatus = 0;
            LogUtils.d(CityInfoUpgradeManager.TAG, "onResponse: 更新地市配置信息失败，网络请求失败：" + response.message());
        }

        public void onFailure(Call<ResponseBody> call, Throwable t) {
            LogUtils.d(CityInfoUpgradeManager.TAG, "onFailure: 网络请求失败，" + (t != null ? t.getMessage() : ""));
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00a2 A[Catch:{ IOException -> 0x009c }] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00a7 A[Catch:{ IOException -> 0x009c }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean writeFileToSDCard(java.lang.String r18, okhttp3.ResponseBody r19) {
        /*
            r17 = this;
            java.io.File r13 = new java.io.File     // Catch:{ IOException -> 0x009c }
            java.lang.String r14 = "remoteCity.json.tmp"
            r0 = r18
            r13.<init>(r0, r14)     // Catch:{ IOException -> 0x009c }
            boolean r14 = r13.exists()     // Catch:{ IOException -> 0x009c }
            if (r14 == 0) goto L_0x001c
            boolean r14 = r13.delete()     // Catch:{ IOException -> 0x009c }
            if (r14 == 0) goto L_0x005c
            java.lang.String r14 = "CityInfoUpgradeManager"
            java.lang.String r15 = "writeFileToSDCard: 缓存文件删除成功，开始下载"
            com.iflytek.easytrans.core.utils.log.LogUtils.d(r14, r15)     // Catch:{ IOException -> 0x009c }
        L_0x001c:
            r8 = 0
            r9 = 0
            r14 = 4096(0x1000, float:5.74E-42)
            byte[] r3 = new byte[r14]     // Catch:{ IOException -> 0x00ae, all -> 0x009f }
            long r4 = r19.contentLength()     // Catch:{ IOException -> 0x00ae, all -> 0x009f }
            r6 = 0
            java.io.InputStream r8 = r19.byteStream()     // Catch:{ IOException -> 0x00ae, all -> 0x009f }
            java.io.FileOutputStream r10 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x00ae, all -> 0x009f }
            r10.<init>(r13)     // Catch:{ IOException -> 0x00ae, all -> 0x009f }
        L_0x0031:
            int r11 = r8.read(r3)     // Catch:{ IOException -> 0x008e, all -> 0x00ab }
            r14 = -1
            if (r11 != r14) goto L_0x0065
            r10.flush()     // Catch:{ IOException -> 0x008e, all -> 0x00ab }
            java.io.File r12 = new java.io.File     // Catch:{ IOException -> 0x008e, all -> 0x00ab }
            java.lang.String r14 = "remoteCity.json"
            r0 = r18
            r12.<init>(r0, r14)     // Catch:{ IOException -> 0x008e, all -> 0x00ab }
            boolean r14 = r12.exists()     // Catch:{ IOException -> 0x008e, all -> 0x00ab }
            if (r14 == 0) goto L_0x004d
            r12.delete()     // Catch:{ IOException -> 0x008e, all -> 0x00ab }
        L_0x004d:
            boolean r14 = r13.renameTo(r12)     // Catch:{ IOException -> 0x008e, all -> 0x00ab }
            if (r8 == 0) goto L_0x0056
            r8.close()     // Catch:{ IOException -> 0x009c }
        L_0x0056:
            if (r10 == 0) goto L_0x005b
            r10.close()     // Catch:{ IOException -> 0x009c }
        L_0x005b:
            return r14
        L_0x005c:
            java.lang.String r14 = "CityInfoUpgradeManager"
            java.lang.String r15 = "writeFileToSDCard: 缓存文件删除失败，无法下载"
            com.iflytek.easytrans.core.utils.log.LogUtils.d(r14, r15)     // Catch:{ IOException -> 0x009c }
            r14 = 0
            goto L_0x005b
        L_0x0065:
            r14 = 0
            r10.write(r3, r14, r11)     // Catch:{ IOException -> 0x008e, all -> 0x00ab }
            long r14 = (long) r11     // Catch:{ IOException -> 0x008e, all -> 0x00ab }
            long r6 = r6 + r14
            java.lang.String r14 = "CityInfoUpgradeManager"
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x008e, all -> 0x00ab }
            r15.<init>()     // Catch:{ IOException -> 0x008e, all -> 0x00ab }
            java.lang.String r16 = "file download: "
            java.lang.StringBuilder r15 = r15.append(r16)     // Catch:{ IOException -> 0x008e, all -> 0x00ab }
            java.lang.StringBuilder r15 = r15.append(r6)     // Catch:{ IOException -> 0x008e, all -> 0x00ab }
            java.lang.String r16 = " of "
            java.lang.StringBuilder r15 = r15.append(r16)     // Catch:{ IOException -> 0x008e, all -> 0x00ab }
            java.lang.StringBuilder r15 = r15.append(r4)     // Catch:{ IOException -> 0x008e, all -> 0x00ab }
            java.lang.String r15 = r15.toString()     // Catch:{ IOException -> 0x008e, all -> 0x00ab }
            com.iflytek.easytrans.core.utils.log.LogUtils.d(r14, r15)     // Catch:{ IOException -> 0x008e, all -> 0x00ab }
            goto L_0x0031
        L_0x008e:
            r2 = move-exception
            r9 = r10
        L_0x0090:
            r14 = 0
            if (r8 == 0) goto L_0x0096
            r8.close()     // Catch:{ IOException -> 0x009c }
        L_0x0096:
            if (r9 == 0) goto L_0x005b
            r9.close()     // Catch:{ IOException -> 0x009c }
            goto L_0x005b
        L_0x009c:
            r2 = move-exception
            r14 = 0
            goto L_0x005b
        L_0x009f:
            r14 = move-exception
        L_0x00a0:
            if (r8 == 0) goto L_0x00a5
            r8.close()     // Catch:{ IOException -> 0x009c }
        L_0x00a5:
            if (r9 == 0) goto L_0x00aa
            r9.close()     // Catch:{ IOException -> 0x009c }
        L_0x00aa:
            throw r14     // Catch:{ IOException -> 0x009c }
        L_0x00ab:
            r14 = move-exception
            r9 = r10
            goto L_0x00a0
        L_0x00ae:
            r2 = move-exception
            goto L_0x0090
        */
        throw new UnsupportedOperationException("Method not decompiled: com.toycloud.launcher.biz.globalconfig.CityInfoUpgradeManager.writeFileToSDCard(java.lang.String, okhttp3.ResponseBody):boolean");
    }

    public String getRegionName(String areaCode) {
        if (TextUtils.isEmpty(areaCode) || ArrayUtils.isEmpty((Collection<?>) this.mProvinceDatas) || ArrayUtils.isEmpty((Map<?, ?>) this.mCitisDatasMap)) {
            return null;
        }
        CityModel currentCityModel = null;
        for (Map.Entry<String, List<CityModel>> entry : this.mCitisDatasMap.entrySet()) {
            List<CityModel> cityModelList = entry.getValue();
            if (!ArrayUtils.isEmpty((Collection<?>) cityModelList)) {
                Iterator<CityModel> it = cityModelList.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    CityModel cityModel = it.next();
                    if (areaCode.equals(cityModel.getId())) {
                        currentCityModel = cityModel;
                        break;
                    }
                }
            }
        }
        if (currentCityModel == null) {
            return null;
        }
        if (currentCityModel.getId().equals(currentCityModel.getParentid())) {
            return currentCityModel.getAreaname();
        }
        for (CityModel cityModel2 : this.mProvinceDatas) {
            if (cityModel2.getParentid().equals(cityModel2.getId())) {
                return cityModel2.getAreaname() + currentCityModel.getAreaname();
            }
        }
        return null;
    }
}
