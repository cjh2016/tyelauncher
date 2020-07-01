package com.boll.tyelauncher.holder.presenter;

package com.toycloud.launcher.holder.presenter;

import android.content.Context;
import android.text.TextUtils;
import com.toycloud.launcher.holder.presenter.SubjectMapQuerier;

public class MapQuerier {
    public static final long DELAY = 300000;
    private final SubjectMapQuerier mChemicalQuerier;
    private final SubjectMapQuerier mMathQuerier;
    private final SubjectMapQuerier mPhysicalQuerier;
    private final SubjectMapQuerier mScienceQuerier;

    public MapQuerier(Context context, SubjectMapQuerier.Callback callback) {
        this.mMathQuerier = new SubjectMapQuerier(context, "02", DELAY);
        this.mMathQuerier.setCallback(callback);
        this.mPhysicalQuerier = new SubjectMapQuerier(context, "05", DELAY);
        this.mPhysicalQuerier.setCallback(callback);
        this.mChemicalQuerier = new SubjectMapQuerier(context, "06", DELAY);
        this.mChemicalQuerier.setCallback(callback);
        this.mScienceQuerier = new SubjectMapQuerier(context, "19", DELAY);
        this.mScienceQuerier.setCallback(callback);
    }

    public void queryImmediately(CatalogItem catalogInfo) {
        if (catalogInfo != null) {
            String subject = catalogInfo.subjectCode;
            if (TextUtils.equals(subject, "02")) {
                this.mMathQuerier.queryImmediately(catalogInfo);
            } else if (TextUtils.equals(subject, "05")) {
                this.mPhysicalQuerier.queryImmediately(catalogInfo);
            } else if (TextUtils.equals(subject, "06")) {
                this.mChemicalQuerier.queryImmediately(catalogInfo);
            } else if (TextUtils.equals(subject, "19")) {
                this.mScienceQuerier.queryImmediately(catalogInfo);
            } else {
                throw new IllegalArgumentException("不合法的subjectCode: " + subject);
            }
        }
    }

    public void queryMaybeDelay(CatalogItem catalogInfo) {
        if (catalogInfo != null) {
            String subject = catalogInfo.subjectCode;
            if (TextUtils.equals(subject, "02")) {
                this.mMathQuerier.queryMaybeDelay(catalogInfo);
            } else if (TextUtils.equals(subject, "05")) {
                this.mPhysicalQuerier.queryMaybeDelay(catalogInfo);
            } else if (TextUtils.equals(subject, "06")) {
                this.mChemicalQuerier.queryMaybeDelay(catalogInfo);
            } else if (TextUtils.equals(subject, "19")) {
                this.mScienceQuerier.queryMaybeDelay(catalogInfo);
            } else {
                throw new IllegalArgumentException("不合法的subjectCode: " + subject);
            }
        }
    }

    public void cancelMath() {
        this.mMathQuerier.cancel();
    }

    public void cancelPhysical() {
        this.mPhysicalQuerier.cancel();
    }

    public void cancelChemical() {
        this.mChemicalQuerier.cancel();
    }

    public void cancelScience() {
        this.mScienceQuerier.cancel();
    }

    public void cancelAll() {
        this.mMathQuerier.cancel();
        this.mPhysicalQuerier.cancel();
        this.mChemicalQuerier.cancel();
        this.mScienceQuerier.cancel();
    }
}
