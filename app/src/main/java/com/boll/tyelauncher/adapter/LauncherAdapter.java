package com.boll.tyelauncher.adapter;

package com.toycloud.launcher.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.anarchy.classify.simple.AppInfo;
import com.iflytek.cbg.common.utils.ListUtils;
import com.toycloud.launcher.holder.BaseHolder;
import com.toycloud.launcher.holder.Launcher_3rdAPP_ViewHold;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LauncherAdapter extends PagerAdapter {
    private List<BaseHolder> mAppHolders;
    private List<BaseHolder> mCardHolders;

    public LauncherAdapter(List<BaseHolder> prevHolders, List<BaseHolder> appHolders) {
        this.mCardHolders = prevHolders;
        this.mAppHolders = appHolders;
    }

    public void addCardViewHolder(BaseHolder viewHolder) {
        if (viewHolder != null && !this.mCardHolders.contains(viewHolder)) {
            this.mCardHolders.add(viewHolder);
            notifyDataSetChanged();
        }
    }

    public void removeCardViewHolder(BaseHolder viewHolder) {
        if (viewHolder != null && this.mCardHolders.remove(viewHolder)) {
            notifyDataSetChanged();
        }
    }

    public int getCount() {
        return ListUtils.size(this.mCardHolders) + ListUtils.size(this.mAppHolders);
    }

    public int getCardViewCount() {
        return ListUtils.size(this.mCardHolders);
    }

    public boolean isViewFromObject(View view, Object object) {
        return view == ((BaseHolder) object).getRootView();
    }

    public Object instantiateItem(ViewGroup container, int position) {
        BaseHolder holder;
        int prevSize = ListUtils.size(this.mCardHolders);
        if (position < prevSize) {
            holder = this.mCardHolders.get(position);
        } else {
            holder = this.mAppHolders.get(position - prevSize);
        }
        View view = holder.getRootView();
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        container.addView(view);
        return holder;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(((BaseHolder) object).getRootView());
    }

    public int getItemPosition(Object object) {
        int index = this.mCardHolders.indexOf(object);
        if (index < 0) {
            int index2 = this.mAppHolders.indexOf(object);
            if (index2 < 0) {
                return -2;
            }
            index = index2 + this.mCardHolders.size();
        }
        return index;
    }

    public Launcher_3rdAPP_ViewHold getAppViewHolder(int pagerIndex) {
        return (Launcher_3rdAPP_ViewHold) ListUtils.getItem(this.mAppHolders, pagerIndex - ListUtils.size(this.mCardHolders));
    }

    public Launcher_3rdAPP_ViewHold getAppViewHolder(AppInfo appInfo) {
        if (appInfo == null) {
            return null;
        }
        return getAppViewHolder(appInfo.getPakageName());
    }

    public Launcher_3rdAPP_ViewHold getAppViewHolder(String pkgName) {
        if (this.mAppHolders == null) {
            return null;
        }
        Iterator<BaseHolder> it = this.mAppHolders.iterator();
        while (it.hasNext()) {
            Launcher_3rdAPP_ViewHold viewHold = (Launcher_3rdAPP_ViewHold) it.next();
            if (viewHold.getAppIndex(pkgName) >= 0) {
                return viewHold;
            }
        }
        return null;
    }

    public List<BaseHolder> getAllAppListHolders() {
        return this.mAppHolders;
    }

    public List<BaseHolder> getCardHolders() {
        return this.mCardHolders;
    }

    public List<BaseHolder> getAllHolders() {
        List<BaseHolder> holders = new ArrayList<>();
        if (this.mCardHolders != null) {
            holders.addAll(this.mCardHolders);
        }
        if (this.mAppHolders != null) {
            holders.addAll(this.mAppHolders);
        }
        return holders;
    }

    public void setEditMode(boolean editMode) {
        if (this.mAppHolders != null) {
            Iterator<BaseHolder> it = this.mAppHolders.iterator();
            while (it.hasNext()) {
                ((Launcher_3rdAPP_ViewHold) it.next()).setEditMode(editMode);
            }
        }
    }
}
