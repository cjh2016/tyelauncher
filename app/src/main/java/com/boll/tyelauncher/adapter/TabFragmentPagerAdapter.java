package com.boll.tyelauncher.adapter;

package com.toycloud.launcher.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import framework.hz.salmon.base.BaseFragment;
import java.util.List;

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> fragments;

    public TabFragmentPagerAdapter(FragmentManager fm, List<BaseFragment> fragments2) {
        super(fm);
        this.fragments = fragments2;
    }

    public Fragment getItem(int arg0) {
        return this.fragments.get(arg0);
    }

    public int getCount() {
        return this.fragments.size();
    }
}