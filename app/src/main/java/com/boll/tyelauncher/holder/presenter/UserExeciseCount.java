package com.boll.tyelauncher.holder.presenter;

package com.toycloud.launcher.holder.presenter;

public class UserExeciseCount {
    public int mChemicalConnt;
    public int mMathCount;
    public int mPhysicalCount;
    public int mScienceCount;

    public void resetMathCount() {
        this.mMathCount = 0;
    }

    public void resetPhysicalCount() {
        this.mPhysicalCount = 0;
    }

    public void resetChemicalCount() {
        this.mChemicalConnt = 0;
    }

    public void resetScienceCount() {
        this.mScienceCount = 0;
    }

    public void reset() {
        this.mMathCount = 0;
        this.mPhysicalCount = 0;
        this.mChemicalConnt = 0;
        this.mScienceCount = 0;
    }
}