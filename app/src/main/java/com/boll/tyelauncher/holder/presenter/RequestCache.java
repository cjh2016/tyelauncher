package com.boll.tyelauncher.holder.presenter;

package com.toycloud.launcher.holder.presenter;

import android.content.Context;
import android.text.TextUtils;
import com.toycloud.launcher.api.model.User;
import com.toycloud.launcher.api.response.MappingInfoResponse;
import com.toycloud.launcher.common.GlobalVariable;
import com.toycloud.launcher.holder.model.MMKVModel;
import com.toycloud.launcher.util.GsonUtils;
import com.toycloud.launcher.util.NumberConvertUtil;
import com.toycloud.launcher.util.SharepreferenceUtil;
import java.util.HashMap;
import java.util.Map;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class RequestCache {
    public static final String KEY_MAP_REQUEST_CHEMICAL = "key.req.map.che.cur";
    public static final String KEY_MAP_REQUEST_CHEMICAL_NEW = "key.req.map.che.new";
    public static final String KEY_MAP_REQUEST_MATH = "key.req.map.math.cur";
    public static final String KEY_MAP_REQUEST_MATH_NEW = "key.req.map.math.new";
    public static final String KEY_MAP_REQUEST_PHYSICAL = "key.req.map.phy.cur";
    public static final String KEY_MAP_REQUEST_PHYSICAL_NEW = "key.req.map.phy.new";
    public static final String KEY_MAP_REQUEST_SCIENCE = "key.req.map.science.cur";
    public static final String KEY_MAP_REQUEST_SCIENCE_NEW = "key.req.map.science.new";
    public static final String KEY_MAP_RESPONSE_CHEMICAL = "key.rep.map.che";
    public static final String KEY_MAP_RESPONSE_MATH = "key.rep.map.math";
    public static final String KEY_MAP_RESPONSE_PHYSICAL = "key.rep.map.phy";
    public static final String KEY_MAP_RESPONSE_SCIENCE = "key.rep.map.science";
    public static final int SUBJECT_CHEMICAL = 3;
    public static final int SUBJECT_MATH = 1;
    public static final int SUBJECT_PHYSICAL = 2;
    public static final int SUBJECT_SCIENCE = 4;
    private final Context mContext;
    private final MMKVModel mModel;
    private final Map<String, CatalogItem> mRequestMap = new HashMap();

    private static final String buildKey(Context context, String key) {
        int gradeInt;
        String school;
        User user = SharepreferenceUtil.getSharepferenceInstance(context).getUserInfo();
        if (user == null) {
            return null;
        }
        String userId = user.getUserid();
        String grade = user.getGradecode();
        if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(grade) || (gradeInt = NumberConvertUtil.parseInteger(grade, -1)) < 0) {
            return null;
        }
        if (gradeInt >= 7) {
            school = "middle_school";
        } else {
            school = "primary_school";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(key).append(":for:").append(userId).append(":").append(school);
        return sb.toString();
    }

    public RequestCache(Context context) {
        this.mContext = context.getApplicationContext();
        this.mModel = new MMKVModel(this.mContext);
    }

    public void saveMapRequestParams(int subject, CatalogItem params, boolean isCurrent) {
        String key;
        if (params != null && subject >= 1 && subject <= 4) {
            if (isCurrent) {
                if (subject == 1) {
                    key = KEY_MAP_REQUEST_MATH;
                } else if (subject == 2) {
                    key = KEY_MAP_REQUEST_PHYSICAL;
                } else if (subject == 3) {
                    key = KEY_MAP_REQUEST_CHEMICAL;
                } else {
                    key = KEY_MAP_REQUEST_SCIENCE;
                }
            } else if (subject == 1) {
                key = KEY_MAP_REQUEST_MATH_NEW;
            } else if (subject == 2) {
                key = KEY_MAP_REQUEST_PHYSICAL_NEW;
            } else if (subject == 3) {
                key = KEY_MAP_REQUEST_CHEMICAL_NEW;
            } else {
                key = KEY_MAP_REQUEST_SCIENCE_NEW;
            }
            String keyForUser = buildKey(this.mContext, key);
            if (keyForUser != null) {
                this.mModel.putString(keyForUser, GsonUtils.toJson(params));
            }
        }
    }

    public CatalogItem getMapRequestParams(int subject, boolean isCurrent) {
        String key;
        if (subject < 1 || subject > 4) {
            return null;
        }
        if (isCurrent) {
            if (subject == 1) {
                key = KEY_MAP_REQUEST_MATH;
            } else if (subject == 2) {
                key = KEY_MAP_REQUEST_PHYSICAL;
            } else if (subject == 3) {
                key = KEY_MAP_REQUEST_CHEMICAL;
            } else {
                key = KEY_MAP_REQUEST_SCIENCE;
            }
        } else if (subject == 1) {
            key = KEY_MAP_REQUEST_MATH_NEW;
        } else if (subject == 2) {
            key = KEY_MAP_REQUEST_PHYSICAL_NEW;
        } else if (subject == 3) {
            key = KEY_MAP_REQUEST_CHEMICAL_NEW;
        } else {
            key = KEY_MAP_REQUEST_SCIENCE_NEW;
        }
        String keyForUser = buildKey(this.mContext, key);
        if (keyForUser == null) {
            return null;
        }
        String json = this.mModel.getString(keyForUser);
        if (!TextUtils.isEmpty(json)) {
            return (CatalogItem) GsonUtils.changeJsonToBean(json, CatalogItem.class);
        }
        return null;
    }

    public void saveMapResp(int subject, MappingInfoResponse response) {
        String key;
        if (response != null && subject >= 1 && subject <= 4) {
            if (subject == 1) {
                key = KEY_MAP_RESPONSE_MATH;
            } else if (subject == 2) {
                key = KEY_MAP_RESPONSE_PHYSICAL;
            } else if (subject == 3) {
                key = KEY_MAP_RESPONSE_CHEMICAL;
            } else {
                key = KEY_MAP_RESPONSE_SCIENCE;
            }
            String keyForUser = buildKey(this.mContext, key);
            if (keyForUser != null) {
                this.mModel.putString(keyForUser, GsonUtils.toJson(response));
            }
        }
    }

    public void saveCurMapAsync(final int subject, final CatalogItem catalog, final MappingInfoResponse response) {
        if (catalog != null && response != null) {
            Observable.create(new Observable.OnSubscribe<Object>() {
                public void call(Subscriber<? super Object> subscriber) {
                    RequestCache.this.saveMapResp(subject, response);
                    RequestCache.this.saveMapRequestParams(subject, catalog, true);
                    subscriber.onNext(GlobalVariable.NULL);
                    subscriber.onCompleted();
                }
            }).subscribeOn(Schedulers.io());
        }
    }

    public MappingInfoResponse getMapResp(int subject) {
        String key;
        if (subject < 1 || subject > 4) {
            return null;
        }
        if (subject == 1) {
            key = KEY_MAP_RESPONSE_MATH;
        } else if (subject == 2) {
            key = KEY_MAP_RESPONSE_PHYSICAL;
        } else if (subject == 3) {
            key = KEY_MAP_RESPONSE_CHEMICAL;
        } else {
            key = KEY_MAP_RESPONSE_SCIENCE;
        }
        String keyForUser = buildKey(this.mContext, key);
        if (keyForUser == null) {
            return null;
        }
        String json = this.mModel.getString(keyForUser);
        if (!TextUtils.isEmpty(json)) {
            return (MappingInfoResponse) GsonUtils.changeJsonToBean(json, MappingInfoResponse.class);
        }
        return null;
    }

    public String moveString(String srcKey, String dstKey, boolean moveIfEmpty) {
        String srcKeyForUser = buildKey(this.mContext, srcKey);
        String srcValue = this.mModel.getString(srcKeyForUser);
        String dstKeyForUser = buildKey(this.mContext, dstKey);
        boolean move = false;
        if (!TextUtils.isEmpty(srcValue)) {
            this.mModel.putString(dstKeyForUser, srcValue);
            move = true;
        } else if (moveIfEmpty) {
            this.mModel.putString(dstKeyForUser, "");
            move = true;
        }
        if (move) {
            this.mModel.remove(srcKeyForUser);
        }
        return srcValue;
    }

    public void removeForKey(String key) {
        String keyForUser = buildKey(this.mContext, key);
        if (keyForUser != null) {
            this.mModel.remove(keyForUser);
        }
    }

    public void clear() {
        String key1 = buildKey(this.mContext, KEY_MAP_REQUEST_MATH);
        String key2 = buildKey(this.mContext, KEY_MAP_REQUEST_PHYSICAL);
        String key3 = buildKey(this.mContext, KEY_MAP_REQUEST_CHEMICAL);
        String key4 = buildKey(this.mContext, KEY_MAP_REQUEST_SCIENCE);
        String key5 = buildKey(this.mContext, KEY_MAP_REQUEST_MATH_NEW);
        String key6 = buildKey(this.mContext, KEY_MAP_REQUEST_PHYSICAL_NEW);
        String key7 = buildKey(this.mContext, KEY_MAP_REQUEST_CHEMICAL_NEW);
        String key8 = buildKey(this.mContext, KEY_MAP_REQUEST_SCIENCE_NEW);
        String key9 = buildKey(this.mContext, KEY_MAP_RESPONSE_MATH);
        String keyA = buildKey(this.mContext, KEY_MAP_RESPONSE_PHYSICAL);
        String keyB = buildKey(this.mContext, KEY_MAP_RESPONSE_CHEMICAL);
        String keyC = buildKey(this.mContext, KEY_MAP_RESPONSE_SCIENCE);
        this.mModel.remove(key1, key2, key3, key4, key5, key6, key7, key8, key9, keyA, keyB, keyC);
    }
}
