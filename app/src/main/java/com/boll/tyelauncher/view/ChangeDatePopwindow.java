package com.boll.tyelauncher.view;

package com.toycloud.launcher.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.toycloud.launcher.R;
import com.toycloud.launcher.view.wheelview.OnWheelChangedListener;
import com.toycloud.launcher.view.wheelview.OnWheelScrollListener;
import com.toycloud.launcher.view.wheelview.WheelView;
import com.toycloud.launcher.view.wheelview.adapter.AbstractWheelTextAdapter1;
import java.util.ArrayList;
import java.util.Calendar;

public class ChangeDatePopwindow extends PopupWindow implements View.OnClickListener {
    /* access modifiers changed from: private */
    public ArrayList<String> arry_days = new ArrayList<>();
    /* access modifiers changed from: private */
    public ArrayList<String> arry_months = new ArrayList<>();
    private ArrayList<String> arry_years = new ArrayList<>();
    private TextView btnSure;
    private Context context;
    private String currentDay = getDay();
    private String currentMonth = getMonth();
    /* access modifiers changed from: private */
    public String currentYear = getYear();
    /* access modifiers changed from: private */
    public String day;
    private boolean issetdata = true;
    /* access modifiers changed from: private */
    public CalendarTextAdapter mDaydapter;
    /* access modifiers changed from: private */
    public CalendarTextAdapter mMonthAdapter;
    /* access modifiers changed from: private */
    public CalendarTextAdapter mYearAdapter;
    /* access modifiers changed from: private */
    public int maxTextSize = 16;
    /* access modifiers changed from: private */
    public int minTextSize = 14;
    /* access modifiers changed from: private */
    public String month;
    private OnBirthListener onBirthListener;
    View rootView;
    /* access modifiers changed from: private */
    public String selectDay;
    /* access modifiers changed from: private */
    public String selectMonth;
    /* access modifiers changed from: private */
    public String selectYear;
    private final TextView title;
    /* access modifiers changed from: private */
    public WheelView wvDay;
    /* access modifiers changed from: private */
    public WheelView wvMonth;
    private WheelView wvYear;

    public interface OnBirthListener {
        void onClick(String str, String str2, String str3);
    }

    public ChangeDatePopwindow(final Context context2, String default_year, String default_month, String default_day) {
        super(context2);
        this.context = context2;
        this.rootView = View.inflate(context2, R.layout.layout_date_picker_new, (ViewGroup) null);
        this.title = (TextView) this.rootView.findViewById(R.id.title);
        this.title.setOnClickListener(this);
        this.wvYear = (WheelView) this.rootView.findViewById(R.id.wv_birth_year);
        this.wvMonth = (WheelView) this.rootView.findViewById(R.id.wv_birth_month);
        this.wvDay = (WheelView) this.rootView.findViewById(R.id.wv_birth_day);
        this.btnSure = (TextView) this.rootView.findViewById(R.id.btn_myinfo_sure);
        this.rootView.setOnClickListener(this);
        setContentView(this.rootView);
        setWidth(-1);
        setHeight(-1);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setClippingEnabled(false);
        this.btnSure.setOnClickListener(this);
        this.month = default_month;
        initYears();
        initMonths(Integer.parseInt(this.month));
        calDays(default_year, this.month);
        initDays(Integer.parseInt(this.day));
        this.wvYear.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) ChangeDatePopwindow.this.mYearAdapter.getItemText(wheel.getCurrentItem());
                String unused = ChangeDatePopwindow.this.selectYear = currentText;
                ChangeDatePopwindow.this.setTextviewSize(currentText, ChangeDatePopwindow.this.mYearAdapter);
                String unused2 = ChangeDatePopwindow.this.currentYear = currentText.substring(0, currentText.length() - 1).toString();
                Log.d("currentYear==", ChangeDatePopwindow.this.currentYear);
                ChangeDatePopwindow.this.setYear(ChangeDatePopwindow.this.currentYear);
                ChangeDatePopwindow.this.initMonths(Integer.parseInt(ChangeDatePopwindow.this.month));
                CalendarTextAdapter unused3 = ChangeDatePopwindow.this.mMonthAdapter = new CalendarTextAdapter(context2, ChangeDatePopwindow.this.arry_months, 0, ChangeDatePopwindow.this.maxTextSize, ChangeDatePopwindow.this.minTextSize);
                ChangeDatePopwindow.this.wvMonth.setVisibleItems(5);
                ChangeDatePopwindow.this.wvMonth.setViewAdapter(ChangeDatePopwindow.this.mMonthAdapter);
                ChangeDatePopwindow.this.wvMonth.setCurrentItem(0);
                ChangeDatePopwindow.this.calDays(ChangeDatePopwindow.this.currentYear, ChangeDatePopwindow.this.month);
            }
        });
        this.wvYear.addScrollingListener(new OnWheelScrollListener() {
            public void onScrollingStarted(WheelView wheel) {
            }

            public void onScrollingFinished(WheelView wheel) {
                ChangeDatePopwindow.this.setTextviewSize((String) ChangeDatePopwindow.this.mYearAdapter.getItemText(wheel.getCurrentItem()), ChangeDatePopwindow.this.mYearAdapter);
            }
        });
        this.wvMonth.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) ChangeDatePopwindow.this.mMonthAdapter.getItemText(wheel.getCurrentItem());
                String unused = ChangeDatePopwindow.this.selectMonth = currentText;
                ChangeDatePopwindow.this.setTextviewSize(currentText, ChangeDatePopwindow.this.mMonthAdapter);
                ChangeDatePopwindow.this.setMonth(currentText.substring(0, 1));
                ChangeDatePopwindow.this.initDays(Integer.parseInt(ChangeDatePopwindow.this.day));
                CalendarTextAdapter unused2 = ChangeDatePopwindow.this.mDaydapter = new CalendarTextAdapter(context2, ChangeDatePopwindow.this.arry_days, 0, ChangeDatePopwindow.this.maxTextSize, ChangeDatePopwindow.this.minTextSize);
                ChangeDatePopwindow.this.wvDay.setVisibleItems(5);
                ChangeDatePopwindow.this.wvDay.setViewAdapter(ChangeDatePopwindow.this.mDaydapter);
                ChangeDatePopwindow.this.wvDay.setCurrentItem(0);
                ChangeDatePopwindow.this.calDays(ChangeDatePopwindow.this.currentYear, ChangeDatePopwindow.this.month);
            }
        });
        this.wvMonth.addScrollingListener(new OnWheelScrollListener() {
            public void onScrollingStarted(WheelView wheel) {
            }

            public void onScrollingFinished(WheelView wheel) {
                ChangeDatePopwindow.this.setTextviewSize((String) ChangeDatePopwindow.this.mMonthAdapter.getItemText(wheel.getCurrentItem()), ChangeDatePopwindow.this.mMonthAdapter);
            }
        });
        this.wvDay.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) ChangeDatePopwindow.this.mDaydapter.getItemText(wheel.getCurrentItem());
                ChangeDatePopwindow.this.setTextviewSize(currentText, ChangeDatePopwindow.this.mDaydapter);
                String unused = ChangeDatePopwindow.this.selectDay = currentText;
            }
        });
        this.wvDay.addScrollingListener(new OnWheelScrollListener() {
            public void onScrollingStarted(WheelView wheel) {
            }

            public void onScrollingFinished(WheelView wheel) {
                ChangeDatePopwindow.this.setTextviewSize((String) ChangeDatePopwindow.this.mDaydapter.getItemText(wheel.getCurrentItem()), ChangeDatePopwindow.this.mDaydapter);
            }
        });
        setDate(default_year, default_month, default_day);
    }

    private void initView() {
        this.mYearAdapter = new CalendarTextAdapter(this.context, this.arry_years, setYear(this.currentYear), this.maxTextSize, this.minTextSize);
        this.wvYear.setVisibleItems(5);
        this.wvYear.setViewAdapter(this.mYearAdapter);
        this.wvYear.setCurrentItem(setYear(this.currentYear));
        this.mMonthAdapter = new CalendarTextAdapter(this.context, this.arry_months, setMonth(this.currentMonth), this.maxTextSize, this.minTextSize);
        this.wvMonth.setVisibleItems(5);
        this.wvMonth.setViewAdapter(this.mMonthAdapter);
        this.wvMonth.setCurrentItem(setMonth(this.currentMonth));
        this.mDaydapter = new CalendarTextAdapter(this.context, this.arry_days, Integer.parseInt(this.currentDay) - 1, this.maxTextSize, this.minTextSize);
        this.wvDay.setVisibleItems(5);
        this.wvDay.setViewAdapter(this.mDaydapter);
        this.wvDay.setCurrentItem(Integer.parseInt(this.currentDay) - 1);
    }

    public void initYears() {
        for (int i = 1950; i <= Integer.parseInt(getYear()); i++) {
            this.arry_years.add(i + "年");
        }
    }

    public void initMonths(int months) {
        this.arry_months.clear();
        for (int i = 1; i <= months; i++) {
            this.arry_months.add(i + "月");
        }
    }

    public void initDays(int days) {
        this.arry_days.clear();
        for (int i = 1; i <= days; i++) {
            this.arry_days.add(i + "日");
        }
    }

    private class CalendarTextAdapter extends AbstractWheelTextAdapter1 {
        ArrayList<String> list;

        protected CalendarTextAdapter(Context context, ArrayList<String> list2, int currentItem, int maxsize, int minsize) {
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
            return this.list.get(index) + "";
        }
    }

    public void setBirthdayListener(OnBirthListener onBirthListener2) {
        this.onBirthListener = onBirthListener2;
    }

    public void onClick(View v) {
        if (v == this.btnSure) {
            if (this.onBirthListener != null) {
                this.onBirthListener.onClick(this.selectYear, this.selectMonth, this.selectDay);
                dismiss();
                Log.d("cy", "" + this.selectYear + "" + this.selectMonth + "" + this.selectDay);
            }
        } else if (v == this.rootView) {
            dismiss();
        } else {
            if (v == this.title) {
            }
        }
    }

    public void setTextviewSize(String curriteItemText, CalendarTextAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            if (curriteItemText.equals(textvew.getText().toString())) {
                textvew.setTextSize((float) this.maxTextSize);
                textvew.setTextColor(Color.parseColor("#0578FA"));
            } else {
                textvew.setTextSize((float) this.minTextSize);
                textvew.setTextColor(Color.parseColor("#666666"));
            }
        }
    }

    public String getYear() {
        return Calendar.getInstance().get(1) + "";
    }

    public String getMonth() {
        return (Calendar.getInstance().get(2) + 1) + "";
    }

    public String getDay() {
        return Calendar.getInstance().get(5) + "";
    }

    public void initData() {
        setDate(getYear(), getMonth(), getDay());
        this.currentDay = "1";
        this.currentMonth = "1";
    }

    public void setDate(String year, String month2, String day2) {
        this.selectYear = year + "年";
        this.selectMonth = month2 + "月";
        this.selectDay = day2 + "日";
        this.issetdata = true;
        this.currentYear = year;
        this.currentMonth = month2;
        this.currentDay = day2;
        if (year == getYear()) {
            this.month = getMonth();
        } else {
            this.month = "12";
        }
        calDays(year, month2);
        initView();
    }

    public int setYear(String year) {
        int yearIndex = 0;
        if (!year.equals(getYear())) {
            this.month = "12";
        } else {
            this.month = getMonth();
        }
        int i = 1950;
        while (i <= Integer.parseInt(getYear()) && i != Integer.parseInt(year)) {
            yearIndex++;
            i++;
        }
        return yearIndex;
    }

    public int setMonth(String month2) {
        int monthIndex = 0;
        calDays(this.currentYear, month2);
        int i = 1;
        while (i < Integer.parseInt(this.month) && Integer.parseInt(month2) != i) {
            monthIndex++;
            i++;
        }
        return monthIndex;
    }

    public void calDays(String year, String month2) {
        boolean leayyear;
        if (Integer.parseInt(year) % 4 != 0 || Integer.parseInt(year) % 100 == 0) {
            leayyear = false;
        } else {
            leayyear = true;
        }
        for (int i = 1; i <= 12; i++) {
            switch (Integer.parseInt(month2)) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    this.day = "31";
                    break;
                case 2:
                    if (!leayyear) {
                        this.day = "28";
                        break;
                    } else {
                        this.day = "29";
                        break;
                    }
                case 4:
                case 6:
                case 9:
                case 11:
                    this.day = "30";
                    break;
            }
        }
    }
}
