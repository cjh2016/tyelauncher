package com.boll.tyelauncher.receiver;

package com.toycloud.launcher.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.text.TextUtils;
import android.util.Log;
import com.facebook.common.util.UriUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iflytek.framelib.base.Constants;
import com.orhanobut.logger.Logger;
import com.toycloud.launcher.api.model.User;
import com.toycloud.launcher.common.GlobalVariable;
import com.toycloud.launcher.holder.helper.StudyCPHelper;
import com.toycloud.launcher.model.AppContants;
import com.toycloud.launcher.model.VoiceCallModel;
import com.toycloud.launcher.model.launcher.RemoteController;
import com.toycloud.launcher.receiver.voicehelper.BizOpenAppHandler;
import com.toycloud.launcher.receiver.voicehelper.DefaultOpenAppHandler;
import com.toycloud.launcher.util.GradeUtil;
import com.toycloud.launcher.util.SharepreferenceUtil;
import com.toycloud.launcher.util.ToastUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VoiceStartAppReceiver extends BroadcastReceiver {
    public static String ACTION_OPEN_APP = "com.iflytek.action.OPEN_CONTROL";
    private static String TAG = "VoiceStartAppReceiver";

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e(TAG, action + "");
        if (action.equals(ACTION_OPEN_APP)) {
            try {
                JSONObject jsonObject = (JSONObject) new JSONArray(intent.getStringExtra(UriUtil.LOCAL_CONTENT_SCHEME)).get(0);
                Gson gson = new GsonBuilder().create();
                Log.e("content--->", jsonObject.toString());
                VoiceCallModel voiceCallModel = (VoiceCallModel) gson.fromJson(jsonObject.toString(), VoiceCallModel.class);
                if (voiceCallModel != null) {
                    String pkg = voiceCallModel.getPackageName();
                    if (TextUtils.isEmpty(pkg)) {
                        Logger.w(TAG, "packageName为空");
                    } else if (RemoteController.getInstance(context).isForbbiden(pkg)) {
                        Logger.w(TAG, "该应用被禁止");
                    } else if (!BizOpenAppHandler.getInstance().openApp(context, voiceCallModel)) {
                        DefaultOpenAppHandler.openApp(context, voiceCallModel);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void toLoginPacageNanme(Context context, VoiceCallModel voiceCallModel) {
        String packageName;
        Intent intent;
        if (!isAvilible(context, voiceCallModel.getPackageName())) {
            ToastUtils.showShort((CharSequence) "未安装应用");
            return;
        }
        try {
            packageName = voiceCallModel.getPackageName();
            if (packageName.equals("org.codeaurora.gallery") || packageName.equals("com.android.documentsui") || packageName.equals("org.codeaurora.snapcam") || packageName.equals(GlobalVariable.MICROCLASS_PKG)) {
                intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
            } else {
                intent = new Intent();
            }
            intent.setPackage(packageName);
            intent.addFlags(268435456);
            if (!TextUtils.isEmpty(voiceCallModel.getExtras())) {
                JSONObject jsonObject = new JSONObject(voiceCallModel.getExtras());
                List<String> allKey = getAllKey(jsonObject);
                for (int i = 0; i < allKey.size(); i++) {
                    intent.putExtra(allKey.get(i), jsonObject.getInt(allKey.get(i)));
                    Log.e("intent-->", allKey.get(i) + ":" + jsonObject.getInt(allKey.get(i)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Throwable th) {
            return;
        }
        User userInfo = SharepreferenceUtil.getSharepferenceInstance(context).getUserInfo();
        if (userInfo != null) {
            if (packageName.equals("com.iflytek.dictatewords") || packageName.equals(AppContants.RECITE_BOOK) || packageName.equals(GlobalVariable.ENGLISH_LSP_PKG) || packageName.equals("com.iflytek.sceneenglish")) {
                intent.putExtra("token", "Bearer " + SharepreferenceUtil.getToken());
            } else {
                intent.putExtra("token", SharepreferenceUtil.getToken());
            }
            intent.putExtra(Constants.GRADE_CODE, userInfo.getGradecode());
            intent.putExtra("username", userInfo.getUsername());
            intent.putExtra(AppContants.KEY_USER_ID, userInfo.getUserid());
            intent.putExtra(GlobalVariable.KEY_SUBJECT_CODE, "02");
            intent.putExtra(AppContants.KEY_USER_ID, userInfo.getUserid());
            intent.putExtra("userid", userInfo.getId() + "");
            intent.putExtra(StudyCPHelper.COLUMN_NAME_GRADE_CODE, userInfo.getGradecode());
            intent.putExtra(AppContants.KEY_GRADE_NAME, GradeUtil.getGradeName(userInfo.getGradecode()));
            intent.putExtra(AppContants.KEY_GRADE_CODE, userInfo.getGradecode());
            intent.putExtra("extras", voiceCallModel.getExtras());
        }
        context.startActivity(intent);
    }

    public static void toLoginActivity(Context context, VoiceCallModel voiceCallModel) {
        if (!isAvilible(context, voiceCallModel.getPackageName())) {
            ToastUtils.showShort((CharSequence) "未安装应用");
            return;
        }
        Intent intent = new Intent();
        ComponentName cn = new ComponentName(voiceCallModel.getPackageName(), voiceCallModel.getClassName());
        intent.addFlags(268435456);
        if (!TextUtils.isEmpty(voiceCallModel.getExtras())) {
            try {
                JSONObject jsonObject = new JSONObject(voiceCallModel.getExtras());
                List<String> allKey = getAllKey(jsonObject);
                for (int i = 0; i < allKey.size(); i++) {
                    intent.putExtra(allKey.get(i), jsonObject.getInt(allKey.get(i)));
                    Log.e("intent-->", allKey.get(i) + ":" + jsonObject.getInt(allKey.get(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        User userInfo = SharepreferenceUtil.getSharepferenceInstance(context).getUserInfo();
        String packageName = voiceCallModel.getPackageName();
        if (userInfo != null) {
            if (packageName.equals("com.iflytek.dictatewords") || packageName.equals(AppContants.RECITE_BOOK) || packageName.equals(GlobalVariable.ENGLISH_LSP_PKG) || packageName.equals("com.iflytek.sceneenglish")) {
                intent.putExtra("token", "Bearer " + SharepreferenceUtil.getToken());
            } else {
                intent.putExtra("token", SharepreferenceUtil.getToken());
            }
            intent.putExtra(Constants.GRADE_CODE, userInfo.getGradecode());
            intent.putExtra("username", userInfo.getUsername());
            intent.putExtra(AppContants.KEY_USER_ID, userInfo.getUserid());
            intent.putExtra(GlobalVariable.KEY_SUBJECT_CODE, "02");
            intent.putExtra(AppContants.KEY_USER_ID, userInfo.getUserid());
            intent.putExtra("userid", userInfo.getId() + "");
            intent.putExtra(StudyCPHelper.COLUMN_NAME_GRADE_CODE, userInfo.getGradecode());
            intent.putExtra(AppContants.KEY_GRADE_NAME, GradeUtil.getGradeName(userInfo.getGradecode()));
            intent.putExtra(AppContants.KEY_GRADE_CODE, userInfo.getGradecode());
        }
        intent.setComponent(cn);
        try {
            context.startActivity(intent);
        } catch (Throwable th) {
        }
    }

    protected static boolean isAvilible(Context context, String packageName) {
        List<PackageInfo> pinfo = context.getPackageManager().getInstalledPackages(0);
        List<String> pName = new ArrayList<>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                pName.add(pinfo.get(i).packageName);
            }
        }
        return pName.contains(packageName);
    }

    public static List<String> getAllKey(JSONObject jsonObject) {
        List<String> list = new ArrayList<>();
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            list.add(keys.next());
        }
        return list;
    }
}
