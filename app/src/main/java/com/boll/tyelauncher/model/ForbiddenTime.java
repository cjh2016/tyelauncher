package com.boll.tyelauncher.model;


import java.io.Serializable;
import java.util.Calendar;

public class ForbiddenTime implements Serializable {
    public static final int TYPE_WEEK = 0;
    public static final int TYPE_WEEKEND = 1;
    private int endTime;
    private int startTime;
    private int type;

    public int getType() {
        return this.type;
    }

    public void setType(int type2) {
        this.type = type2;
    }

    public int getStartTime() {
        return this.startTime;
    }

    public void setStartTime(int startTime2) {
        this.startTime = startTime2;
    }

    public int getEndTime() {
        return this.endTime;
    }

    public void setEndTime(int endTime2) {
        this.endTime = endTime2;
    }

    public static final boolean equals(ForbiddenTime first, ForbiddenTime second) {
        if (first == second) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }
        if (first.type == second.type && first.startTime == second.startTime && first.endTime == second.endTime) {
            return true;
        }
        return false;
    }

    public boolean isTimeMatch() {
        Calendar calendar = Calendar.getInstance();
        return isTimeMatch((calendar.get(Calendar.HOUR_OF_DAY) * 60) + calendar.get(Calendar.MINUTE));
    }

    public boolean isTimeMatch(int minuteOfDay) {
        return minuteOfDay < this.startTime || minuteOfDay >= this.endTime;
    }

    public boolean isValid() {
        boolean z = true;
        if (this.type != 0 && this.type != 1) {
            return false;
        }
        if (this.endTime <= this.startTime) {
            z = false;
        }
        return z;
    }
}
