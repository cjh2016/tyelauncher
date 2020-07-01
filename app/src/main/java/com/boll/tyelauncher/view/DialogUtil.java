package com.boll.tyelauncher.view;

package com.toycloud.launcher.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.toycloud.launcher.R;
import com.toycloud.launcher.myinterface.Listener_Update;
import com.toycloud.launcher.ui.login.LoginActivity;

public class DialogUtil {
    public static AlertDialog alertDialog;
    public static AlertDialog.Builder normalDialog;

    public static void showDialogToLogin(final Context context) {
        AlertDialog.Builder normalDialog2 = new AlertDialog.Builder(context);
        normalDialog2.setTitle("登录信息提醒");
        normalDialog2.setMessage(" 您当前未登录，是否前往登录");
        normalDialog2.setNegativeButton("不了", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        normalDialog2.setPositiveButton("好的", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(context, LoginActivity.class));
            }
        });
        normalDialog2.create().show();
    }

    public static void showDialogToLogin_System(final Context context) {
        AlertDialog.Builder normalDialog2 = new AlertDialog.Builder(context);
        normalDialog2.setTitle("登录信息提醒");
        normalDialog2.setMessage(" 您当前未登录，是否前往登录");
        normalDialog2.setNegativeButton("不了", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        normalDialog2.setPositiveButton("好的", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(context, LoginActivity.class));
            }
        });
        AlertDialog alertDialog2 = normalDialog2.create();
        alertDialog2.getWindow().setType(2003);
        alertDialog2.show();
    }

    public static void showDialogToUpdate_Not_Must(String message, Context context, final Listener_Update listener_update) {
        String mMessage;
        if (TextUtils.isEmpty(message)) {
            mMessage = "是否更新";
        } else {
            mMessage = message;
        }
        normalDialog = new AlertDialog.Builder(context);
        normalDialog.setTitle("发现新版本了");
        normalDialog.setMessage(mMessage);
        normalDialog.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                listener_update.isNeedUpdate(false, false);
            }
        });
        normalDialog.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                listener_update.isNeedUpdate(true, false);
            }
        });
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            alertDialog = null;
        }
        alertDialog = normalDialog.create();
        alertDialog.getWindow().setType(2003);
        alertDialog.show();
    }

    public static void showUpdateDialog(boolean showDialog, Context context, String appName, String appVersion, String appSize, String updateContent, boolean forceUpdate, Listener_Update listener_update) {
        if (showDialog || forceUpdate) {
            normalDialog = new AlertDialog.Builder(context);
            alertDialog = normalDialog.create();
            View dialogView = View.inflate(context, R.layout.layout_app_update_alert_dialog, (ViewGroup) null);
            dialogView.setBackgroundColor(Color.parseColor("#00ffffff"));
            final boolean z = forceUpdate;
            final Listener_Update listener_Update = listener_update;
            ((ImageButton) dialogView.findViewById(R.id.close_image_button)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DialogUtil.alertDialog.dismiss();
                    if (z) {
                        listener_Update.isNeedUpdate(false, true);
                    }
                }
            });
            ((TextView) dialogView.findViewById(R.id.app_name_text_view)).setText("应用：" + appName);
            ((TextView) dialogView.findViewById(R.id.app_version_text_view)).setText("版本：" + appVersion);
            ((TextView) dialogView.findViewById(R.id.app_size_text_view)).setText("大小：" + appSize);
            ((TextView) dialogView.findViewById(R.id.app_update_content_text_view)).setText("更新内容\n" + updateContent);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            TextView updateNowTextView = (TextView) dialogView.findViewById(R.id.update_now_text_view);
            View lineView = dialogView.findViewById(R.id.horizontal_line_view);
            TextView nonUpdateTextView = (TextView) dialogView.findViewById(R.id.non_update_text_view);
            TextView normalUpdateNowTextView = (TextView) dialogView.findViewById(R.id.normal_update_now_text_view);
            if (forceUpdate) {
                lineView.setVisibility(4);
                nonUpdateTextView.setVisibility(4);
                normalUpdateNowTextView.setVisibility(4);
                updateNowTextView.setVisibility(0);
                final Listener_Update listener_Update2 = listener_update;
                updateNowTextView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        listener_Update2.isNeedUpdate(true, true);
                        DialogUtil.alertDialog.dismiss();
                    }
                });
            } else {
                lineView.setVisibility(0);
                nonUpdateTextView.setVisibility(0);
                normalUpdateNowTextView.setVisibility(0);
                updateNowTextView.setVisibility(4);
                final Listener_Update listener_Update3 = listener_update;
                nonUpdateTextView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        listener_Update3.isNeedUpdate(false, false);
                        DialogUtil.alertDialog.dismiss();
                    }
                });
                final Listener_Update listener_Update4 = listener_update;
                normalUpdateNowTextView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        listener_Update4.isNeedUpdate(true, false);
                        DialogUtil.alertDialog.dismiss();
                    }
                });
            }
            if (alertDialog != null && alertDialog.isShowing()) {
                alertDialog.dismiss();
                alertDialog = null;
                alertDialog = normalDialog.create();
            }
            alertDialog.setView(dialogView);
            alertDialog.show();
            return;
        }
        listener_update.isNeedUpdate(false, false);
    }

    public static void showDialogToUpdate_Must(Context context, final Listener_Update listener_update) {
        normalDialog = new AlertDialog.Builder(context);
        normalDialog.setTitle("发现新版本了");
        normalDialog.setMessage("不更新将不能正常使用哦");
        normalDialog.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                listener_update.isNeedUpdate(false, true);
            }
        });
        normalDialog.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                listener_update.isNeedUpdate(true, true);
            }
        });
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            alertDialog = null;
        }
        alertDialog = normalDialog.create();
        alertDialog.getWindow().setType(2003);
        alertDialog.show();
    }

    private void showForceUpdateDialog(Context context) {
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        View dialogView = View.inflate(context, R.layout.layout_app_update_alert_dialog, (ViewGroup) null);
        dialogView.setBackgroundColor(Color.parseColor("#00ffffff"));
        ((ImageButton) dialogView.findViewById(R.id.close_image_button)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setView(dialogView);
        dialog.show();
    }

    public static MyDialog showDilog(Context context, boolean isShowNegative, String content, String positive_str, String negative_str, DialogInterface.OnClickListener listener_positive, DialogInterface.OnClickListener listener_negative) {
        MyDialog myDialog = new MyDialog(context);
        myDialog.setContent(content);
        myDialog.setPositiveButton(positive_str, listener_positive);
        if (isShowNegative) {
            myDialog.setNegativeVisable(isShowNegative);
            myDialog.setNegativeButton(negative_str, listener_negative);
        } else {
            myDialog.setNegativeVisable(isShowNegative);
        }
        myDialog.show();
        return myDialog;
    }

    public static MyDialog showDilogToRegs(Context context, DialogInterface.OnClickListener listener_positive, DialogInterface.OnClickListener listener_other) {
        MyDialog myDialog = new MyDialog(context);
        myDialog.setContent("账号不存在");
        myDialog.setPositiveButton("返回", listener_positive);
        myDialog.setOtherButton("注册新账号", listener_other);
        myDialog.showOtherButton();
        myDialog.show();
        return myDialog;
    }

    public static void showSNDialog(Context context, String Message) {
        AlertDialog.Builder normalDialog2 = new AlertDialog.Builder(context);
        normalDialog2.setTitle("SN号如下：");
        normalDialog2.setMessage(Message);
        normalDialog2.setCancelable(false);
        normalDialog2.setNegativeButton("知道了", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        normalDialog2.create().show();
    }

    public static MyDialog_NewStly showDilogToCancel(Context context, DialogInterface.OnClickListener listener_positive, DialogInterface.OnClickListener listener_other) {
        MyDialog_NewStly myDialog = new MyDialog_NewStly(context);
        myDialog.setContent("完善信息后就可以顺畅使用了\n请继续填写哦");
        myDialog.setPositiveButton("继续", listener_positive);
        myDialog.setNegativeButton("放弃", listener_other);
        myDialog.show();
        return myDialog;
    }

    public static MyDialog_NewStly showDilogToRegs_NewStyle(Context context, DialogInterface.OnClickListener listener_positive, DialogInterface.OnClickListener listener_other) {
        MyDialog_NewStly myDialog = new MyDialog_NewStly(context);
        myDialog.setContent("账号不存在");
        myDialog.setNegativeButton("返回", listener_positive);
        myDialog.setPositiveButton("注册新账号", listener_other);
        myDialog.show();
        return myDialog;
    }

    public static MyDialog_AddSchool showDilogToRegs_AddSchool(Context context, DialogInterface.OnClickListener listener_positive, DialogInterface.OnClickListener listener_other) {
        MyDialog_AddSchool myDialog = new MyDialog_AddSchool(context, R.style.dialog_download);
        myDialog.setNegativeButton("取消", listener_positive);
        myDialog.setPositiveButton("确定", listener_other);
        myDialog.show();
        return myDialog;
    }

    public static class MyDialogBuilder {
        private String hintContent;
        private DialogInterface.OnClickListener otherListener;
        private String otherTxt;
        private DialogInterface.OnClickListener positiveListener;
        private String positiveTxt;

        public MyDialogBuilder hintContent(String hintContent2) {
            this.hintContent = hintContent2;
            return this;
        }

        public MyDialogBuilder positiveTxt(String positiveTxt2) {
            this.positiveTxt = positiveTxt2;
            return this;
        }

        public MyDialogBuilder otherTxt(String otherTxt2) {
            this.otherTxt = otherTxt2;
            return this;
        }

        public MyDialogBuilder positiveListener(DialogInterface.OnClickListener positiveListener2) {
            this.positiveListener = positiveListener2;
            return this;
        }

        public MyDialogBuilder otherListener(DialogInterface.OnClickListener otherListener2) {
            this.otherListener = otherListener2;
            return this;
        }

        public MyDialog_NewStly build(Context context) {
            MyDialog_NewStly myDialog = new MyDialog_NewStly(context);
            myDialog.setContent(this.hintContent);
            myDialog.setPositiveButton(this.positiveTxt, this.positiveListener);
            myDialog.setNegativeButton(this.otherTxt, this.otherListener);
            return myDialog;
        }
    }
}
