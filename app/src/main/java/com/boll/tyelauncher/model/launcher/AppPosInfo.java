package com.boll.tyelauncher.model.launcher;


public class AppPosInfo {
    public int mCellIndex;
    public int mPageIndex;

    public AppPosInfo(int pageIndex, int cellIndex) {
        this.mPageIndex = pageIndex;
        this.mCellIndex = cellIndex;
    }

    public void desPageIndex() {
        this.mPageIndex--;
    }

    public void incPageIndex() {
        this.mPageIndex++;
    }
}
