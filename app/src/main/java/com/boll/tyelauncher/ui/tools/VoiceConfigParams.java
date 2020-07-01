package com.boll.tyelauncher.ui.tools;


import android.util.Log;

import com.boll.tyelauncher.common.GlobalVariable;
import com.boll.tyelauncher.model.AppContants;
import com.boll.tyelauncher.model.EtsConstant;
import com.boll.tyelauncher.util.GsonUtils;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class VoiceConfigParams {
    private static final String TAG = "VoiceConfigParams";

    public static class Config {
        public String action = "OpenApp";
        public String operation = "TC";
        public Param param;
    }

    public static class Param {
        public List<VListItem> vList = new ArrayList();

        public void addItem(VListItem vList2) {
            this.vList = new ArrayList();
            this.vList.add(vList2);
        }
    }

    public static class VListItem {
        public String action = "";
        public String className = "";
        public String extendJson = "";
        public String extras = "";
        public String name;
        public boolean needCheck = false;
        public String packageName;
        public int proxy;
        public String startType = "activity";
        public int version;

        public VListItem(int version2, int proxy2, String name2, String pkgName, String extendJson2) {
            this.version = version2;
            this.proxy = proxy2;
            this.name = name2;
            this.packageName = pkgName;
            this.extendJson = extendJson2;
        }
    }

    public static String create(int version, int proxy, String name, String pkgName, String extendJson) {
        VListItem item = new VListItem(version, proxy, name, pkgName, extendJson);
        Config config = new Config();
        Param p = new Param();
        p.addItem(item);
        config.param = p;
        return GsonUtils.toJson(config);
    }

    public static void test() throws Exception {
        JSONObject object = new JSONObject();
        object.put("target", GlobalVariable.TAB_CODE_JJQ);
        String result = create(2, 1, "节节清", "com.iflytek.recommend_tsp", object.toString());
        Log.d(TAG, "节节清");
        Log.d(TAG, result);
        JSONObject object2 = new JSONObject();
        object2.put("target", GlobalVariable.TAB_CODE_ZZL);
        String result2 = create(2, 1, "章章练", "com.iflytek.recommend_tsp", object2.toString());
        Log.d(TAG, "章章练");
        Log.d(TAG, result2);
        JSONObject object3 = new JSONObject();
        object3.put("target", GlobalVariable.TAB_CODE_JDC);
        String result3 = create(2, 1, "阶段测", "com.iflytek.recommend_tsp", object3.toString());
        Log.d(TAG, "阶段测");
        Log.d(TAG, result3);
        String result4 = create(2, 1, "思维拓展训练", "fake.com.iflytek.thinking.train", "");
        Log.d(TAG, "思维拓展训练");
        Log.d(TAG, result4);
        JSONObject object4 = new JSONObject();
        object4.put("target", "com.ets100.study.ACTION_SYNC_RW");
        String result5 = create(2, 1, "英语同步练习", "fake.com.ets100.study", object4.toString());
        Log.d(TAG, "英语同步练习");
        Log.d(TAG, result5);
        JSONObject object5 = new JSONObject();
        object5.put("target", "com.ets100.study.ACTION_SIMULATION_LS");
        String result6 = create(2, 1, "英语模拟考试", "fake.com.ets100.study", object5.toString());
        Log.d(TAG, "英语模拟考试");
        Log.d(TAG, result6);
        JSONObject object6 = new JSONObject();
        object6.put("target", EtsConstant.ACTION_READ_COURSE);
        String result7 = create(2, 1, "英语读课文", "fake.com.ets100.study", object6.toString());
        Log.d(TAG, "英语读课文");
        Log.d(TAG, result7);
        JSONObject object7 = new JSONObject();
        object7.put("target", EtsConstant.ACTION_BOOK_RECITE);
        String result8 = create(2, 1, "英语背课文", "fake.com.ets100.study", object7.toString());
        Log.d(TAG, "英语背课文");
        Log.d(TAG, result8);
        JSONObject object8 = new JSONObject();
        object8.put("target", "com.ets100.study.ACTION_COMPOSITION_CHECK");
        String result9 = create(2, 1, "英语作文批改", "fake.com.ets100.study", object8.toString());
        Log.d(TAG, "英语作文批改");
        Log.d(TAG, result9);
        JSONObject object9 = new JSONObject();
        object9.put("target", EtsConstant.ACTION_SYNC_CHALLENGE);
        String result10 = create(2, 1, "同步闯关", "fake.com.ets100.pupil.lite", object9.toString());
        Log.d(TAG, "同步闯关");
        Log.d(TAG, result10);
        String result11 = create(2, 1, "练口语", "fake.com.iflytek.yyt.situationaldialogue", "");
        Log.d(TAG, "练口语");
        Log.d(TAG, result11);
        String result12 = create(2, 1, "英语微课堂", "fake.com.iflytek.englishvideo", "");
        Log.d(TAG, "英语微课堂");
        Log.d(TAG, result12);
        JSONObject object10 = new JSONObject();
        object10.put("target", "com.ets100.study.ACTION_SOUND_MARK_STUDY");
        String result13 = create(2, 1, "国际音标", "fake.com.ets100.study", object10.toString());
        Log.d(TAG, "国际音标");
        Log.d(TAG, result13);
        JSONObject object11 = new JSONObject();
        object11.put("target", "com.ets100.study.ACTION_SOUND_MARK_STUDY");
        String result14 = create(2, 1, "音标练习", "fake.com.ets100.study", object11.toString());
        Log.d(TAG, "音标练习");
        Log.d(TAG, result14);
        String result15 = create(2, 1, "诗词天地", "fake.com.iflytek.poetryworld", "");
        Log.d(TAG, "诗词天地");
        Log.d(TAG, result15);
        String result16 = create(2, 1, "字词听写", "fake.com.iflytek.dictatechinesewords", "");
        Log.d(TAG, "字词听写");
        Log.d(TAG, result16);
        String result17 = create(2, 1, "语文背课文", "fake.com.toycloud.tyrrecite", "");
        Log.d(TAG, "语文背课文");
        Log.d(TAG, result17);
        String result18 = create(2, 1, "汉字笔顺", "fake.com.toycloud.app.chinesecharacters", "");
        Log.d(TAG, "汉字笔顺");
        Log.d(TAG, result18);
        String result19 = create(2, 0, "优秀作文选", AppContants.COMPOSITION, "");
        Log.d(TAG, "优秀作文选");
        Log.d(TAG, result19);
        JSONObject object12 = new JSONObject();
        object12.put("target", "home");
        String result20 = create(2, 1, "个人中心", "fake.com.toycloud.launcher", object12.toString());
        Log.d(TAG, "个人中心");
        Log.d(TAG, result20);
        JSONObject object13 = new JSONObject();
        object13.put("target", "daily_report");
        String result21 = create(2, 1, "学习日报", "fake.com.toycloud.launcher", object13.toString());
        Log.d(TAG, "学习日报");
        Log.d(TAG, result21);
        JSONObject object14 = new JSONObject();
        object14.put("target", "course_report");
        String result22 = create(2, 1, "课程学习报告", "fake.com.toycloud.launcher", object14.toString());
        Log.d(TAG, "课程学习报告");
        Log.d(TAG, result22);
        String result23 = create(2, 1, "近反义词", "fake.com.iflytek.antonym", "");
        Log.d(TAG, "近反义词");
        Log.d(TAG, result23);
        String result24 = create(2, 1, "成语解释", "fake.com.iflytek.idiominterpretation", "");
        Log.d(TAG, "成语解释");
        Log.d(TAG, result24);
        String result25 = create(2, 0, "用户反馈", GlobalVariable.FEEDBACKPACKAGENAME, "");
        Log.d(TAG, "用户反馈");
        Log.d(TAG, result25);
    }
}
