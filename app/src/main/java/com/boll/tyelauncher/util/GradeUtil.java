package com.boll.tyelauncher.util;



public class GradeUtil {
    public static String getGradeName(String gradeCode) {
        char c = 65535;
        switch (gradeCode.hashCode()) {
            case 1537:
                if (gradeCode.equals(GradeHelper.GRADE_CODE_01)) {
                    c = 0;
                    break;
                }
                break;
            case 1538:
                if (gradeCode.equals("02")) {
                    c = 1;
                    break;
                }
                break;
            case 1539:
                if (gradeCode.equals("03")) {
                    c = 2;
                    break;
                }
                break;
            case 1540:
                if (gradeCode.equals("04")) {
                    c = 3;
                    break;
                }
                break;
            case 1541:
                if (gradeCode.equals("05")) {
                    c = 4;
                    break;
                }
                break;
            case 1542:
                if (gradeCode.equals("06")) {
                    c = 5;
                    break;
                }
                break;
            case 1543:
                if (gradeCode.equals(GradeHelper.GRADE_CODE_07)) {
                    c = 6;
                    break;
                }
                break;
            case 1544:
                if (gradeCode.equals(GradeHelper.GRADE_CODE_08)) {
                    c = 7;
                    break;
                }
                break;
            case 1545:
                if (gradeCode.equals(GradeHelper.GRADE_CODE_09)) {
                    c = 8;
                    break;
                }
                break;
            case 1567:
                if (gradeCode.equals("10")) {
                    c = 9;
                    break;
                }
                break;
            case 1568:
                if (gradeCode.equals(GradeHelper.GRADE_CODE_11)) {
                    c = 10;
                    break;
                }
                break;
            case 1569:
                if (gradeCode.equals("12")) {
                    c = 11;
                    break;
                }
                break;
            case 1576:
                if (gradeCode.equals("19")) {
                    c = 12;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return "一年级";
            case 1:
                return "二年级";
            case 2:
                return "三年级";
            case 3:
                return "四年级";
            case 4:
                return "五年级";
            case 5:
                return "六年级";
            case 6:
                return "七年级";
            case 7:
                return "八年级";
            case 8:
                return "九年级";
            case 9:
                return "高一";
            case 10:
                return "高二";
            case 11:
                return "高三";
            case 12:
                return "高中";
            default:
                return "";
        }
    }

    public static boolean isPrimarySchoolGrade(String gradeCode) {
        char c = 65535;
        switch (gradeCode.hashCode()) {
            case 1537:
                if (gradeCode.equals(GradeHelper.GRADE_CODE_01)) {
                    c = 0;
                    break;
                }
                break;
            case 1538:
                if (gradeCode.equals("02")) {
                    c = 1;
                    break;
                }
                break;
            case 1539:
                if (gradeCode.equals("03")) {
                    c = 2;
                    break;
                }
                break;
            case 1540:
                if (gradeCode.equals("04")) {
                    c = 3;
                    break;
                }
                break;
            case 1541:
                if (gradeCode.equals("05")) {
                    c = 4;
                    break;
                }
                break;
            case 1542:
                if (gradeCode.equals("06")) {
                    c = 5;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return true;
            default:
                return false;
        }
    }
}