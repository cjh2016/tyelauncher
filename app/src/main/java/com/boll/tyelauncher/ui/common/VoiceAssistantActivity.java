package com.boll.tyelauncher.ui.common;

package com.toycloud.launcher.ui.common;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.iflytek.utils.LogAgentHelper;
import com.toycloud.launcher.R;
import com.toycloud.launcher.model.EtsConstant;
import com.toycloud.launcher.util.Utils;
import framework.hz.salmon.base.BaseFragmentActivity;
import java.util.ArrayList;
import java.util.List;

public class VoiceAssistantActivity extends BaseFragmentActivity {
    private static final String APPLICATION_ID = "com.toycloud.app.myskill";
    public static final String AUTHORITY = "com.iflytek.provider.SwitchContentProvider";
    public static final Uri CONTENT_URI_FIRST = Uri.parse("content://com.iflytek.provider.SwitchContentProvider/time");
    private static final String LAUNCH_ACTIVITY = "com.iflytek.ty_myskill.ui.MainActivity";
    public static final int TYPE_SWITCH = 0;
    public static final int TYPE_WAKE_ABLE = 1;
    @BindView(2131689661)
    View mBackIv;
    @BindView(2131689844)
    ImageView mBlackScreenWakeupIv;
    @BindView(2131689841)
    View mBlackScreenWakeupLl;
    /* access modifiers changed from: private */
    public boolean mIsOpenSpirit;
    private boolean mIsWakeAble;
    private VoiceAssistantReceiver mReceiver;
    @BindView(2131689845)
    View mVoiceSkillLl;
    @BindView(2131689840)
    ImageView mVoiceWakeupIv;
    @BindView(2131689839)
    View mVoiceWakeupLl;
    @BindView(2131689842)
    TextView mVoiceWakeupTv1;
    @BindView(2131689843)
    TextView mVoiceWakeupTv2;

    /* access modifiers changed from: protected */
    @RequiresApi(api = 24)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_assistant);
        ButterKnife.bind((Activity) this);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(201326592);
            window.getDecorView().setSystemUiVisibility(1792);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
            window.setNavigationBarColor(0);
            window.getDecorView().setSystemUiVisibility(9216);
        }
        registerReceiver();
        search();
        refreshView();
        this.mBlackScreenWakeupLl.setOnClickListener(this);
        this.mVoiceWakeupLl.setOnClickListener(this);
        this.mVoiceSkillLl.setOnClickListener(this);
        this.mBackIv.setOnClickListener(this);
    }

    /* access modifiers changed from: private */
    public void refreshView() {
        int i = R.drawable.ic_switch_on;
        this.mVoiceWakeupIv.setImageResource(this.mIsOpenSpirit ? R.drawable.ic_switch_on : R.drawable.ic_switch_off);
        ImageView imageView = this.mBlackScreenWakeupIv;
        if (!this.mIsOpenSpirit || !this.mIsWakeAble) {
            i = R.drawable.ic_switch_off;
        }
        imageView.setImageResource(i);
        this.mVoiceWakeupTv1.setTextColor(this.mIsOpenSpirit ? -13421773 : -6710887);
        this.mBlackScreenWakeupLl.setEnabled(this.mIsOpenSpirit);
    }

    private void registerReceiver() {
        this.mReceiver = new VoiceAssistantReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.toycloud.action.SWITCH_SPIRIT");
        registerReceiver(this.mReceiver, intentFilter);
    }

    public boolean handleMessage(Message msg) {
        return false;
    }

    public void onClick(View v) {
        boolean z;
        boolean z2 = true;
        try {
            switch (v.getId()) {
                case R.id.iv_back:
                    finish();
                    return;
                case R.id.ll_voice_wakeup:
                    switchSpirit(0, !this.mIsOpenSpirit);
                    if (!this.mIsOpenSpirit) {
                        z = true;
                    } else {
                        z = false;
                    }
                    this.mIsOpenSpirit = z;
                    this.mIsWakeAble = false;
                    switchSpirit(1, false);
                    ContentResolver contentResolver = getContentResolver();
                    if (!this.mIsOpenSpirit) {
                        z2 = false;
                    }
                    Settings.System.putInt(contentResolver, "toycloud_elf", z2 ? 1 : 0);
                    refreshView();
                    return;
                case R.id.ll_black_screen_wakeup:
                    switchSpirit(1, !this.mIsWakeAble);
                    if (this.mIsWakeAble) {
                        z2 = false;
                    }
                    this.mIsWakeAble = z2;
                    refreshView();
                    return;
                case R.id.ll_voice_skill:
                    gotoVoiceSkill();
                    return;
                default:
                    return;
            }
        } catch (Throwable th) {
        }
    }

    private void gotoVoiceSkill() {
        if (isAvilible(Utils.getContext(), APPLICATION_ID)) {
            try {
                Intent i = new Intent();
                i.setComponent(new ComponentName(APPLICATION_ID, LAUNCH_ACTIVITY));
                startActivity(i);
            } catch (Throwable th) {
            }
        } else {
            Toast.makeText(Utils.getContext(), "请检查是否安装了 我的技能 应用", 0).show();
        }
    }

    private void switchSpirit(int type, boolean isOpen) {
        ContentValues values = new ContentValues();
        values.put(type == 0 ? "switch" : "wake_able", isOpen ? "1" : EtsConstant.SUCCESS);
        if (getContentResolver().query(CONTENT_URI_FIRST, (String[]) null, (String) null, (String[]) null, (String) null).getCount() > 0) {
            getContentResolver().update(CONTENT_URI_FIRST, values, "_id = 1", (String[]) null);
            getContentResolver().notifyChange(CONTENT_URI_FIRST, (ContentObserver) null);
            return;
        }
        getContentResolver().insert(CONTENT_URI_FIRST, values);
        getContentResolver().notifyChange(CONTENT_URI_FIRST, (ContentObserver) null);
    }

    public void search() {
        Cursor cursor = getContentResolver().query(CONTENT_URI_FIRST, (String[]) null, (String) null, (String[]) null, (String) null);
        cursor.moveToFirst();
        String switcher = "1";
        String wake_able = "1";
        while (!cursor.isAfterLast()) {
            switcher = cursor.getString(cursor.getColumnIndex("switch"));
            wake_able = cursor.getString(cursor.getColumnIndex("wake_able"));
            cursor.moveToNext();
        }
        cursor.close();
        if (switcher.equals(EtsConstant.SUCCESS)) {
            this.mIsOpenSpirit = false;
        } else {
            this.mIsOpenSpirit = true;
        }
        if (wake_able.equals(EtsConstant.SUCCESS)) {
            this.mIsWakeAble = false;
        } else {
            this.mIsWakeAble = true;
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        try {
            unregisterReceiver(this.mReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private class VoiceAssistantReceiver extends BroadcastReceiver {
        private VoiceAssistantReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if ("com.toycloud.action.SWITCH_SPIRIT".equals(intent.getAction())) {
                boolean unused = VoiceAssistantActivity.this.mIsOpenSpirit = intent.getBooleanExtra("SWITCH_SPIRIT_IS_OPEN", false);
                VoiceAssistantActivity.this.refreshView();
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean isAvilible(Context context, String packageName) {
        List<PackageInfo> pinfo = context.getPackageManager().getInstalledPackages(0);
        List<String> pName = new ArrayList<>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                pName.add(pinfo.get(i).packageName);
            }
        }
        return pName.contains(packageName);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        LogAgentHelper.onActive();
    }
}
