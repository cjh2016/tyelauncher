package com.boll.tyelauncher.biz.evalhint.activity;

package com.toycloud.launcher.biz.evalhint.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.View;
import com.google.gson.Gson;
import com.iflytek.easytrans.core.utils.common.ValueUtils;
import com.toycloud.launcher.R;
import com.toycloud.launcher.api.model.User;
import com.toycloud.launcher.biz.evalhint.entity.CityGradeInfo;
import com.toycloud.launcher.biz.evalhint.view.HighlightTextView;
import com.toycloud.launcher.util.SharepreferenceUtil;
import framework.hz.salmon.base.BaseFragmentActivity;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EvalHintActivity extends BaseFragmentActivity {
    private static final String EXTRA_EVAL_HINT_CONFIG = "extra_eval_hint_config";
    private static final String EXTRA_REGION_NAME = "extra_region_name";
    private static final String EXTRA_USER = "extra_user";

    public static void start(Context context, User user, String regionName, String evalHintConfig) {
        if (context != null && user != null && !TextUtils.isEmpty(evalHintConfig)) {
            Intent intent = new Intent(context, EvalHintActivity.class);
            intent.putExtra(EXTRA_USER, user);
            intent.putExtra(EXTRA_REGION_NAME, regionName);
            intent.putExtra(EXTRA_EVAL_HINT_CONFIG, evalHintConfig);
            intent.addFlags(268435456);
            context.startActivity(intent);
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        String testName;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eval_hint);
        User user = (User) getIntent().getSerializableExtra(EXTRA_USER);
        String evalHintConfig = getIntent().getStringExtra(EXTRA_EVAL_HINT_CONFIG);
        String regionName = getIntent().getStringExtra(EXTRA_REGION_NAME);
        HighlightTextView evalHintTitle = (HighlightTextView) findViewById(R.id.eval_hint_title);
        findViewById(R.id.ok_btn).setOnClickListener(this);
        Map<String, Set<Integer>> cityGradeMap = new HashMap<>();
        CityGradeInfo[] cityGradeInfos = (CityGradeInfo[]) new Gson().fromJson(evalHintConfig, CityGradeInfo[].class);
        if (cityGradeInfos != null) {
            int length = cityGradeInfos.length;
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 >= length) {
                    break;
                }
                CityGradeInfo cityGradeInfo = cityGradeInfos[i2];
                String areaCode = cityGradeInfo.getArea_code();
                String gradeRegion = cityGradeInfo.getGrade_region();
                if (!TextUtils.isEmpty(areaCode) && !TextUtils.isEmpty(gradeRegion)) {
                    String[] gradeGroups = gradeRegion.split(",");
                    int length2 = gradeGroups.length;
                    for (int i3 = 0; i3 < length2; i3++) {
                        String gradeGroup = gradeGroups[i3];
                        if (!TextUtils.isEmpty(gradeGroup)) {
                            String[] grades = gradeGroup.split("-");
                            if (grades.length == 2) {
                                int beginGrade = ValueUtils.parseInt(grades[0], 0);
                                int endGrade = ValueUtils.parseInt(grades[1], 0);
                                if (beginGrade > 0 && beginGrade <= endGrade && endGrade <= 12) {
                                    Set<Integer> gradeSet = cityGradeMap.get(areaCode);
                                    if (gradeSet == null) {
                                        gradeSet = new HashSet<>();
                                        cityGradeMap.put(areaCode, gradeSet);
                                    }
                                    for (int i4 = beginGrade; i4 <= endGrade; i4++) {
                                        gradeSet.add(Integer.valueOf(i4));
                                    }
                                }
                            }
                        }
                    }
                }
                i = i2 + 1;
            }
        }
        Set<Integer> gradeSet2 = cityGradeMap.get(user.getAreacode());
        int gradeCode = ValueUtils.parseInt(user.getGradecode(), 0);
        if (gradeSet2 == null || (!gradeSet2.contains(Integer.valueOf(gradeCode)) && (gradeCode > 6 || !gradeSet2.contains(7)))) {
            SpannableString spannableString = new SpannableString("与中高考听说考试评测相同的技术");
            spannableString.setSpan(new StyleSpan(1), 0, 15, 33);
            evalHintTitle.setText(spannableString);
            return;
        }
        if (TextUtils.isEmpty(regionName)) {
            regionName = SharepreferenceUtil.getSharepferenceInstance(this).getRegionName();
        }
        if (TextUtils.isEmpty(regionName)) {
            regionName = "当前省市";
        }
        if (gradeCode >= 10) {
            testName = "高考";
        } else {
            testName = "中考";
        }
        SpannableString spannableString2 = new SpannableString(regionName + testName + "听说考试评测\n已使用科大讯飞学习机同源技术");
        spannableString2.setSpan(new StyleSpan(1), 0, (regionName + testName).length(), 33);
        evalHintTitle.setText(spannableString2);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok_btn:
                finish();
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void exitAnim() {
    }
}
