package com.boll.tyelauncher.holder;

package com.toycloud.launcher.holder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import com.iflytek.framelib.base.Constants;
import com.iflytek.stats.StatsHelper;
import com.toycloud.launcher.R;
import com.toycloud.launcher.common.GlobalVariable;
import com.toycloud.launcher.model.AppContants;
import com.toycloud.launcher.myinterface.Listener_Update;
import com.toycloud.launcher.util.GradeUtil;
import com.toycloud.launcher.util.NoDoubleClickUtils;
import com.toycloud.launcher.util.PackageUtils;
import com.toycloud.launcher.util.PkgUtil;
import com.toycloud.launcher.util.SharepreferenceUtil;
import com.toycloud.launcher.util.UpdateUtil;
import com.toycloud.launcher.view.DialogUtil;
import com.toycloud.launcher.view.SegmentCardView;

public class CardPrimaryMath extends BaseHolder implements View.OnClickListener {
    private static final String TAG = "Math_ViewHolder";
    /* access modifiers changed from: private */
    public Context mContext;
    private SegmentCardView scAccurateLearning;
    private SegmentCardView scThinkingTraining;
    private SegmentCardView scWrongBook;

    public CardPrimaryMath(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public int getLayoutId() {
        return R.layout.fragment_primary_math;
    }

    /* access modifiers changed from: protected */
    public void initView(Context context, View rootView) {
        this.mContext = context;
        this.scAccurateLearning = (SegmentCardView) rootView.findViewById(R.id.sc_accurate_learning);
        this.scThinkingTraining = (SegmentCardView) rootView.findViewById(R.id.sc_thinking_training);
        this.scWrongBook = (SegmentCardView) rootView.findViewById(R.id.sc_wrong_book);
        this.scAccurateLearning.setOnClickListener(this);
        this.scThinkingTraining.setOnClickListener(this);
        this.scWrongBook.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sc_accurate_learning:
                StatsHelper.primaryMathClickJZXX();
                startApp("com.iflytek.recommend_tsp");
                return;
            case R.id.sc_thinking_training:
                StatsHelper.primaryMathClickSWXL();
                startApp(AppContants.THINKING_TRAIN);
                return;
            case R.id.sc_wrong_book:
                StatsHelper.primaryMathClickCTB();
                startApp("com.iflytek.wrongnotebook");
                return;
            default:
                return;
        }
    }

    public String getStatPageName() {
        return "Fragment_Primary_Math";
    }

    private void startApp(final String pkgName) {
        if (!NoDoubleClickUtils.isDoubleClick()) {
            if (!GlobalVariable.isLogin()) {
                DialogUtil.showDialogToLogin(this.mContext);
            } else if (TextUtils.isEmpty(pkgName)) {
            } else {
                if (isAvilible(this.mContext, pkgName)) {
                    new UpdateUtil(this.mContext, new Listener_Update() {
                        public void isNeedUpdate(boolean isUpdate, boolean isMustUpdate) {
                            if (!isMustUpdate && !isUpdate) {
                                try {
                                    String str = pkgName;
                                    char c = 65535;
                                    switch (str.hashCode()) {
                                        case -1496893291:
                                            if (str.equals(AppContants.THINKING_TRAIN)) {
                                                c = 0;
                                                break;
                                            }
                                            break;
                                        case -1265187293:
                                            if (str.equals("com.iflytek.recommend_tsp")) {
                                                c = 2;
                                                break;
                                            }
                                            break;
                                        case -785672995:
                                            if (str.equals("com.iflytek.wrongnotebook")) {
                                                c = 1;
                                                break;
                                            }
                                            break;
                                    }
                                    switch (c) {
                                        case 0:
                                            PkgUtil.startAppByPackName(CardPrimaryMath.this.mContext, pkgName);
                                            return;
                                        case 1:
                                            Intent intent = new Intent();
                                            intent.addFlags(268435456);
                                            intent.putExtra("token", CardPrimaryMath.this.userInfo.getToken());
                                            intent.putExtra(AppContants.KEY_GRADE_NAME, GradeUtil.getGradeName(CardPrimaryMath.this.userInfo.getGradecode()));
                                            intent.putExtra(AppContants.KEY_GRADE_CODE, CardPrimaryMath.this.userInfo.getGradecode());
                                            intent.setComponent(GlobalVariable.createWrongNotebookComponentName());
                                            CardPrimaryMath.this.mContext.startActivity(intent);
                                            return;
                                        case 2:
                                            Intent intentRecommend = new Intent();
                                            ComponentName componentName = new ComponentName("com.iflytek.recommend_tsp", GlobalVariable.RECOMMEND_TSP_STUDY_SYSTEM__ACTIVITY);
                                            intentRecommend.putExtra("token", SharepreferenceUtil.getToken());
                                            intentRecommend.putExtra(Constants.GRADE_CODE, CardPrimaryMath.this.userInfo.getGradecode());
                                            intentRecommend.putExtra("username", CardPrimaryMath.this.userInfo.getUsername());
                                            intentRecommend.putExtra(AppContants.KEY_USER_ID, CardPrimaryMath.this.userInfo.getUserid());
                                            intentRecommend.putExtra(GlobalVariable.KEY_SUBJECT_CODE, "02");
                                            intentRecommend.setComponent(componentName);
                                            intentRecommend.addFlags(268435456);
                                            CardPrimaryMath.this.mContext.startActivity(intentRecommend);
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
    }
}
