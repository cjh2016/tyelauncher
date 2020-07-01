package com.boll.tyelauncher.ui.regist;

package com.toycloud.launcher.ui.regist;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.iflytek.utils.LogAgentHelper;
import com.toycloud.launcher.R;
import com.toycloud.launcher.api.response.GetPublisherResponse;
import com.toycloud.launcher.api.service.LauncherHttpHelper;
import framework.hz.salmon.base.BaseFragmentActivity;
import framework.hz.salmon.retrofit.BaseSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CompleteJcInfoActivity extends BaseFragmentActivity {
    @BindView(2131689705)
    Button save;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_jc_info);
        ButterKnife.bind((Activity) this);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(201326592);
            window.getDecorView().setSystemUiVisibility(1792);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
            window.setNavigationBarColor(0);
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        LogAgentHelper.onActive();
    }

    public boolean handleMessage(Message message) {
        return false;
    }

    public void onClick(View view) {
    }

    @OnClick({2131689705})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.save:
                setResult(-1);
                finish();
                return;
            default:
                return;
        }
    }

    private void getPublisher(String grade, String subject) {
        LauncherHttpHelper.getLauncherService().getPublisher(grade, subject).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<GetPublisherResponse>(getApplicationContext()) {
            public void onCompleted() {
                super.onCompleted();
                CompleteJcInfoActivity.this.dismissProgressDialog();
            }

            public void onError(Throwable e) {
                CompleteJcInfoActivity.this.dismissProgressDialog();
                super.onError(e);
            }

            public void onNext(GetPublisherResponse response) {
                super.onNext(response);
                CompleteJcInfoActivity.this.dismissProgressDialog();
            }
        });
    }
}