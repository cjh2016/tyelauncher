package com.boll.tyelauncher.fragment;

package com.toycloud.launcher.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iflytek.stats.StatsHelper;
import com.toycloud.launcher.R;
import com.toycloud.launcher.adapter.EnglishModelAdapter;
import com.toycloud.launcher.api.model.User;
import com.toycloud.launcher.api.response.RadarReportResponse;
import com.toycloud.launcher.api.service.LauncherHttpHelper;
import com.toycloud.launcher.helper.EnglishModelHelper;
import com.toycloud.launcher.helper.FunctionModuleBean;
import com.toycloud.launcher.helper.RadarModuleBean;
import com.toycloud.launcher.helper.UnitModelBean;
import com.toycloud.launcher.holder.Launcher_EnglishPage_ViewHolder;
import com.toycloud.launcher.model.EtsConstant;
import com.toycloud.launcher.util.EtsUtils;
import com.toycloud.launcher.util.SharepreferenceUtil;
import com.toycloud.launcher.view.RadarView;
import framework.hz.salmon.base.BaseFragment;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class EnglishContentFragment extends BaseFragment {
    private static final String TAG = "EnglishContent";
    private String mCurrentFragment;
    /* access modifiers changed from: private */
    public List<UnitModelBean> mData = new ArrayList();
    private EnglishModelAdapter mModelAdapter;
    private RadarView mRadarView;
    private RecyclerView mRlvEntry;
    private View mRootView;

    @Retention(RetentionPolicy.SOURCE)
    public @interface FragmentName {
        public static final String PRACTICE = "practice";
        public static final String TEST = "test";
    }

    public static EnglishContentFragment getInstance() {
        return new EnglishContentFragment();
    }

    public void setCurrentFragmentName(String fragmentName) {
        this.mCurrentFragment = fragmentName;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mRootView = inflater.inflate(R.layout.fragment_english_page_new, (ViewGroup) null);
        initView(this.mRootView);
        Log.d(TAG, "onCreateView");
        refreshData(true, (List<String>) null);
        return this.mRootView;
    }

    public void refreshData(boolean isNeedRefresh, List<String> list) {
        if (this.mRootView != null && isNeedRefresh) {
            Log.d(TAG, "可以刷新");
            this.mData.clear();
            User userInfo = SharepreferenceUtil.getSharepferenceInstance(getContext()).getUserInfo();
            if (userInfo == null) {
                showDefaultValue();
                return;
            }
            searchDataToRadar(userInfo.getToken());
            FunctionModuleBean functionModuleBean = EnglishModelHelper.getInstance(getContext()).queryFunctionModule(userInfo.getToken());
            if (functionModuleBean == null || functionModuleBean.getCode() != 0) {
                showDefaultValue();
                return;
            }
            FunctionModuleBean.DataBean dataBean = functionModuleBean.getData();
            if (dataBean != null) {
                List<String> data = dataBean.getFunctionModuleData();
                if (data == null || data.size() == 0) {
                    showDefaultValue();
                } else if (FragmentName.PRACTICE.equals(this.mCurrentFragment)) {
                    Log.d(TAG, "-------1:" + this.mCurrentFragment);
                    nofifyData(userInfo, data, EtsConstant.BOOK_SYNC, "10");
                } else if (FragmentName.TEST.equals(this.mCurrentFragment)) {
                    Log.d(TAG, "-------2:" + this.mCurrentFragment);
                    nofifyData(userInfo, data, "1", "12");
                }
            } else {
                showDefaultValue();
            }
        }
    }

    private void nofifyData(User userInfo, List<String> data, String modelId1, String modelId2) {
        if (data.contains(modelId1) || data.contains(modelId2)) {
            if (data.contains(modelId1)) {
                UnitModelBean unitModelBean = EnglishModelHelper.getInstance(getContext()).queryRecentUnit(userInfo.getToken(), modelId1);
                if (unitModelBean == null || !EtsConstant.SUCCESS.equals(unitModelBean.getCode())) {
                    UnitModelBean unitModelBean1 = new UnitModelBean();
                    unitModelBean1.setModuleId(modelId1);
                    if (FragmentName.PRACTICE.equals(this.mCurrentFragment)) {
                        unitModelBean1.setModuleTitle(getString(R.string.ting_shuo_tongbu));
                    } else {
                        unitModelBean1.setModuleTitle(getString(R.string.ting_shuo_moni));
                    }
                    unitModelBean1.setModuleSubTitle(getString(R.string.listening_and_speaking));
                    UnitModelBean.DataBean dataBean1 = new UnitModelBean.DataBean();
                    dataBean1.setUnitName(getString(R.string.default_unit));
                    dataBean1.setTotalScore(0.0f);
                    dataBean1.setScore(0.0f);
                    unitModelBean1.setData(dataBean1);
                    this.mData.add(unitModelBean1);
                } else {
                    unitModelBean.setModuleId(modelId1);
                    if (FragmentName.PRACTICE.equals(this.mCurrentFragment)) {
                        unitModelBean.setModuleTitle(getString(R.string.ting_shuo_tongbu));
                    } else {
                        unitModelBean.setModuleTitle(getString(R.string.ting_shuo_moni));
                    }
                    unitModelBean.setModuleSubTitle(getString(R.string.listening_and_speaking));
                    this.mData.add(unitModelBean);
                }
            }
            if (data.contains(modelId2)) {
                UnitModelBean unitModelBean2 = EnglishModelHelper.getInstance(getContext()).queryRecentUnit(userInfo.getToken(), modelId2);
                if (unitModelBean2 == null || !EtsConstant.SUCCESS.equals(unitModelBean2.getCode())) {
                    UnitModelBean unitModelBean22 = new UnitModelBean();
                    unitModelBean22.setModuleId(modelId2);
                    if (FragmentName.PRACTICE.equals(this.mCurrentFragment)) {
                        unitModelBean22.setModuleTitle(getString(R.string.du_xie_tongbu));
                    } else {
                        unitModelBean22.setModuleTitle(getString(R.string.du_xie_moni));
                    }
                    unitModelBean22.setModuleSubTitle(getString(R.string.reading_and_writing));
                    UnitModelBean.DataBean dataBean2 = new UnitModelBean.DataBean();
                    dataBean2.setUnitName(getString(R.string.default_unit));
                    dataBean2.setTotalScore(0.0f);
                    dataBean2.setScore(0.0f);
                    unitModelBean22.setData(dataBean2);
                    this.mData.add(unitModelBean22);
                } else {
                    unitModelBean2.setModuleId(modelId2);
                    if (FragmentName.PRACTICE.equals(this.mCurrentFragment)) {
                        unitModelBean2.setModuleTitle(getString(R.string.du_xie_tongbu));
                    } else {
                        unitModelBean2.setModuleTitle(getString(R.string.du_xie_moni));
                    }
                    unitModelBean2.setModuleSubTitle(getString(R.string.reading_and_writing));
                    this.mData.add(unitModelBean2);
                }
            }
            this.mModelAdapter.notifyDataSetChanged();
            return;
        }
        showDefaultValue();
    }

    private void searchDataToRadar(String token) {
        Log.d(TAG, "searchDataToRadar");
        RadarModuleBean radarModuleBean = EnglishModelHelper.getInstance(getContext()).queryRadar(token);
        if (radarModuleBean == null || radarModuleBean.getCode() != 0 || radarModuleBean.getData() == null || radarModuleBean.getData().getRadarData() == null || radarModuleBean.getData().getRadarData().size() == 0) {
            Log.d(TAG, "resetRadar");
            resetRadar();
            return;
        }
        List<Float> data = radarModuleBean.getData().getRadarData();
        if (data == null || data.size() < 4) {
            resetRadar();
            return;
        }
        reportRadar(data);
        int j = 0;
        for (Float d : data) {
            Log.d(TAG, "雷达图原始数据:" + d);
            if (Math.round(d.floatValue() * 100.0f) == 0 && (j = j + 1) == data.size()) {
                resetRadar();
                return;
            }
        }
        List<Integer> totalData = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            totalData.add(100);
        }
        if (this.mRadarView != null) {
            this.mRadarView.setRatioValues(data);
            this.mRadarView.setTotalValues(totalData);
        }
    }

    private void showDefaultValue() {
        UnitModelBean unitModelBean1 = new UnitModelBean();
        if (FragmentName.PRACTICE.equals(this.mCurrentFragment)) {
            unitModelBean1.setModuleId(EtsConstant.BOOK_SYNC);
            unitModelBean1.setModuleTitle(getString(R.string.ting_shuo_tongbu));
        } else {
            unitModelBean1.setModuleId("1");
            unitModelBean1.setModuleTitle(getString(R.string.ting_shuo_moni));
        }
        unitModelBean1.setModuleSubTitle(getString(R.string.listening_and_speaking));
        UnitModelBean.DataBean dataBean1 = new UnitModelBean.DataBean();
        dataBean1.setUnitName(getString(R.string.default_unit));
        dataBean1.setTotalScore(0.0f);
        dataBean1.setScore(0.0f);
        unitModelBean1.setData(dataBean1);
        UnitModelBean unitModelBean2 = new UnitModelBean();
        if (FragmentName.PRACTICE.equals(this.mCurrentFragment)) {
            unitModelBean2.setModuleId("10");
            unitModelBean2.setModuleTitle(getString(R.string.du_xie_tongbu));
        } else {
            unitModelBean2.setModuleId("12");
            unitModelBean2.setModuleTitle(getString(R.string.du_xie_moni));
        }
        unitModelBean2.setModuleSubTitle(getString(R.string.reading_and_writing));
        UnitModelBean.DataBean dataBean2 = new UnitModelBean.DataBean();
        dataBean2.setUnitName(getString(R.string.default_unit));
        dataBean2.setTotalScore(0.0f);
        dataBean2.setScore(0.0f);
        unitModelBean2.setData(dataBean2);
        this.mData.add(unitModelBean1);
        this.mData.add(unitModelBean2);
        this.mModelAdapter.notifyDataSetChanged();
        resetRadar();
    }

    private void resetRadar() {
        if (this.mRadarView != null) {
            this.mRadarView.resetView();
        }
    }

    private void initView(View view) {
        this.mRadarView = (RadarView) view.findViewById(R.id.radar_view);
        this.mRlvEntry = (RecyclerView) view.findViewById(R.id.rlv_entry);
        this.mRlvEntry.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mModelAdapter = new EnglishModelAdapter(R.layout.item_english_model, this.mData);
        this.mModelAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String moduleId = ((UnitModelBean) EnglishContentFragment.this.mData.get(position)).getModuleId();
                User user = SharepreferenceUtil.getSharepferenceInstance(EnglishContentFragment.this.getContext()).getUserInfo();
                if (EtsConstant.BOOK_SYNC.equals(moduleId)) {
                    EtsUtils.launchEts(EnglishContentFragment.this.getContext(), "com.ets100.study.ACTION_SYNC_LS", user);
                    StatsHelper.englishContentClickedTSTB();
                } else if ("10".equals(moduleId)) {
                    EtsUtils.launchEts(EnglishContentFragment.this.getContext(), "com.ets100.study.ACTION_SYNC_RW", user);
                    StatsHelper.englishContentClickedDXTB();
                } else if ("1".equals(moduleId)) {
                    EtsUtils.launchEts(EnglishContentFragment.this.getContext(), "com.ets100.study.ACTION_SIMULATION_LS", user);
                    StatsHelper.englishContentClickedTSMN();
                } else if ("12".equals(moduleId)) {
                    EtsUtils.launchEts(EnglishContentFragment.this.getContext(), "com.ets100.study.ACTION_SIMULATION_RW", user);
                    StatsHelper.englishContentClickedDXMN();
                }
            }
        });
        this.mRlvEntry.setAdapter(this.mModelAdapter);
    }

    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d(TAG, "onHiddenChanged");
    }

    public void setupView() {
    }

    public boolean handleMessage(Message msg) {
        return false;
    }

    public void onClick(View v) {
    }

    private void reportRadar(List<Float> data) {
        if (!Launcher_EnglishPage_ViewHolder.isNeedReport) {
            Log.d(TAG, "not refresh");
            return;
        }
        Launcher_EnglishPage_ViewHolder.isNeedReport = false;
        if (data == null || data.size() < 4) {
            Log.d(TAG, "data is null or < 4");
            Launcher_EnglishPage_ViewHolder.isNeedReport = true;
            return;
        }
        List<Integer> tempData = new ArrayList<>();
        for (Float f : data) {
            tempData.add(Integer.valueOf(Math.round(f.floatValue() * 100.0f)));
        }
        Log.d(TAG, "听:" + tempData.get(0));
        Log.d(TAG, "说:" + tempData.get(1));
        Log.d(TAG, "读:" + tempData.get(2));
        Log.d(TAG, "写:" + tempData.get(3));
        User userInfo = SharepreferenceUtil.getSharepferenceInstance(getContext()).getUserInfo();
        if (userInfo != null) {
            Log.d(TAG, "userInfo:" + userInfo.getGradecode());
            LauncherHttpHelper.getHttpService().reportRadarData(userInfo.getGradecode(), tempData.get(0).intValue(), tempData.get(1).intValue(), tempData.get(2).intValue(), tempData.get(3).intValue()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<RadarReportResponse>() {
                public void onComplete() {
                    Log.d(EnglishContentFragment.TAG, "onCompleted");
                    Launcher_EnglishPage_ViewHolder.isNeedReport = true;
                }

                public void onError(Throwable e) {
                    Launcher_EnglishPage_ViewHolder.isNeedReport = true;
                    Log.d(EnglishContentFragment.TAG, "onError:" + e.getMessage());
                }

                public void onSubscribe(Disposable d) {
                }

                public void onNext(RadarReportResponse radarReportResponse) {
                    Launcher_EnglishPage_ViewHolder.isNeedReport = true;
                    if (radarReportResponse != null) {
                        Log.d(EnglishContentFragment.TAG, "radarReportResponse:" + radarReportResponse.toString());
                    }
                }
            });
            return;
        }
        Launcher_EnglishPage_ViewHolder.isNeedReport = true;
    }
}