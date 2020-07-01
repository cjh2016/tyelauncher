package com.boll.tyelauncher.holder;

package com.toycloud.launcher.holder;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.iflytek.stats.StatsHelper;
import com.iflytek.stats.StatsKeys;
import com.toycloud.launcher.R;
import com.toycloud.launcher.api.model.Constants;
import com.toycloud.launcher.util.PackageUtils;
import com.toycloud.launcher.util.SharepreferenceUtil;
import com.toycloud.launcher.view.SegmentCardView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

public class Launcher_ETS_ViewHolder extends BaseHolder {
    private static final String TAG = "Launcher_ETS_ViewHolder";
    private SegmentCardView imgEts;
    private SegmentCardView imgZXW;
    private Context mContext;
    /* access modifiers changed from: private */
    public String mEtsPkgName = Constants.EHEAR_APP;
    /* access modifiers changed from: private */
    public String mZxwPkgName = Constants.ZXW_APP;
    private View viewLine;

    @Retention(RetentionPolicy.SOURCE)
    public @interface PkgName {
        public static final String ETS = "ets";
        public static final String ZXW = "zxw";
    }

    public Launcher_ETS_ViewHolder(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public int getLayoutId() {
        return R.layout.layout_ets;
    }

    /* access modifiers changed from: protected */
    public void initView(Context context, View rootView) {
        this.mContext = context;
        this.imgEts = (SegmentCardView) this.mRootView.findViewById(R.id.img_ets);
        this.imgEts.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Map<String, String> map = new HashMap<>();
                if (!Launcher_ETS_ViewHolder.this.isEtsShow() || !Launcher_ETS_ViewHolder.this.isZXWShow()) {
                    map.put(StatsKeys.K_D_LAUNCHER_STATE, "1");
                } else {
                    map.put(StatsKeys.K_D_LAUNCHER_STATE, "2");
                }
                StatsHelper.etsClickedEnglish(map);
                Launcher_ETS_ViewHolder.this.startApp(Launcher_ETS_ViewHolder.this.mEtsPkgName);
            }
        });
        this.imgZXW = (SegmentCardView) this.mRootView.findViewById(R.id.img_zxw);
        this.imgZXW.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Map<String, String> map = new HashMap<>();
                if (!Launcher_ETS_ViewHolder.this.isEtsShow() || !Launcher_ETS_ViewHolder.this.isZXWShow()) {
                    map.put(StatsKeys.K_D_LAUNCHER_STATE, "1");
                } else {
                    map.put(StatsKeys.K_D_LAUNCHER_STATE, "2");
                }
                StatsHelper.etsClickedZXW(map);
                Launcher_ETS_ViewHolder.this.startApp(Launcher_ETS_ViewHolder.this.mZxwPkgName);
            }
        });
        this.viewLine = this.mRootView.findViewById(R.id.view_line);
    }

    /* access modifiers changed from: private */
    public void startApp(String pkgName) {
        Log.d(TAG, "-------");
        if (PackageUtils.isAvilible(this.mContext, pkgName)) {
            Log.d(TAG, "----------------");
            Intent LaunchIntent = this.mContext.getPackageManager().getLaunchIntentForPackage(pkgName);
            LaunchIntent.addFlags(268435456);
            LaunchIntent.putExtra("token", SharepreferenceUtil.getToken());
            this.mContext.startActivity(LaunchIntent);
            return;
        }
        Toast.makeText(this.mContext, "未安装该应用", 0).show();
    }

    public void refreshView(String pkgName, boolean isShow) {
        if (PkgName.ETS.equals(pkgName)) {
            Log.d(TAG, "refreshView:" + pkgName + isShow);
            if (!isShow) {
                hideViewLine();
                hideETS();
                setImgZXWFull();
            } else if (isZXWShow()) {
                showViewLine();
                showETS();
                setImgEtsHalf();
                setImgZXWHalf();
            } else {
                hideViewLine();
                showETS();
                setImgEtsFull();
            }
        } else if (PkgName.ZXW.equals(pkgName)) {
            Log.d(TAG, "refreshView:" + pkgName + isShow);
            if (!isShow) {
                hideViewLine();
                hideZXW();
                setImgEtsFull();
            } else if (isEtsShow()) {
                showZXW();
                showViewLine();
                setImgEtsHalf();
                setImgZXWHalf();
            } else {
                hideViewLine();
                showZXW();
                setImgZXWFull();
            }
        }
    }

    private void setImgEtsFull() {
        if (this.imgEts != null) {
            this.imgEts.setBackgroundResource(R.drawable.ets_full);
        }
    }

    private void setImgEtsHalf() {
        if (this.imgEts != null) {
            this.imgEts.setBackgroundResource(R.drawable.ets_half);
        }
    }

    private void setImgZXWFull() {
        if (this.imgZXW != null) {
            this.imgZXW.setBackgroundResource(R.drawable.zxw_full);
        }
    }

    private void setImgZXWHalf() {
        if (this.imgZXW != null) {
            this.imgZXW.setBackgroundResource(R.drawable.zxw_half);
        }
    }

    public boolean isEtsShow() {
        return this.imgEts.getVisibility() == 0;
    }

    public boolean isZXWShow() {
        return this.imgZXW.getVisibility() == 0;
    }

    private boolean isViewLineShow() {
        return this.viewLine.getVisibility() == 0;
    }

    private void showZXW() {
        if (this.imgZXW != null && this.imgZXW.getVisibility() == 8) {
            this.imgZXW.setVisibility(0);
        }
    }

    private void hideZXW() {
        if (this.imgZXW != null && this.imgZXW.getVisibility() == 0) {
            this.imgZXW.setVisibility(8);
        }
    }

    private void showViewLine() {
        if (this.viewLine != null && this.viewLine.getVisibility() == 8) {
            this.viewLine.setVisibility(0);
        }
    }

    private void hideViewLine() {
        if (this.viewLine != null && this.viewLine.getVisibility() == 0) {
            this.viewLine.setVisibility(8);
        }
    }

    private void showETS() {
        if (this.imgEts != null && this.imgEts.getVisibility() == 8) {
            this.imgEts.setVisibility(0);
        }
    }

    private void hideETS() {
        if (this.imgEts != null && this.imgEts.getVisibility() == 0) {
            this.imgEts.setVisibility(8);
        }
    }

    public String getStatPageName() {
        return "Fragment_ETSShortcut";
    }
}