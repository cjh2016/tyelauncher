package com.boll.tyelauncher.util;


import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boll.tyelauncher.R;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class PasswordUtil {
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        return Pattern.compile("^1[3-9]\\d{9}$").matcher(str).matches();
    }

    public static boolean isPasswordAllNumber(String str) throws PatternSyntaxException {
        return Pattern.compile("^\\d+$").matcher(str).matches();
    }

    public static boolean isPasswordAllLetter(String str) throws PatternSyntaxException {
        return Pattern.compile("^[A-Za-z]+$").matcher(str).matches();
    }

    public static void checkPassword(AutoHiddenHintEditText editText, ViewGroup viewGroup, TextView text_notice) {
        if (text_notice.isShown()) {
            String inputPassword = editText.getText().toString();
            int length1 = inputPassword.length();
            if (!TextUtils.isEmpty(inputPassword)) {
                boolean isAllNumber = isPasswordAllNumber(inputPassword);
                boolean isAllLetter = isPasswordAllLetter(inputPassword);
                if (length1 < 6) {
                    text_notice.setVisibility(0);
                    viewGroup.setBackgroundResource(R.drawable.edit_bg_red);
                    text_notice.setText("密码格式有错误：密码长度不足6位");
                } else if (isAllNumber) {
                    viewGroup.setBackgroundResource(R.drawable.edit_bg_red);
                    text_notice.setVisibility(0);
                    text_notice.setText("密码格式有错误：不能只用纯数字");
                } else if (isAllLetter) {
                    viewGroup.setBackgroundResource(R.drawable.edit_bg_red);
                    text_notice.setVisibility(0);
                    text_notice.setText("密码格式有错误：不能只用纯字母");
                } else {
                    viewGroup.setBackgroundResource(R.drawable.edit_bg_press);
                    text_notice.setVisibility(8);
                }
            }
        }
    }
}
