package com.boll.tyelauncher.holder.presenter;

package com.toycloud.launcher.holder.presenter;

import android.content.Context;
import com.toycloud.launcher.api.response.MappingInfoResponse;
import com.toycloud.launcher.common.GlobalVariable;
import com.toycloud.launcher.holder.helper.StudyCPHelper;
import com.toycloud.launcher.holder.model.StudySnapshotItem;
import com.toycloud.launcher.util.NumberConvertUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.HashMap;
import java.util.Map;

public class RxCacheLoader {
    public static Observable<Object> loadCache(final Context context, final RequestCache cacheReq, final String userId, final String gradeCode) {
        return Observable.create(new ObservableOnSubscribe<Object>() {
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                String gc = gradeCode;
                int gradeInt = NumberConvertUtil.parseInteger(gradeCode, -1);
                if (gradeInt >= 10 && gradeInt <= 12) {
                    gc = "19";
                }
                RxCacheLoader.doLoadCache(context, e, cacheReq, userId, gc);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /* access modifiers changed from: private */
    public static void doLoadCache(Context context, ObservableEmitter<Object> subscriber, RequestCache cacheReq, String userId, String gradeCode) {
        try {
            Map<String, Object> cache = loadCacheInner(context, cacheReq, userId, gradeCode);
            if (cache == null) {
                subscriber.onNext(GlobalVariable.NULL);
            } else {
                subscriber.onNext(cache);
            }
        } catch (Throwable exp) {
            subscriber.onError(exp);
        }
    }

    private static Map<String, Object> loadCacheInner(Context context, RequestCache cacheReq, String userId, String gradeCode) {
        CatalogItem curMathReq;
        MappingInfoResponse mathMap;
        Object obj;
        CatalogItem curPhysicalReq;
        MappingInfoResponse physicalMap;
        Object obj2;
        CatalogItem curChemicalReq;
        Object newChemicalReq;
        MappingInfoResponse chemicalMap;
        Object newPhysicalReq;
        Object newMathReq;
        Map<String, StudySnapshotItem> items = StudyCPHelper.query(context, userId);
        StudySnapshotItem providerItem = StudySnapshotItem.findBySubject(items, "02");
        if (providerItem == null || providerItem.mCatalogItem == null) {
            curMathReq = cacheReq.getMapRequestParams(1, true);
            Object newMathReq2 = cacheReq.getMapRequestParams(1, false);
            mathMap = cacheReq.getMapResp(1);
            obj = newMathReq2;
        } else if (providerItem.mMappingInfo != null) {
            curMathReq = providerItem.mCatalogItem;
            mathMap = providerItem.mMappingInfo;
            cacheReq.saveMapRequestParams(1, providerItem.mCatalogItem, true);
            cacheReq.saveMapResp(1, providerItem.mMappingInfo);
            cacheReq.removeForKey(RequestCache.KEY_MAP_REQUEST_MATH_NEW);
            obj = null;
        } else {
            CatalogItem mathReq = cacheReq.getMapRequestParams(1, true);
            mathMap = cacheReq.getMapResp(1);
            if (mathReq == null || mathMap == null || CatalogItem.isEquals(providerItem.mCatalogItem, mathReq)) {
                curMathReq = providerItem.mCatalogItem;
                newMathReq = null;
                cacheReq.saveMapRequestParams(1, providerItem.mCatalogItem, true);
                cacheReq.removeForKey(RequestCache.KEY_MAP_RESPONSE_MATH);
                cacheReq.removeForKey(RequestCache.KEY_MAP_REQUEST_MATH_NEW);
            } else {
                curMathReq = mathReq;
                newMathReq = providerItem.mCatalogItem;
                cacheReq.saveMapRequestParams(1, providerItem.mCatalogItem, false);
            }
            obj = newMathReq;
        }
        StudySnapshotItem providerItem2 = StudySnapshotItem.findBySubject(items, "05");
        if (providerItem2 == null || providerItem2.mCatalogItem == null) {
            curPhysicalReq = cacheReq.getMapRequestParams(2, true);
            Object newPhysicalReq2 = cacheReq.getMapRequestParams(2, false);
            physicalMap = cacheReq.getMapResp(2);
            obj2 = newPhysicalReq2;
        } else if (providerItem2.mMappingInfo != null) {
            curPhysicalReq = providerItem2.mCatalogItem;
            physicalMap = providerItem2.mMappingInfo;
            cacheReq.saveMapRequestParams(2, providerItem2.mCatalogItem, true);
            cacheReq.saveMapResp(2, providerItem2.mMappingInfo);
            cacheReq.removeForKey(RequestCache.KEY_MAP_REQUEST_PHYSICAL_NEW);
            obj2 = null;
        } else {
            CatalogItem physicalReq = cacheReq.getMapRequestParams(2, true);
            physicalMap = cacheReq.getMapResp(2);
            if (physicalReq == null || physicalMap == null || CatalogItem.isEquals(providerItem2.mCatalogItem, physicalReq)) {
                curPhysicalReq = providerItem2.mCatalogItem;
                newPhysicalReq = null;
                cacheReq.saveMapRequestParams(2, providerItem2.mCatalogItem, true);
                cacheReq.removeForKey(RequestCache.KEY_MAP_RESPONSE_PHYSICAL);
                cacheReq.removeForKey(RequestCache.KEY_MAP_REQUEST_PHYSICAL_NEW);
            } else {
                curPhysicalReq = physicalReq;
                newPhysicalReq = providerItem2.mCatalogItem;
                cacheReq.saveMapRequestParams(2, providerItem2.mCatalogItem, false);
            }
            obj2 = newPhysicalReq;
        }
        StudySnapshotItem providerItem3 = StudySnapshotItem.findBySubject(items, "06");
        if (providerItem3 == null || providerItem3.mCatalogItem == null) {
            curChemicalReq = cacheReq.getMapRequestParams(3, true);
            newChemicalReq = cacheReq.getMapRequestParams(3, false);
            chemicalMap = cacheReq.getMapResp(3);
        } else if (providerItem3.mMappingInfo != null) {
            curChemicalReq = providerItem3.mCatalogItem;
            newChemicalReq = null;
            chemicalMap = providerItem3.mMappingInfo;
            cacheReq.saveMapRequestParams(3, providerItem3.mCatalogItem, true);
            cacheReq.saveMapResp(3, providerItem3.mMappingInfo);
            cacheReq.removeForKey(RequestCache.KEY_MAP_REQUEST_CHEMICAL_NEW);
        } else {
            CatalogItem chemicalReq = cacheReq.getMapRequestParams(3, true);
            chemicalMap = cacheReq.getMapResp(3);
            if (chemicalReq == null || chemicalMap == null || CatalogItem.isEquals(providerItem3.mCatalogItem, chemicalReq)) {
                curChemicalReq = providerItem3.mCatalogItem;
                newChemicalReq = null;
                cacheReq.saveMapRequestParams(3, providerItem3.mCatalogItem, true);
                cacheReq.removeForKey(RequestCache.KEY_MAP_RESPONSE_CHEMICAL);
                cacheReq.removeForKey(RequestCache.KEY_MAP_REQUEST_CHEMICAL_NEW);
            } else {
                curChemicalReq = chemicalReq;
                newChemicalReq = providerItem3.mCatalogItem;
                cacheReq.saveMapRequestParams(3, providerItem3.mCatalogItem, false);
            }
        }
        HashMap hashMap = new HashMap();
        handleResult(curMathReq, RequestCache.KEY_MAP_REQUEST_MATH, obj, RequestCache.KEY_MAP_REQUEST_MATH_NEW, mathMap, RequestCache.KEY_MAP_RESPONSE_MATH, hashMap, cacheReq);
        handleResult(curPhysicalReq, RequestCache.KEY_MAP_REQUEST_PHYSICAL, obj2, RequestCache.KEY_MAP_REQUEST_PHYSICAL_NEW, physicalMap, RequestCache.KEY_MAP_RESPONSE_PHYSICAL, hashMap, cacheReq);
        handleResult(curChemicalReq, RequestCache.KEY_MAP_REQUEST_CHEMICAL, newChemicalReq, RequestCache.KEY_MAP_REQUEST_CHEMICAL_NEW, chemicalMap, RequestCache.KEY_MAP_RESPONSE_CHEMICAL, hashMap, cacheReq);
        if (hashMap.isEmpty()) {
            return null;
        }
        return hashMap;
    }

    private static void handleResult(Object oldReq, String oldReqKey, Object newReq, String newReqKey, Object mapping, String mappingKey, Map<String, Object> result, RequestCache cacheReq) {
        if (oldReq == null) {
            if (newReq != null) {
                cacheReq.moveString(newReqKey, oldReqKey, false);
                cacheReq.removeForKey(mappingKey);
            }
        } else if (mapping != null) {
            result.put(oldReqKey, oldReq);
            result.put(mappingKey, mapping);
            if (newReq != null) {
                result.put(newReqKey, newReq);
            }
        } else if (newReq != null) {
            cacheReq.moveString(newReqKey, oldReqKey, false);
            result.put(oldReqKey, newReq);
        } else {
            result.put(oldReqKey, oldReq);
        }
    }
}