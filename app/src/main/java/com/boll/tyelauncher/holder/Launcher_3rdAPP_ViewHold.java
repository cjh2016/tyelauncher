package com.boll.tyelauncher.holder;

package com.toycloud.launcher.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import com.anarchy.classify.callback.CanceEditInterface;
import com.anarchy.classify.simple.AppInfo;
import com.toycloud.launcher.R;
import com.toycloud.launcher.model.launcher.impl.LauncherModelView;
import com.toycloud.launcher.ui.BaseMainActivity;
import com.toycloud.launcher.widget.IReaderMockData;
import com.toycloud.launcher.widget.ThirdAppAdapter;
import com.toycloud.launcher.widget.ThirdAppClassifyView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Launcher_3rdAPP_ViewHold extends BaseHolder implements View.OnClickListener, CanceEditInterface, ThirdAppAdapter.DataChangeInterFace {
    private static final String TAG = "Launcher_3rdAPP_ViewHold";
    private ThirdAppAdapter mAdapter;
    /* access modifiers changed from: private */
    public ArrayList<AppInfo> mAppInfos;
    private Context mContext;
    /* access modifiers changed from: private */
    public ThirdAppClassifyView mIconListView;
    /* access modifiers changed from: private */
    public BaseMainActivity mMainActivity;
    private View mMainView;
    /* access modifiers changed from: private */
    public LauncherModelView mModelView;

    public Launcher_3rdAPP_ViewHold(Context context, ArrayList<AppInfo> apps) {
        super(context);
        this.mContext = context;
        this.mAppInfos = apps;
    }

    public void setMainActivity(BaseMainActivity mainActivity) {
        this.mMainActivity = mainActivity;
    }

    public void setModelView(LauncherModelView modelView) {
        this.mModelView = modelView;
    }

    /* access modifiers changed from: protected */
    public int getLayoutId() {
        return R.layout.viewholder_3rd_app;
    }

    public void onCreate() {
        super.onCreate();
        initDataView();
    }

    public int getAppIndex(AppInfo appInfo) {
        if (this.mAppInfos == null || appInfo == null) {
            return -1;
        }
        return getAppIndex(appInfo.getPakageName());
    }

    public int getAppIndex(String pkgName) {
        if (this.mAppInfos == null || pkgName == null) {
            return -1;
        }
        int index = 0;
        Iterator<AppInfo> it = this.mAppInfos.iterator();
        while (it.hasNext()) {
            if (TextUtils.equals(it.next().getPakageName(), pkgName)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public boolean isEditMode() {
        return this.mAdapter.isEditMode();
    }

    public void setEditMode(boolean editMode) {
        this.mAdapter.setEditMode(editMode);
    }

    public void setData(ArrayList<AppInfo> arrayList) {
    }

    public ArrayList<AppInfo> getApps() {
        return this.mAppInfos;
    }

    public ArrayList<AppInfo> getCurrentApps() {
        return this.mAdapter.getAppInfoList();
    }

    private void initDataView() {
        this.mAdapter = new ThirdAppAdapter(this.mContext);
        this.mAdapter.setDataChangeInterFace(this);
        generateRandomMockData();
        this.mAdapter.registerObserver(new ThirdAppAdapter.IReaderObserver() {
            private int count = 0;

            public void onChecked(boolean isChecked) {
                this.count = (isChecked ? 1 : -1) + this.count;
                if (this.count <= 0) {
                    this.count = 0;
                }
            }

            public void onEditChanged(boolean inEdit) {
            }

            public void onRestore() {
                this.count = 0;
            }

            public void onHideSubDialog() {
                Launcher_3rdAPP_ViewHold.this.mIconListView.hideSubContainer();
            }

            public void onLongClick() {
                if (Launcher_3rdAPP_ViewHold.this.mMainActivity != null) {
                    Launcher_3rdAPP_ViewHold.this.mMainActivity.setEditMode(true);
                }
            }

            public void onPositionChanged(int srcPos, int targetPos) {
                Launcher_3rdAPP_ViewHold.this.mAppInfos.add(targetPos, Launcher_3rdAPP_ViewHold.this.mAppInfos.remove(srcPos));
                if (Launcher_3rdAPP_ViewHold.this.mModelView != null) {
                    Launcher_3rdAPP_ViewHold.this.mModelView.updateAppPosCache();
                }
            }
        });
        this.mIconListView.setAdapter(this.mAdapter);
        this.mIconListView.setDebugAble(true);
    }

    /* access modifiers changed from: protected */
    public void initView(Context context, View rootView) {
        this.mIconListView = (ThirdAppClassifyView) rootView.findViewById(R.id.classify_view);
        this.mIconListView.setCanceEditInterface(this);
        this.mMainView = rootView.findViewById(R.id.activity_main);
        this.mMainView.setOnClickListener(this);
        this.mIconListView.setOnClickListener(this);
    }

    private void generateRandomMockData() {
        this.mAdapter.clearData();
        int size = this.mAppInfos.size();
        List<IReaderMockData> datas = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            datas.add(convert(this.mAppInfos.get(i)));
        }
        this.mAdapter.setMockSource(datas);
        setDataChange(this.mAdapter.getMockSource());
    }

    private IReaderMockData convert(AppInfo appInfo) {
        IReaderMockData mockData = new IReaderMockData();
        mockData.setAppInfo(appInfo);
        mockData.setColor(this.mContext.getResources().getColor(R.color.transparent));
        return mockData;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_main:
                this.mAdapter.setEditMode(false);
                return;
            case R.id.classify_view:
                this.mAdapter.setEditMode(false);
                return;
            default:
                return;
        }
    }

    public void cancelEdit() {
    }

    public void cancelEdit(int type) {
    }

    public void setDataChange(List<IReaderMockData> list) {
    }

    public void addApp(AppInfo appInfo) {
        this.mAppInfos.add(appInfo);
        this.mAdapter.appendItem(convert(appInfo));
    }

    public void removeApp(AppInfo appInfo, int position) {
        if (this.mAppInfos.remove(position) != appInfo) {
            throw new RuntimeException("BUG");
        }
        this.mAdapter.removeApp(appInfo);
    }

    public void updateAppInfo(AppInfo appInfo, int position) {
        this.mAdapter.notifyDataSetChanged();
    }

    public void updateFull() {
        this.mAdapter.notifyDataSetChanged();
    }

    public String getStatPageName() {
        return "Fragment_ThirdAppListPage";
    }
}