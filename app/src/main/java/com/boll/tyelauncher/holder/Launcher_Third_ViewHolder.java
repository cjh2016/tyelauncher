package com.boll.tyelauncher.holder;

package com.toycloud.launcher.holder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.iflytek.stats.StatsHelper;
import com.toycloud.launcher.R;
import com.toycloud.launcher.api.model.User;
import com.toycloud.launcher.common.GlobalVariable;
import com.toycloud.launcher.holder.helper.StudyCPHelper;
import com.toycloud.launcher.model.AppContants;
import com.toycloud.launcher.myinterface.Listener_Update;
import com.toycloud.launcher.util.ForbiddenUtil;
import com.toycloud.launcher.util.GradeUtil;
import com.toycloud.launcher.util.NoDoubleClickUtils;
import com.toycloud.launcher.util.PackageUtils;
import com.toycloud.launcher.util.SharepreferenceUtil;
import com.toycloud.launcher.util.UpdateUtil;
import com.toycloud.launcher.view.DialogUtil;

public class Launcher_Third_ViewHolder extends BaseHolder implements View.OnClickListener {
    private LinearLayout epaper;
    private ImageView img22;
    private ImageView img_micro_class;
    private ImageView iv_english;
    private ImageView iv_search_by_photo;
    private ImageView iv_translate;
    private ImageView iv_wrong_note_book;
    /* access modifiers changed from: private */
    public Context mContext;

    public Launcher_Third_ViewHolder(Context context) {
        super(context);
        this.mContext = context;
    }

    /* access modifiers changed from: protected */
    public int getLayoutId() {
        return R.layout.fragment_epaper;
    }

    /* access modifiers changed from: protected */
    public void initView(Context context, View rootView) {
        this.epaper = (LinearLayout) rootView.findViewById(R.id.epaper);
        this.iv_wrong_note_book = (ImageView) rootView.findViewById(R.id.iv_wrong_note_book);
        this.iv_search_by_photo = (ImageView) rootView.findViewById(R.id.iv_search_by_photo);
        this.img22 = (ImageView) rootView.findViewById(R.id.img22);
        this.img_micro_class = (ImageView) rootView.findViewById(R.id.img_micro_class);
        this.iv_translate = (ImageView) rootView.findViewById(R.id.iv_translate);
        this.iv_english = (ImageView) rootView.findViewById(R.id.iv_learning_english);
        this.epaper.setOnClickListener(this);
        this.iv_wrong_note_book.setOnClickListener(this);
        this.iv_search_by_photo.setOnClickListener(this);
        this.img_micro_class.setOnClickListener(this);
        this.img22.setOnClickListener(this);
        this.iv_translate.setOnClickListener(this);
        this.iv_english.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.epaper:
                if (NoDoubleClickUtils.isDoubleClick()) {
                    return;
                }
                if (!GlobalVariable.isLogin()) {
                    DialogUtil.showDialogToLogin(this.mContext);
                    return;
                } else if (isAvilible(this.mContext, GlobalVariable.SYNCHRONOUSEXERCISE_PKG)) {
                    new UpdateUtil(this.mContext, new Listener_Update() {
                        public void isNeedUpdate(boolean isUpdate, boolean isMustUpdate) {
                            if (!isMustUpdate && !isUpdate) {
                                try {
                                    Intent i = new Intent();
                                    i.putExtra("token", Launcher_Third_ViewHolder.this.userInfo.getToken());
                                    i.putExtra(StudyCPHelper.COLUMN_NAME_GRADE_CODE, Launcher_Third_ViewHolder.this.userInfo.getGradecode());
                                    i.addFlags(268435456);
                                    i.setComponent(new ComponentName(GlobalVariable.SYNCHRONOUSEXERCISE_PKG, GlobalVariable.SYNCHRONOUSEXERCISE_ENTER_ACTIVITY));
                                    Launcher_Third_ViewHolder.this.mContext.startActivity(i);
                                } catch (Throwable th) {
                                }
                            }
                        }
                    }).checkUpdate(GlobalVariable.SYNCHRONOUSEXERCISE_PKG, new PackageUtils(this.mContext).getVersionCode(GlobalVariable.SYNCHRONOUSEXERCISE_PKG));
                    return;
                } else {
                    Toast.makeText(this.mContext, "请检查是否安装了同步练习", 0).show();
                    return;
                }
            case R.id.iv_wrong_note_book:
                StatsHelper.toolClickedWrongBook();
                if (!GlobalVariable.isLogin()) {
                    DialogUtil.showDialogToLogin(this.mContext);
                    return;
                }
                try {
                    if (isAvilible(this.mContext, "com.iflytek.wrongnotebook")) {
                        new UpdateUtil(this.mContext, new Listener_Update() {
                            public void isNeedUpdate(boolean isUpdate, boolean isMustUpdate) {
                                if (!isMustUpdate && !isUpdate) {
                                    try {
                                        Intent intent = new Intent();
                                        intent.addFlags(268435456);
                                        intent.putExtra("token", Launcher_Third_ViewHolder.this.userInfo.getToken());
                                        intent.putExtra(AppContants.KEY_GRADE_NAME, GradeUtil.getGradeName(Launcher_Third_ViewHolder.this.userInfo.getGradecode()));
                                        intent.putExtra(AppContants.KEY_GRADE_CODE, Launcher_Third_ViewHolder.this.userInfo.getGradecode());
                                        intent.setComponent(GlobalVariable.createWrongNotebookComponentName());
                                        Launcher_Third_ViewHolder.this.mContext.startActivity(intent);
                                    } catch (Throwable th) {
                                    }
                                }
                            }
                        }).checkUpdate("com.iflytek.wrongnotebook", new PackageUtils(this.mContext).getVersionCode("com.iflytek.wrongnotebook"));
                        return;
                    } else {
                        Toast.makeText(this.mContext, "请检查是否安装了错题本应用", 0).show();
                        return;
                    }
                } catch (Exception e) {
                    e.getMessage();
                    return;
                }
            case R.id.img_micro_class:
                StatsHelper.toolClickedMSWKT();
                if (!GlobalVariable.isLogin()) {
                    DialogUtil.showDialogToLogin(this.mContext);
                    return;
                } else if (isAvilible(this.mContext, GlobalVariable.MICROCLASS_PKG)) {
                    new UpdateUtil(this.mContext, new Listener_Update() {
                        public void isNeedUpdate(boolean isUpdate, boolean isMustUpdate) {
                            if (!isMustUpdate && !isUpdate) {
                                try {
                                    Intent i = Launcher_Third_ViewHolder.this.mContext.getPackageManager().getLaunchIntentForPackage(GlobalVariable.MICROCLASS_PKG);
                                    i.addFlags(268435456);
                                    User userInfo = SharepreferenceUtil.getSharepferenceInstance(Launcher_Third_ViewHolder.this.mContext).getUserInfo();
                                    i.putExtra("token", SharepreferenceUtil.getToken());
                                    i.putExtra("userid", userInfo.getId() + "");
                                    i.putExtra(AppContants.KEY_GRADE_NAME, GradeUtil.getGradeName(userInfo.getGradecode()) + "");
                                    i.putExtra(AppContants.KEY_GRADE_CODE, userInfo.getGradecode() + "");
                                    Launcher_Third_ViewHolder.this.mContext.startActivity(i);
                                } catch (Throwable th) {
                                }
                            }
                        }
                    }).checkUpdate(GlobalVariable.MICROCLASS_PKG, new PackageUtils(this.mContext).getVersionCode(GlobalVariable.MICROCLASS_PKG));
                    return;
                } else {
                    Toast.makeText(this.mContext, "请检查是否安装了名师微课堂", 0).show();
                    return;
                }
            case R.id.iv_search_by_photo:
                StatsHelper.toolClickedWZY();
                if (!GlobalVariable.isLogin()) {
                    DialogUtil.showDialogToLogin(this.mContext);
                    return;
                } else if (isAvilible(this.mContext, GlobalVariable.SEARCHBYPHOTO_PKG)) {
                    new UpdateUtil(this.mContext, new Listener_Update() {
                        public void isNeedUpdate(boolean isUpdate, boolean isMustUpdate) {
                            if (!isMustUpdate && !isUpdate) {
                                try {
                                    Intent intent = new Intent();
                                    intent.addFlags(268435456);
                                    intent.putExtra("token", Launcher_Third_ViewHolder.this.userInfo.getToken());
                                    intent.setComponent(new ComponentName(GlobalVariable.SEARCHBYPHOTO_PKG, GlobalVariable.SEARCHBYPHOTO_ENTER_ACTIVITY));
                                    Launcher_Third_ViewHolder.this.mContext.startActivity(intent);
                                } catch (Throwable th) {
                                }
                            }
                        }
                    }).checkUpdate(GlobalVariable.SEARCHBYPHOTO_PKG, new PackageUtils(this.mContext).getVersionCode(GlobalVariable.SEARCHBYPHOTO_PKG));
                    return;
                } else {
                    Toast.makeText(this.mContext, "请检查是否安装了拍照搜题应用", 0).show();
                    return;
                }
            case R.id.iv_translate:
                if (!isAvilible(this.mContext, GlobalVariable.PACKAGE_ENGLISGTOCHINENSE)) {
                    Toast.makeText(this.mContext, "请检查是否安装了中英互译应用", 0).show();
                    return;
                } else if (!ForbiddenUtil.isForbiddenOpen((Activity) this.mContext, GlobalVariable.PACKAGE_ENGLISGTOCHINENSE)) {
                    new UpdateUtil(this.mContext, new Listener_Update() {
                        public void isNeedUpdate(boolean isUpdate, boolean isMustUpdate) {
                            if (!isMustUpdate && !isUpdate) {
                                try {
                                    ComponentName cn = new ComponentName(GlobalVariable.PACKAGE_ENGLISGTOCHINENSE, GlobalVariable.PACKAGE_ENGLISGTOCHINENSE_ACTIIVITY);
                                    Intent intent = new Intent();
                                    intent.setComponent(cn);
                                    Launcher_Third_ViewHolder.this.mContext.startActivity(intent);
                                } catch (Throwable th) {
                                }
                            }
                        }
                    }).checkUpdate(GlobalVariable.PACKAGE_ENGLISGTOCHINENSE, new PackageUtils(this.mContext).getVersionCode(GlobalVariable.PACKAGE_ENGLISGTOCHINENSE));
                    return;
                } else {
                    return;
                }
            case R.id.iv_learning_english:
                if (NoDoubleClickUtils.isDoubleClick()) {
                    return;
                }
                if (!GlobalVariable.isLogin()) {
                    DialogUtil.showDialogToLogin(this.mContext);
                    return;
                } else if (!isAvilible(this.mContext, "com.iflytek.sceneenglish")) {
                    Toast.makeText(this.mContext, "请检查是否安装了情景英语应用", 0).show();
                    return;
                } else if (!ForbiddenUtil.isForbiddenOpen((Activity) this.mContext, "com.iflytek.sceneenglish")) {
                    new UpdateUtil(this.mContext, new Listener_Update() {
                        public void isNeedUpdate(boolean isUpdate, boolean isMustUpdate) {
                            if (!isMustUpdate && !isUpdate) {
                                try {
                                    Intent i = new Intent();
                                    i.putExtra("token", "Bearer " + SharepreferenceUtil.getToken());
                                    i.setComponent(new ComponentName("com.iflytek.sceneenglish", "com.iflytek.sceneenglish.ui.activity.MainActivity"));
                                    i.addFlags(268435456);
                                    Launcher_Third_ViewHolder.this.mContext.startActivity(i);
                                } catch (Throwable th) {
                                }
                            }
                        }
                    }).checkUpdate("com.iflytek.sceneenglish", new PackageUtils(this.mContext).getVersionCode("com.iflytek.sceneenglish"));
                    return;
                } else {
                    return;
                }
            case R.id.img22:
                if (!GlobalVariable.isLogin()) {
                    DialogUtil.showDialogToLogin(this.mContext);
                    return;
                } else if (isAvilible(this.mContext, GlobalVariable.RECOMMEND_PREPAREFORSCHOOL_PKG)) {
                    new UpdateUtil(this.mContext, new Listener_Update() {
                        public void isNeedUpdate(boolean isUpdate, boolean isMustUpdate) {
                            if (!isMustUpdate && !isUpdate) {
                                try {
                                    Intent intent = new Intent();
                                    intent.setComponent(new ComponentName(GlobalVariable.RECOMMEND_PREPAREFORSCHOOL_PKG, GlobalVariable.RECOMMEND_PREPAREFORSCHOOL_MAIN));
                                    Launcher_Third_ViewHolder.this.mContext.startActivity(intent);
                                } catch (Throwable th) {
                                }
                            }
                        }
                    }).checkUpdate(GlobalVariable.RECOMMEND_PREPAREFORSCHOOL_PKG, new PackageUtils(this.mContext).getVersionCode(GlobalVariable.RECOMMEND_PREPAREFORSCHOOL_PKG));
                    return;
                } else {
                    Toast.makeText(this.mContext, "请检查是否安装了备战中高考", 0).show();
                    return;
                }
            default:
                return;
        }
    }

    public String getStatPageName() {
        return "Fragment_WeiKeTang";
    }
}