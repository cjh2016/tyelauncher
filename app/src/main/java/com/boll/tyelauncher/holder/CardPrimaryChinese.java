package com.boll.tyelauncher.holder;

package com.toycloud.launcher.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import com.iflytek.stats.StatsHelper;
import com.toycloud.launcher.R;
import com.toycloud.launcher.common.GlobalVariable;
import com.toycloud.launcher.model.AppContants;
import com.toycloud.launcher.myinterface.Listener_Update;
import com.toycloud.launcher.util.NoDoubleClickUtils;
import com.toycloud.launcher.util.PackageUtils;
import com.toycloud.launcher.util.PkgUtil;
import com.toycloud.launcher.util.UpdateUtil;
import com.toycloud.launcher.view.DialogUtil;
import com.toycloud.launcher.view.SegmentCardView;

public class CardPrimaryChinese extends BaseHolder implements View.OnClickListener {
    /* access modifiers changed from: private */
    public Context mContext;
    private SegmentCardView scComposition;
    private SegmentCardView scDictation;
    private SegmentCardView scPoetry;
    private SegmentCardView scRecitingExt;
    private SegmentCardView scStrokeOrder;

    public CardPrimaryChinese(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public int getLayoutId() {
        return R.layout.fragment_primary_chinese;
    }

    /* access modifiers changed from: protected */
    public void initView(Context context, View rootView) {
        this.mContext = context;
        this.scPoetry = (SegmentCardView) rootView.findViewById(R.id.sc_poetry);
        this.scRecitingExt = (SegmentCardView) rootView.findViewById(R.id.sc_reciting_ext);
        this.scDictation = (SegmentCardView) rootView.findViewById(R.id.sc_dictation);
        this.scStrokeOrder = (SegmentCardView) rootView.findViewById(R.id.sc_stroke_order);
        this.scComposition = (SegmentCardView) rootView.findViewById(R.id.sc_composition);
        this.scPoetry.setOnClickListener(this);
        this.scRecitingExt.setOnClickListener(this);
        this.scDictation.setOnClickListener(this);
        this.scStrokeOrder.setOnClickListener(this);
        this.scComposition.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sc_poetry:
                StatsHelper.primaryChineseClickSCTD();
                startApp(AppContants.POETRY_WORLD);
                return;
            case R.id.sc_reciting_ext:
                StatsHelper.primaryChineseClickBKW();
                startApp(AppContants.RECITING_TEXT);
                return;
            case R.id.sc_dictation:
                StatsHelper.primaryChineseClickZCTX();
                startApp(AppContants.WORD_DICTATION);
                return;
            case R.id.sc_stroke_order:
                StatsHelper.primaryChineseHZBS();
                startApp(AppContants.CN_CHARACTERS);
                return;
            case R.id.sc_composition:
                StatsHelper.primaryChineseClickYXZWX();
                startApp(AppContants.COMPOSITION);
                return;
            default:
                return;
        }
    }

    public String getStatPageName() {
        return "Fragment_Primary_Chinese";
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
                                    PkgUtil.startAppByPackName(CardPrimaryChinese.this.mContext, pkgName);
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