package com.boll.tyelauncher.view;

package com.toycloud.launcher.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.iflytek.cbg.common.utils.TimeUtils;
import com.toycloud.launcher.R;
import com.toycloud.launcher.model.EtsConstant;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatePickerPopWin extends PopupWindow implements View.OnClickListener {
    private static final int DEFAULT_MIN_YEAR = 1920;
    public TextView confirmBtn;
    public View contentView;
    List<String> dayList = new ArrayList();
    public LoopView dayLoopView;
    /* access modifiers changed from: private */
    public int dayPos = 0;
    private Context mContext;
    private OnDatePickedListener mListener;
    private int maxDay;
    private int maxMonth;
    private int maxYear;
    private int minYear;
    List<String> monthList = new ArrayList();
    public LoopView monthLoopView;
    /* access modifiers changed from: private */
    public int monthPos = 0;
    public View pickerContainerV;
    private boolean showDayMonthYear;
    List<String> yearList = new ArrayList();
    public LoopView yearLoopView;
    /* access modifiers changed from: private */
    public int yearPos = 0;

    public interface OnDatePickedListener {
        void onDatePickCompleted(int i, int i2, int i3, String str);
    }

    public static class Builder {
        /* access modifiers changed from: private */
        public Context context;
        /* access modifiers changed from: private */
        public String dateChose = DatePickerPopWin.getStrDate();
        /* access modifiers changed from: private */
        public OnDatePickedListener listener;
        /* access modifiers changed from: private */
        public int maxDay = Calendar.getInstance().get(5);
        /* access modifiers changed from: private */
        public int maxMonth = (Calendar.getInstance().get(2) + 1);
        /* access modifiers changed from: private */
        public int maxYear = (Calendar.getInstance().get(1) + 1);
        /* access modifiers changed from: private */
        public int minYear = DatePickerPopWin.DEFAULT_MIN_YEAR;
        /* access modifiers changed from: private */
        public boolean showDayMonthYear = false;

        public Builder(Context context2, OnDatePickedListener listener2) {
            this.context = context2;
            this.listener = listener2;
        }

        public int getMaxMonth() {
            return this.maxMonth;
        }

        public Builder setMaxMonth(int maxMonth2) {
            this.maxMonth = maxMonth2;
            return this;
        }

        public int getMaxDay() {
            return this.maxDay;
        }

        public Builder setMaxDay(int maxDay2) {
            this.maxDay = maxDay2;
            return this;
        }

        public Builder minYear(int minYear2) {
            this.minYear = minYear2;
            return this;
        }

        public Builder maxYear(int maxYear2) {
            this.maxYear = maxYear2;
            return this;
        }

        public Builder dateChose(String dateChose2) {
            if (!TextUtils.isEmpty(dateChose2)) {
                this.dateChose = dateChose2;
            }
            return this;
        }

        public DatePickerPopWin build() {
            if (this.minYear <= this.maxYear) {
                return new DatePickerPopWin(this);
            }
            throw new IllegalArgumentException();
        }

        public Builder showDayMonthYear(boolean useDayMonthYear) {
            this.showDayMonthYear = useDayMonthYear;
            return this;
        }
    }

    public DatePickerPopWin(Builder builder) {
        this.minYear = builder.minYear;
        this.maxYear = builder.maxYear;
        this.mContext = builder.context;
        this.mListener = builder.listener;
        this.maxDay = builder.maxDay;
        this.maxMonth = builder.maxMonth;
        this.showDayMonthYear = builder.showDayMonthYear;
        setSelectedDate(builder.dateChose);
        initView();
    }

    private void initView() {
        this.contentView = LayoutInflater.from(this.mContext).inflate(R.layout.layout_date_picker_inverted, (ViewGroup) null);
        this.confirmBtn = (TextView) this.contentView.findViewById(R.id.btn_confirm);
        this.yearLoopView = (LoopView) this.contentView.findViewById(R.id.picker_year);
        this.monthLoopView = (LoopView) this.contentView.findViewById(R.id.picker_month);
        this.dayLoopView = (LoopView) this.contentView.findViewById(R.id.picker_day);
        this.pickerContainerV = this.contentView.findViewById(R.id.container_picker);
        this.yearLoopView.setLoopListener(new LoopScrollListener() {
            public void onItemSelect(int item) {
                int unused = DatePickerPopWin.this.yearPos = item;
                DatePickerPopWin.this.initDayPickerView();
            }
        });
        this.monthLoopView.setLoopListener(new LoopScrollListener() {
            public void onItemSelect(int item) {
                int unused = DatePickerPopWin.this.monthPos = item;
                DatePickerPopWin.this.initDayPickerView();
            }
        });
        this.dayLoopView.setLoopListener(new LoopScrollListener() {
            public void onItemSelect(int item) {
                int unused = DatePickerPopWin.this.dayPos = item;
            }
        });
        initPickerViews();
        initDayPickerView();
        this.confirmBtn.setOnClickListener(this);
        this.contentView.setOnClickListener(this);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setAnimationStyle(R.style.FadeInPopWin);
        setClippingEnabled(false);
        setContentView(this.contentView);
        setWidth(-1);
        setHeight(-1);
    }

    private void initPickerViews() {
        this.yearList.clear();
        Log.e("date--->", this.maxYear + ":" + this.maxMonth + ":" + this.maxDay);
        int yearCount = this.maxYear - this.minYear;
        for (int i = 0; i < yearCount; i++) {
            this.yearList.add(format2LenStr(this.minYear + i));
        }
        this.yearLoopView.setDataList((ArrayList) this.yearList);
        this.yearLoopView.setInitPosition(this.yearPos);
    }

    /* access modifiers changed from: private */
    public void initDayPickerView() {
        Calendar calendar = Calendar.getInstance();
        this.dayList = new ArrayList();
        calendar.set(1, this.minYear + this.yearPos);
        calendar.set(2, this.monthPos);
        int dayMaxInMonth = calendar.getActualMaximum(5);
        if (this.yearPos + this.minYear == this.maxYear - 1 && this.monthPos + 1 == this.maxMonth) {
            for (int i = 0; i < this.maxDay; i++) {
                this.dayList.add(format2LenStr(i + 1));
            }
        } else {
            for (int i2 = 0; i2 < dayMaxInMonth; i2++) {
                this.dayList.add(format2LenStr(i2 + 1));
            }
        }
        this.monthList.clear();
        Log.e("date-->", this.yearPos + ":" + this.monthPos + ":" + this.dayPos + ":::::s");
        if (this.yearPos + this.minYear == this.maxYear - 1) {
            for (int j = 0; j < this.maxMonth; j++) {
                this.monthList.add(format2LenStr(j + 1));
            }
        } else {
            for (int j2 = 0; j2 < 12; j2++) {
                this.monthList.add(format2LenStr(j2 + 1));
            }
        }
        this.monthLoopView.setDataList((ArrayList) this.monthList);
        Log.e("monthLoopView---->", this.monthPos + "");
        this.dayLoopView.setDataList((ArrayList) this.dayList);
        this.dayLoopView.setInitPosition(this.dayPos);
    }

    public void setSelectedDate(String dateStr) {
        if (!TextUtils.isEmpty(dateStr)) {
            long milliseconds = getLongFromyyyyMMdd(dateStr);
            Calendar calendar = Calendar.getInstance(Locale.CHINA);
            if (milliseconds != -1) {
                calendar.setTimeInMillis(milliseconds);
                this.yearPos = calendar.get(1) - this.minYear;
                this.monthPos = calendar.get(2);
                this.dayPos = calendar.get(5) - 1;
            }
        }
    }

    public void showPopWin(Activity activity) {
        if (activity != null) {
            showAtLocation(activity.getWindow().getDecorView(), 17, 0, 0);
        }
    }

    public void dismissPopWin() {
        dismiss();
    }

    public void onClick(View v) {
        if (v == this.contentView) {
            dismissPopWin();
        } else if (v == this.confirmBtn) {
            if (this.mListener != null) {
                int year = this.minYear + this.yearPos;
                int month = this.monthPos + 1;
                int day = this.dayPos + 1;
                StringBuffer sb = new StringBuffer();
                sb.append(String.valueOf(year));
                sb.append("-");
                sb.append(format2LenStr(month));
                sb.append("-");
                sb.append(format2LenStr(day));
                this.mListener.onDatePickCompleted(year, month, day, sb.toString());
            }
            dismissPopWin();
        }
    }

    public static long getLongFromyyyyMMdd(String date) {
        Date parse = null;
        try {
            parse = new SimpleDateFormat(TimeUtils.yyyy_MM_dd, Locale.getDefault()).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (parse != null) {
            return parse.getTime();
        }
        return -1;
    }

    public static String getStrDate() {
        Date date;
        SimpleDateFormat dd = new SimpleDateFormat(TimeUtils.yyyy_MM_dd, Locale.CHINA);
        try {
            date = dd.parse("2000-01-01");
        } catch (ParseException e) {
            date = new Date();
            e.printStackTrace();
        }
        return dd.format(date);
    }

    public static String format2LenStr(int num) {
        return num < 10 ? EtsConstant.SUCCESS + num : String.valueOf(num);
    }

    public static int spToPx(Context context, int spValue) {
        return (int) ((((float) spValue) * context.getResources().getDisplayMetrics().scaledDensity) + 0.5f);
    }
}