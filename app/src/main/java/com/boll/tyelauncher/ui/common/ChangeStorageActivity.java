package com.boll.tyelauncher.ui.common;

package com.toycloud.launcher.ui.common;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.StatFs;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.common.util.UriUtil;
import com.iflytek.easytrans.dependency.logagent.BuildConfig;
import com.iflytek.utils.LogAgentHelper;
import com.toycloud.launcher.R;
import com.toycloud.launcher.db.DBHelper;
import com.toycloud.launcher.holder.helper.StudyCPHelper;
import com.toycloud.launcher.provide.LanucherContentProvider;
import com.toycloud.launcher.util.SharepreferenceUtil;
import com.toycloud.launcher.util.ToastUtils;
import framework.hz.salmon.base.BaseFragmentActivity;
import framework.hz.salmon.util.SystemUtils;
import java.text.DecimalFormat;

public class ChangeStorageActivity extends BaseFragmentActivity {
    @BindView(2131689661)
    View mBackIv;
    @BindView(2131689696)
    ImageView mInnerStorageIv;
    @BindView(2131689694)
    View mInnerStorageLl;
    @BindView(2131689695)
    TextView mInnerStorageTv;
    @BindView(2131689699)
    ImageView mSDCardIv;
    @BindView(2131689697)
    View mSDCardLl;
    @BindView(2131689698)
    TextView mSDCardTv;
    private SdcardReceiver mSdcardReceiver;
    /* access modifiers changed from: private */
    public int mStorageType;

    /* access modifiers changed from: protected */
    @RequiresApi(api = 24)
    public void onCreate(Bundle savedInstanceState) {
        int i = 4;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_storage);
        ButterKnife.bind((Activity) this);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(201326592);
            window.getDecorView().setSystemUiVisibility(1792);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
            window.setNavigationBarColor(0);
        }
        registerReceiver();
        this.mStorageType = SharepreferenceUtil.getInt("storageType", 0);
        this.mInnerStorageIv.setVisibility(this.mStorageType == 0 ? 0 : 4);
        ImageView imageView = this.mSDCardIv;
        if (this.mStorageType == 1) {
            i = 0;
        }
        imageView.setVisibility(i);
        if (SystemUtils.getTFCardPath() == null) {
            this.mSDCardLl.setVisibility(8);
        } else {
            this.mSDCardLl.setVisibility(0);
        }
        this.mSDCardLl.setOnClickListener(this);
        this.mInnerStorageLl.setOnClickListener(this);
        this.mBackIv.setOnClickListener(this);
        getSdcardSize();
    }

    /* access modifiers changed from: private */
    public void getSdcardSize() {
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                setStorageSize(new StatFs(SystemUtils.externalSDCardPath()), this.mInnerStorageTv);
                String tfCardPath = SystemUtils.getTFCardPath();
                if (!TextUtils.isEmpty(tfCardPath)) {
                    try {
                        StatFs statFs = new StatFs(tfCardPath);
                        try {
                            setStorageSize(statFs, this.mSDCardTv);
                            StatFs statFs2 = statFs;
                        } catch (Exception e) {
                            StatFs statFs3 = statFs;
                            ToastUtils.showShort((CharSequence) "设置存储路径异常");
                        }
                    } catch (Exception e2) {
                        ToastUtils.showShort((CharSequence) "设置存储路径异常");
                    }
                }
            } catch (Throwable th) {
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        LogAgentHelper.onActive();
    }

    private void registerReceiver() {
        this.mSdcardReceiver = new SdcardReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals("android.intent.action.MEDIA_MOUNTED")) {
                    ChangeStorageActivity.this.mSDCardLl.setVisibility(0);
                    ChangeStorageActivity.this.getSdcardSize();
                } else if (action.equals("android.intent.action.MEDIA_UNMOUNTED")) {
                    ChangeStorageActivity.this.mSDCardLl.setVisibility(8);
                    int unused = ChangeStorageActivity.this.mStorageType = 0;
                    ChangeStorageActivity.this.updateStorageType();
                    SharepreferenceUtil.putInt("storageType", ChangeStorageActivity.this.mStorageType);
                    ChangeStorageActivity.this.mInnerStorageIv.setVisibility(0);
                    ChangeStorageActivity.this.mSDCardIv.setVisibility(4);
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addDataScheme(UriUtil.LOCAL_FILE_SCHEME);
        intentFilter.addAction("android.intent.action.MEDIA_MOUNTED");
        intentFilter.addAction("android.intent.action.MEDIA_UNMOUNTED");
        registerReceiver(this.mSdcardReceiver, intentFilter);
    }

    @RequiresApi(api = 24)
    private void setStorageSize(StatFs statFs, TextView textView) {
        long blocksize = statFs.getBlockSizeLong();
        long totalblocks = statFs.getBlockCountLong();
        long availableblocks = statFs.getAvailableBlocksLong();
        TextView textView2 = textView;
        textView2.setText("已用" + formatSize(blocksize * (totalblocks - availableblocks)) + "，可用" + formatSize(availableblocks * blocksize));
    }

    /* access modifiers changed from: private */
    public void updateStorageType() {
        new DBHelper(this).getWritableDatabase();
        Uri uri = LanucherContentProvider.PERSON_CONTENT_URI;
        ContentValues contentValues = new ContentValues();
        contentValues.put(StudyCPHelper.COLUMN_NAME_UNIQUE_FLAG, 1);
        contentValues.put("storageType", Integer.valueOf(this.mStorageType));
        getContentResolver().update(uri, contentValues, "flag=?", new String[]{"1"});
    }

    private String formatSize(long size) {
        return new DecimalFormat(BuildConfig.VERSION_NAME).format((((((double) size) * 1.0d) / 1024.0d) / 1024.0d) / 1024.0d) + "G";
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        try {
            unregisterReceiver(this.mSdcardReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    public boolean handleMessage(Message msg) {
        return false;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                return;
            case R.id.ll_inner_storage:
                if (this.mStorageType == 1) {
                    this.mStorageType = 0;
                    updateStorageType();
                    SharepreferenceUtil.putInt("storageType", this.mStorageType);
                    this.mInnerStorageIv.setVisibility(0);
                    this.mSDCardIv.setVisibility(4);
                    return;
                }
                return;
            case R.id.ll_sd_card:
                if (this.mStorageType == 0) {
                    this.mStorageType = 1;
                    updateStorageType();
                    SharepreferenceUtil.putInt("storageType", this.mStorageType);
                    this.mInnerStorageIv.setVisibility(4);
                    this.mSDCardIv.setVisibility(0);
                    return;
                }
                return;
            default:
                return;
        }
    }
}
