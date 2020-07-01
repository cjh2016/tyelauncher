package com.boll.tyelauncher.adapter;

package com.toycloud.launcher.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

public class MainViewPageAdapter extends PagerAdapter {
    private Context context;
    private List<View> views;

    public MainViewPageAdapter(List<View> views2, Context context2) {
        this.views = views2;
        this.context = context2;
    }

    public int getCount() {
        return this.views.size();
    }

    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public Object instantiateItem(ViewGroup container, int position) {
        View view = this.views.get(position);
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        container.addView(view);
        return view;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public int getItemPosition(Object object) {
        return -2;
    }
}
