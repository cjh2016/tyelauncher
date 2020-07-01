package com.boll.tyelauncher.view;

package com.toycloud.launcher.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.iflytek.cbg.common.utils.ListUtils;
import com.iflytek.easytrans.core.utils.log.LogUtils;
import com.toycloud.launcher.R;
import com.toycloud.launcher.application.LauncherApplication;
import com.toycloud.launcher.biz.globalconfig.CityInfoUpgradeManager;
import com.toycloud.launcher.model.CityModel;
import com.toycloud.launcher.model.EtsConstant;
import framework.hz.salmon.view.OnWheelChangedListener;
import framework.hz.salmon.view.OnWheelScrollListener;
import framework.hz.salmon.view.WheelView;
import framework.hz.salmon.view.adapter.AbstractWheelTextAdapter1;
import java.util.ArrayList;
import java.util.List;

public class CityPickerPopWin extends PopupWindow implements View.OnClickListener {
    private static final String TAG = "CityPickerPopWin";
    private TextView btnSure;
    /* access modifiers changed from: private */
    public AddressTextAdapter cityAdapter;
    /* access modifiers changed from: private */
    public List<CityModel> citys_model;
    /* access modifiers changed from: private */
    public String curRegionId = "340104";
    private View lyChangeAddressChild;
    /* access modifiers changed from: private */
    public int mCurrent_Province_Position = 0;
    /* access modifiers changed from: private */
    public int maxsize = LauncherApplication.getContext().getResources().getDimensionPixelSize(R.dimen.px17);
    /* access modifiers changed from: private */
    public int minsize = LauncherApplication.getContext().getResources().getDimensionPixelSize(R.dimen.px12);
    private OnAddressCListener onAddressCListener;
    /* access modifiers changed from: private */
    public AddressTextAdapter provinceAdapter;
    /* access modifiers changed from: private */
    public String strCity = "合肥市";
    /* access modifiers changed from: private */
    public String strProvince = "安徽省";
    /* access modifiers changed from: private */
    public WheelView wvCitys;
    private WheelView wvProvince;

    public interface OnAddressCListener {
        void onClick(String str, String str2, String str3);
    }

    public CityPickerPopWin(final Context context, String province, String city) {
        super(context);
        View view = View.inflate(context, R.layout.edit_changeaddress_pop_layout, (ViewGroup) null);
        view.setOnClickListener(this);
        this.wvProvince = (WheelView) view.findViewById(R.id.wv_address_province);
        this.wvCitys = (WheelView) view.findViewById(R.id.wv_address_city);
        this.lyChangeAddressChild = view.findViewById(R.id.ly_myinfo_changeaddress_child);
        this.btnSure = (TextView) view.findViewById(R.id.btn_confirm);
        this.strProvince = province;
        this.strCity = city;
        setContentView(view);
        setWidth(-1);
        setHeight(-1);
        setFocusable(true);
        setClippingEnabled(false);
        setBackgroundDrawable(new BitmapDrawable());
        setAnimationStyle(R.style.FadeInPopWin);
        this.lyChangeAddressChild.setOnClickListener(this);
        this.btnSure.setOnClickListener(this);
        this.provinceAdapter = new AddressTextAdapter(context, CityInfoUpgradeManager.getInstance().getProvinceList(), getProvinceItem(this.strProvince), this.maxsize, this.minsize);
        this.wvProvince.setVisibleItems(5);
        this.wvProvince.setViewAdapter(this.provinceAdapter);
        this.wvProvince.setCurrentItem(getProvinceItem(this.strProvince));
        setTextviewSize(this.strProvince, this.provinceAdapter);
        String provinceCode = "110000";
        int provinceCount = ListUtils.size(CityInfoUpgradeManager.getInstance().getProvinceList());
        for (int i = 0; i < provinceCount; i++) {
            CityModel cityModel = CityInfoUpgradeManager.getInstance().getProvinceList().get(i);
            if (cityModel.getAreaname().equals(this.strProvince)) {
                provinceCode = cityModel.getId();
            }
        }
        this.citys_model = CityInfoUpgradeManager.getInstance().getCityList(provinceCode);
        if (ListUtils.isEmpty((List) this.citys_model)) {
            LogUtils.d(TAG, "CityPickerPopWin: 当前省份对应的地市信息为空");
            return;
        }
        this.curRegionId = this.citys_model.get(getCityItem(provinceCode, this.strCity)).getId();
        this.cityAdapter = new AddressTextAdapter(context, this.citys_model, getCityItem(provinceCode, this.strCity), this.maxsize, this.minsize);
        this.wvCitys.setVisibleItems(5);
        this.wvCitys.setViewAdapter(this.cityAdapter);
        this.wvCitys.setCurrentItem(getCityItem(provinceCode, this.strCity));
        setTextviewSize(this.strCity, this.cityAdapter);
        this.wvProvince.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) CityPickerPopWin.this.provinceAdapter.getItemText(wheel.getCurrentItem());
                String unused = CityPickerPopWin.this.strProvince = currentText;
                CityPickerPopWin.this.setTextviewSize(currentText, CityPickerPopWin.this.provinceAdapter);
                int unused2 = CityPickerPopWin.this.mCurrent_Province_Position = wheel.getCurrentItem();
                List unused3 = CityPickerPopWin.this.citys_model = CityInfoUpgradeManager.getInstance().getCityList(CityInfoUpgradeManager.getInstance().getProvinceList().get(wheel.getCurrentItem()).getId());
                if (!ListUtils.isEmpty(CityPickerPopWin.this.citys_model)) {
                    CityModel currentCity = (CityModel) CityPickerPopWin.this.citys_model.get(0);
                    String unused4 = CityPickerPopWin.this.strCity = currentCity.getAreaname();
                    String unused5 = CityPickerPopWin.this.curRegionId = currentCity.getId();
                    AddressTextAdapter unused6 = CityPickerPopWin.this.cityAdapter = new AddressTextAdapter(context, CityPickerPopWin.this.citys_model, 0, CityPickerPopWin.this.maxsize, CityPickerPopWin.this.minsize);
                    CityPickerPopWin.this.wvCitys.setVisibleItems(5);
                    CityPickerPopWin.this.wvCitys.setViewAdapter(CityPickerPopWin.this.cityAdapter);
                    CityPickerPopWin.this.wvCitys.setCurrentItem(0);
                    CityPickerPopWin.this.setTextviewSize(EtsConstant.SUCCESS, CityPickerPopWin.this.cityAdapter);
                    return;
                }
                LogUtils.d(CityPickerPopWin.TAG, "onChanged: 数据异常，该省份对应的地市信息为空");
            }
        });
        this.wvProvince.addScrollingListener(new OnWheelScrollListener() {
            public void onScrollingStarted(WheelView wheel) {
            }

            public void onScrollingFinished(WheelView wheel) {
                CityPickerPopWin.this.setTextviewSize((String) CityPickerPopWin.this.provinceAdapter.getItemText(wheel.getCurrentItem()), CityPickerPopWin.this.provinceAdapter);
                int unused = CityPickerPopWin.this.mCurrent_Province_Position = wheel.getCurrentItem();
                List unused2 = CityPickerPopWin.this.citys_model = CityInfoUpgradeManager.getInstance().getCityList(CityInfoUpgradeManager.getInstance().getProvinceList().get(CityPickerPopWin.this.mCurrent_Province_Position).getId());
                if (ListUtils.isEmpty(CityPickerPopWin.this.citys_model)) {
                    LogUtils.d(CityPickerPopWin.TAG, "onScrollingFinished: 当前省份对应的地市信息为空，无法保存");
                } else {
                    String unused3 = CityPickerPopWin.this.curRegionId = ((CityModel) CityPickerPopWin.this.citys_model.get(0)).getId();
                }
            }
        });
        this.wvCitys.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) CityPickerPopWin.this.cityAdapter.getItemText(wheel.getCurrentItem());
                String unused = CityPickerPopWin.this.strCity = currentText;
                CityPickerPopWin.this.setTextviewSize(currentText, CityPickerPopWin.this.cityAdapter);
                String unused2 = CityPickerPopWin.this.curRegionId = ((CityModel) CityPickerPopWin.this.citys_model.get(wheel.getCurrentItem())).getId();
            }
        });
        this.wvCitys.addScrollingListener(new OnWheelScrollListener() {
            public void onScrollingStarted(WheelView wheel) {
            }

            public void onScrollingFinished(WheelView wheel) {
                CityPickerPopWin.this.setTextviewSize((String) CityPickerPopWin.this.cityAdapter.getItemText(wheel.getCurrentItem()), CityPickerPopWin.this.cityAdapter);
                String unused = CityPickerPopWin.this.curRegionId = ((CityModel) CityPickerPopWin.this.citys_model.get(wheel.getCurrentItem())).getId();
            }
        });
    }

    private class AddressTextAdapter extends AbstractWheelTextAdapter1 {
        List<CityModel> list;

        protected AddressTextAdapter(Context context, List<CityModel> list2, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_birth_year, 0, currentItem, maxsize, minsize);
            this.list = list2;
            setItemTextResource(R.id.tempValue);
        }

        public View getItem(int index, View cachedView, ViewGroup parent) {
            return super.getItem(index, cachedView, parent);
        }

        public int getItemsCount() {
            return this.list.size();
        }

        /* access modifiers changed from: protected */
        public CharSequence getItemText(int index) {
            if (ListUtils.isEmpty((List) this.list)) {
                return "";
            }
            return this.list.get(index).getAreaname();
        }
    }

    public void setTextviewSize(String curriteItemText, AddressTextAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            if (curriteItemText.equals(textvew.getText().toString())) {
                textvew.setTextSize((float) this.maxsize);
                textvew.setTextColor(Color.parseColor("#ff03aaf3"));
            } else {
                textvew.setTextSize((float) this.minsize);
                textvew.setTextColor(Color.parseColor("#ff333333"));
            }
        }
    }

    public void setAddresskListener(OnAddressCListener onAddressCListener2) {
        this.onAddressCListener = onAddressCListener2;
    }

    public void onClick(View v) {
        if (v == this.btnSure) {
            if (this.onAddressCListener != null) {
                this.onAddressCListener.onClick(this.strProvince, this.strCity, this.curRegionId);
            }
        } else if (v != this.lyChangeAddressChild) {
            dismiss();
        } else {
            return;
        }
        dismiss();
    }

    private int getProvinceItem(String province) {
        List<CityModel> list = CityInfoUpgradeManager.getInstance().getProvinceList();
        int size = ListUtils.size(list);
        for (int i = 0; i < size; i++) {
            if (TextUtils.equals(list.get(i).getAreaname(), province)) {
                return i;
            }
        }
        this.strProvince = "北京";
        return 18;
    }

    private int getCityItem(String currentProvinceCode, String city) {
        List<CityModel> list = CityInfoUpgradeManager.getInstance().getCityList(currentProvinceCode);
        int size = ListUtils.size(list);
        for (int i = 0; i < size; i++) {
            if (TextUtils.equals(city, list.get(i).getAreaname())) {
                return i;
            }
        }
        return 0;
    }
}
