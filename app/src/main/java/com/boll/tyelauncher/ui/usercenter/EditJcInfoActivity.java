package com.boll.tyelauncher.ui.usercenter;

package com.toycloud.launcher.ui.usercenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iflytek.utils.LogAgentHelper;
import com.toycloud.launcher.R;
import com.toycloud.launcher.adapter.SubjectListAdapter;
import com.toycloud.launcher.api.model.User;
import com.toycloud.launcher.api.response.GetPublisherResponse;
import com.toycloud.launcher.api.response.SetSubjectPublisherResponse;
import com.toycloud.launcher.api.service.LauncherHttpHelper;
import com.toycloud.launcher.api.service.bean.SetSubjectPublisherRequest;
import com.toycloud.launcher.common.GlobalVariable;
import com.toycloud.launcher.model.BookInfo;
import com.toycloud.launcher.ui.common.BindWXActivity;
import com.toycloud.launcher.ui.listener.GlobalUserInfoListener;
import com.toycloud.launcher.util.SharepreferenceUtil;
import com.toycloud.launcher.view.CoursePickerPopWin;
import framework.hz.salmon.base.BaseFragmentActivity;
import framework.hz.salmon.retrofit.BaseSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EditJcInfoActivity extends BaseFragmentActivity {
    CoursePickerPopWin chinese = null;
    /* access modifiers changed from: private */
    public List<BookInfo.DataBean> data = new ArrayList();
    @BindView(2131689732)
    RecyclerView jcList;
    /* access modifiers changed from: private */
    public SubjectListAdapter mAdapter;
    @BindView(2131689705)
    Button save;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_jc_info);
        ButterKnife.bind((Activity) this);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(201326592);
            window.getDecorView().setSystemUiVisibility(1792);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
            window.setNavigationBarColor(0);
        }
        this.mAdapter = new SubjectListAdapter(this, this.data);
        this.mAdapter.openLoadAnimation(1);
        this.mAdapter.isFirstOnly(true);
        this.mAdapter.setNotDoAnimationCount(4);
        this.jcList.setHasFixedSize(true);
        this.jcList.setLayoutManager(new GridLayoutManager(getApplicationContext(), 6));
        this.jcList.setAdapter(this.mAdapter);
        this.mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                EditJcInfoActivity.this.getPublisher_BookInfo(GlobalVariable.LOGIN_USER.getGradecode(), (BookInfo.DataBean) EditJcInfoActivity.this.data.get(position), position);
            }
        });
        getSubject();
    }

    public boolean handleMessage(Message message) {
        return false;
    }

    public void onClick(View view) {
    }

    /* access modifiers changed from: private */
    public void getPublisher_BookInfo(String grade, final BookInfo.DataBean subject, final int index) {
        LauncherHttpHelper.getLauncherService().getPublisher(grade, subject.getSubject().getSubjectcode()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<GetPublisherResponse>(getApplicationContext()) {
            public void onCompleted() {
                super.onCompleted();
                EditJcInfoActivity.this.dismissProgressDialog();
            }

            public void onError(Throwable e) {
                EditJcInfoActivity.this.dismissProgressDialog();
                super.onError(e);
            }

            public void onNext(GetPublisherResponse response) {
                super.onNext(response);
                EditJcInfoActivity.this.dismissProgressDialog();
                if (EditJcInfoActivity.this.chinese != null && EditJcInfoActivity.this.chinese.isShowing()) {
                    EditJcInfoActivity.this.chinese.dismissPopWin();
                }
                EditJcInfoActivity.this.chinese = new CoursePickerPopWin.Builder(EditJcInfoActivity.this, new CoursePickerPopWin.OnPublicsherPickListener() {
                    public void onPicked(GetPublisherResponse.DataBean code) {
                        ((BookInfo.DataBean) EditJcInfoActivity.this.data.get(index)).getPublisher().setPublishercode(code.getPublishercode());
                        ((BookInfo.DataBean) EditJcInfoActivity.this.data.get(index)).getPublisher().setPublishername(code.getPublishername());
                        EditJcInfoActivity.this.mAdapter.notifyDataSetChanged();
                    }
                }).setTitle(subject.getSubject().getSubjectname()).setSelected(subject.getPublisher().getPublishercode()).setData(response.getData()).build();
                EditJcInfoActivity.this.chinese.showPopWin(EditJcInfoActivity.this);
            }
        });
    }

    @OnClick({2131689705, 2131689731})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.save:
                submit();
                return;
            case R.id.but_back:
                Intent intent = new Intent(this, BindWXActivity.class);
                intent.putExtra(BindWXActivity.EXTRA_BIND_WX_ACT_START_FROM, 1);
                startActivity(intent);
                finish();
                return;
            default:
                return;
        }
    }

    private void submit() {
        showProgressDialog();
        StringBuffer codeBuffer = new StringBuffer();
        StringBuffer subjectBuffer = new StringBuffer();
        StringBuffer publishername = new StringBuffer();
        for (int i = 0; i < this.data.size(); i++) {
            if (!TextUtils.isEmpty(this.data.get(i).getPublisher().getPublishercode())) {
                if (codeBuffer.toString().length() == 0) {
                    codeBuffer.append(this.data.get(i).getPublisher().getPublishercode());
                    subjectBuffer.append(this.data.get(i).getSubject().getSubjectcode());
                    publishername.append(this.data.get(i).getPublisher().getPublishername());
                } else {
                    codeBuffer.append("," + this.data.get(i).getPublisher().getPublishercode());
                    subjectBuffer.append("," + this.data.get(i).getSubject().getSubjectcode());
                    publishername.append("," + this.data.get(i).getPublisher().getPublishername());
                }
            }
        }
        SetSubjectPublisherRequest request = new SetSubjectPublisherRequest();
        request.publishername = publishername.toString();
        request.grade = SharepreferenceUtil.getSharepferenceInstance(this).getUserInfo().getGradecode();
        request.publisher = codeBuffer.toString();
        request.subject = subjectBuffer.toString();
        LauncherHttpHelper.getLauncherService().setSubjectPublisher(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<SetSubjectPublisherResponse>(getApplicationContext()) {
            public void onCompleted() {
                EditJcInfoActivity.this.dismissProgressDialog();
                super.onCompleted();
            }

            public void onError(Throwable e) {
                EditJcInfoActivity.this.dismissProgressDialog();
                super.onError(e);
            }

            public void onNext(SetSubjectPublisherResponse response) {
                super.onNext(response);
                GlobalVariable.LOGIN_USER = response.getData();
                GlobalVariable.setLogin(true, GlobalVariable.LOGIN_USER);
                String token = SharepreferenceUtil.getToken();
                Gson gson = new GsonBuilder().create();
                User user = (User) gson.fromJson(gson.toJson((Object) response.getData(), (Type) User.class), User.class);
                user.setToken(token);
                SharepreferenceUtil.getSharepferenceInstance(EditJcInfoActivity.this).setUserInfo(user);
                GlobalUserInfoListener.getInstance().onUserInfoChanged(true, user);
                Intent intent = new Intent(EditJcInfoActivity.this, BindWXActivity.class);
                intent.putExtra(BindWXActivity.EXTRA_BIND_WX_ACT_START_FROM, 1);
                EditJcInfoActivity.this.startActivity(intent);
                EditJcInfoActivity.this.setResult(-1);
                Toast.makeText(this.mContext, "填写成功", 0).show();
                EditJcInfoActivity.this.finish();
            }
        });
    }

    private void getSubject() {
        if (!TextUtils.isEmpty(GlobalVariable.LOGIN_USER.getGradecode())) {
            LauncherHttpHelper.getLauncherService().getBookInfo(GlobalVariable.LOGIN_USER.getGradecode()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<BookInfo>(getApplicationContext()) {
                public void onCompleted() {
                    super.onCompleted();
                }

                public void onError(Throwable e) {
                    EditJcInfoActivity.this.dismissProgressDialog();
                    super.onError(e);
                }

                public void onNext(BookInfo response) {
                    super.onNext(response);
                    EditJcInfoActivity.this.data.clear();
                    EditJcInfoActivity.this.data.addAll(response.getData());
                    EditJcInfoActivity.this.mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        LogAgentHelper.onActive();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        finish();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}