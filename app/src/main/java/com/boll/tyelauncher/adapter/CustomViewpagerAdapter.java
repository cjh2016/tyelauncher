package com.boll.tyelauncher.adapter;

package com.toycloud.launcher.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CustomViewpagerAdapter extends FragmentPagerAdapter {
    public CustomViewpagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public Fragment getItem(int position) {
        return null;
    }

    public int getCount() {
        return 0;
    }
}
