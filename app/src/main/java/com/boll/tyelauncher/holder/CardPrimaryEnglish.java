package com.boll.tyelauncher.holder;

package com.toycloud.launcher.holder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.iflytek.stats.StatsHelper;
import com.toycloud.launcher.R;
import com.toycloud.launcher.api.model.User;
import com.toycloud.launcher.common.GlobalVariable;
import com.toycloud.launcher.model.AppContants;
import com.toycloud.launcher.model.EtsConstant;
import com.toycloud.launcher.myinterface.Listener_Update;
import com.toycloud.launcher.util.EtsUtils;
import com.toycloud.launcher.util.NoDoubleClickUtils;
import com.toycloud.launcher.util.PackageUtils;
import com.toycloud.launcher.util.PkgUtil;
import com.toycloud.launcher.util.SharepreferenceUtil;
import com.toycloud.launcher.util.UpdateUtil;
import com.toycloud.launcher.view.DialogUtil;
import com.toycloud.launcher.view.SegmentCardView;

public class CardPrimaryEnglish extends BaseHolder implements View.OnClickListener {
    private static final String TAG = "CardPrimaryEnglish";
    /* access modifiers changed from: private */
    public Context mContext;
    private SegmentCardView scMicroClass;
    private SegmentCardView scOralEnglish;
    private SegmentCardView scPhoneticPractice;
    private SegmentCardView scReciteBook;
    private SegmentCardView scReciteWords;
    private SegmentCardView scSynPassThrough;

    public CardPrimaryEnglish(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public int getLayoutId() {
        return R.layout.fragment_primary_english;
    }

    /* access modifiers changed from: protected */
    public void initView(Context context, View rootView) {
        this.mContext = context;
        this.scSynPassThrough = (SegmentCardView) rootView.findViewById(R.id.sc_syn_pass_through);
        this.scOralEnglish = (SegmentCardView) rootView.findViewById(R.id.sc_oral_english);
        this.scReciteWords = (SegmentCardView) rootView.findViewById(R.id.sc_recite_words);
        this.scPhoneticPractice = (SegmentCardView) rootView.findViewById(R.id.sc_phonetic_practice);
        this.scMicroClass = (SegmentCardView) rootView.findViewById(R.id.sc_micro_class);
        this.scReciteBook = (SegmentCardView) rootView.findViewById(R.id.sc_recite_book);
        this.scSynPassThrough.setOnClickListener(this);
        this.scOralEnglish.setOnClickListener(this);
        this.scReciteWords.setOnClickListener(this);
        this.scPhoneticPractice.setOnClickListener(this);
        this.scMicroClass.setOnClickListener(this);
        this.scReciteBook.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sc_syn_pass_through:
                StatsHelper.primaryEnglishClickTBCG();
                startApp(EtsConstant.ETS_LITE_PACKAGE);
                return;
            case R.id.sc_oral_english:
                StatsHelper.primaryEnglishClickKYLX();
                startApp(AppContants.ORAL_ENGLISH);
                return;
            case R.id.sc_recite_words:
                StatsHelper.primaryEnglishClickJDC();
                startApp("com.iflytek.dictatewords");
                return;
            case R.id.sc_recite_book:
                StatsHelper.primaryEnglishReciteBook();
                startApp(AppContants.PRIMARY_ENGLISH_RECITE_BOOK);
                return;
            case R.id.sc_phonetic_practice:
                StatsHelper.primaryEnglishClickYBLX();
                startApp("com.ets100.study");
                return;
            case R.id.sc_micro_class:
                StatsHelper.primaryEnglishClickYYWKT();
                startApp(AppContants.MICRO_ENGLISH);
                return;
            default:
                return;
        }
    }

    public String getStatPageName() {
        return "Fragment_Primary_English";
    }

    private void startApp(final String pkgName) {
        if (NoDoubleClickUtils.isDoubleClick() || TextUtils.isEmpty(pkgName)) {
            return;
        }
        if (!GlobalVariable.isLogin()) {
            DialogUtil.showDialogToLogin(this.mContext);
        } else if (isAvilible(this.mContext, pkgName)) {
            new UpdateUtil(this.mContext, new Listener_Update() {
                public void isNeedUpdate(boolean isUpdate, boolean isMustUpdate) {
                    if (!isMustUpdate && !isUpdate) {
                        try {
                            User userInfo = SharepreferenceUtil.getSharepferenceInstance(CardPrimaryEnglish.this.mContext).getUserInfo();
                            String str = pkgName;
                            char c = 65535;
                            switch (str.hashCode()) {
                                case -1849577035:
                                    if (str.equals("com.ets100.study")) {
                                        c = 5;
                                        break;
                                    }
                                    break;
                                case -622608360:
                                    if (str.equals("com.iflytek.dictatewords")) {
                                        c = 0;
                                        break;
                                    }
                                    break;
                                case -481115140:
                                    if (str.equals(AppContants.ORAL_ENGLISH)) {
                                        c = 3;
                                        break;
                                    }
                                    break;
                                case 155180822:
                                    if (str.equals(AppContants.MICRO_ENGLISH)) {
                                        c = 1;
                                        break;
                                    }
                                    break;
                                case 1116693698:
                                    if (str.equals(EtsConstant.ETS_LITE_PACKAGE)) {
                                        c = 4;
                                        break;
                                    }
                                    break;
                                case 1471857194:
                                    if (str.equals(AppContants.PRIMARY_ENGLISH_RECITE_BOOK)) {
                                        c = 2;
                                        break;
                                    }
                                    break;
                            }
                            switch (c) {
                                case 0:
                                    Intent i = new Intent();
                                    i.putExtra("token", "Bearer " + userInfo.getToken());
                                    i.putExtra(AppContants.KEY_USER_ID, userInfo.getUserid());
                                    i.setComponent(new ComponentName("com.iflytek.dictatewords", AppContants.RECITE_WORDS_START_ACTIVITY));
                                    i.addFlags(268435456);
                                    CardPrimaryEnglish.this.mContext.startActivity(i);
                                    return;
                                case 1:
                                    PkgUtil.startAppByPackName(CardPrimaryEnglish.this.mContext, pkgName);
                                    return;
                                case 2:
                                    PkgUtil.startAppByPackName(CardPrimaryEnglish.this.mContext, pkgName);
                                    return;
                                case 3:
                                    Intent intent = new Intent();
                                    intent.setComponent(new ComponentName(AppContants.ORAL_ENGLISH, AppContants.ORAL_ENGLISH_START_ACTIVITY));
                                    Bundle bundle = new Bundle();
                                    bundle.putString(AppContants.KEY_OPNE_ID, userInfo.getUserid());
                                    bundle.putString(AppContants.KEY_TYPE, "THIRD_STUDYPAD");
                                    Log.d(CardPrimaryEnglish.TAG, "userId:" + userInfo.getUserid() + " type:" + "THIRD_STUDYPAD");
                                    Log.d(CardPrimaryEnglish.TAG, "token:" + userInfo.getToken());
                                    intent.putExtras(bundle);
                                    intent.addFlags(268435456);
                                    CardPrimaryEnglish.this.mContext.startActivity(intent);
                                    return;
                                case 4:
                                    EtsUtils.launchEtsLite(CardPrimaryEnglish.this.mContext, EtsConstant.ACTION_SYNC_CHALLENGE, userInfo);
                                    return;
                                case 5:
                                    EtsUtils.launchEts(CardPrimaryEnglish.this.mContext, "com.ets100.study.ACTION_SOUND_MARK_STUDY", userInfo);
                                    return;
                                default:
                                    return;
                            }
                        } catch (Throwable th) {
                        }
                    }
                }
            }).checkUpdate(pkgName, new PackageUtils(this.mContext).getVersionCode(pkgName));
        } else {
            Toast.makeText(this.mContext, "未找到功能", 0).show();
        }
    }
}
