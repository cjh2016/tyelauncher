package com.boll.tyelauncher.view;

package com.toycloud.launcher.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.toycloud.launcher.R;
import com.toycloud.launcher.util.CustomToast;
import com.toycloud.launcher.util.WifiSupport;

public class WifiLinkDialog extends Dialog implements View.OnClickListener {
    private Button cancel_button;
    /* access modifiers changed from: private */
    public String capabilities;
    /* access modifiers changed from: private */
    public Button cofirm_button;
    boolean isOpen = false;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public EditText password_edit;
    private TextView text_name;
    private String text_nameString = null;
    private ToSureConnectNet toSureConnectNet;

    public interface ToSureConnectNet {
        void connect(String str, int i);
    }

    public WifiLinkDialog(@NonNull Context context, @StyleRes int themeResId, String text_nameString2, String capabilities2, ToSureConnectNet toSureConnectNet2) {
        super(context, themeResId);
        this.text_nameString = text_nameString2;
        this.capabilities = capabilities2;
        this.mContext = context;
        this.toSureConnectNet = toSureConnectNet2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.setting_wifi_link_dialog, (ViewGroup) null);
        setContentView(view);
        initView(view);
        this.text_name.setText(this.text_nameString);
        initListener();
    }

    private void initListener() {
        this.cancel_button.setOnClickListener(this);
        this.cofirm_button.setOnClickListener(this);
        this.password_edit.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                if (WifiLinkDialog.this.capabilities.contains("WPA") || WifiLinkDialog.this.capabilities.contains("WPA2") || WifiLinkDialog.this.capabilities.contains("WPS")) {
                    if (WifiLinkDialog.this.password_edit.getText() != null || WifiLinkDialog.this.password_edit.getText().toString().length() >= 8) {
                        WifiLinkDialog.this.cofirm_button.setTextColor(Color.parseColor("#2A8AFB"));
                        WifiLinkDialog.this.cofirm_button.setEnabled(true);
                        return;
                    }
                    WifiLinkDialog.this.cofirm_button.setTextColor(Color.parseColor("#999999"));
                    WifiLinkDialog.this.cofirm_button.setEnabled(false);
                } else if (!WifiLinkDialog.this.capabilities.contains("WEP")) {
                } else {
                    if (WifiLinkDialog.this.password_edit.getText() != null || WifiLinkDialog.this.password_edit.getText().toString().length() >= 8) {
                        WifiLinkDialog.this.cofirm_button.setTextColor(Color.parseColor("#2A8AFB"));
                        WifiLinkDialog.this.cofirm_button.setEnabled(true);
                        return;
                    }
                    WifiLinkDialog.this.cofirm_button.setEnabled(false);
                    WifiLinkDialog.this.cofirm_button.setTextColor(Color.parseColor("#999999"));
                }
            }
        });
    }

    @SuppressLint({"ClickableViewAccessibility"})
    private void initView(View view) {
        this.text_name = (TextView) view.findViewById(R.id.wifi_title);
        this.password_edit = (EditText) view.findViewById(R.id.password_edit);
        this.cancel_button = (Button) view.findViewById(R.id.cancel_button);
        this.cofirm_button = (Button) view.findViewById(R.id.cofirm_button);
        this.isOpen = false;
        this.password_edit.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                boolean z = true;
                Drawable drawable = WifiLinkDialog.this.password_edit.getCompoundDrawables()[2];
                if (drawable != null && event.getAction() == 1 && event.getX() > ((float) ((WifiLinkDialog.this.password_edit.getWidth() - WifiLinkDialog.this.password_edit.getPaddingRight()) - drawable.getIntrinsicWidth()))) {
                    Drawable drawable_close = WifiLinkDialog.this.mContext.getResources().getDrawable(R.drawable.but_close);
                    Drawable drawable_open = WifiLinkDialog.this.mContext.getResources().getDrawable(R.drawable.but_open);
                    WifiLinkDialog wifiLinkDialog = WifiLinkDialog.this;
                    if (WifiLinkDialog.this.isOpen) {
                        z = false;
                    }
                    wifiLinkDialog.isOpen = z;
                    if (WifiLinkDialog.this.isOpen) {
                        WifiLinkDialog.this.password_edit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        WifiLinkDialog.this.password_edit.setSelection(WifiLinkDialog.this.password_edit.getText().toString().length());
                        WifiLinkDialog.this.password_edit.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, drawable_open, (Drawable) null);
                    } else {
                        WifiLinkDialog.this.password_edit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        WifiLinkDialog.this.password_edit.setSelection(WifiLinkDialog.this.password_edit.getText().toString().length());
                        WifiLinkDialog.this.password_edit.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, drawable_close, (Drawable) null);
                    }
                }
                return false;
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_button:
                dismiss();
                return;
            case R.id.cofirm_button:
                String password = this.password_edit.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    CustomToast.showToast(this.mContext, "密码不能为空");
                    return;
                } else if (password.length() < 8) {
                    CustomToast.showToast(this.mContext, "密码长度不能少于8个字符");
                    return;
                } else {
                    WifiConfiguration tempConfig = WifiSupport.isExsits(this.text_nameString, getContext());
                    if (tempConfig == null) {
                        WifiSupport.addNetWork(WifiSupport.createWifiConfig(this.text_nameString, password, WifiSupport.getWifiCipher(this.capabilities)), getContext());
                    } else {
                        WifiSupport.addNetWork(tempConfig, getContext());
                    }
                    this.toSureConnectNet.connect(this.text_nameString, 2);
                    dismiss();
                    return;
                }
            default:
                return;
        }
    }
}
