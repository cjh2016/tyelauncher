package com.boll.tyelauncher.view;

package com.toycloud.launcher.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.toycloud.launcher.R;
import java.util.List;

public class TabLayout extends RecyclerView {
    private boolean hasAddDecoration;
    /* access modifiers changed from: private */
    public OnTabSelectedListener mOnTabSelectedListener;
    private TabAdapter mTabAdapter;
    private List<TabInfo> mTabInfoList;

    public interface OnTabSelectedListener {
        void onTabSelected(int i);
    }

    public static class TabInfo {
        /* access modifiers changed from: private */
        public String mText;
        private boolean selected;

        public TabInfo() {
        }

        public TabInfo(String text) {
            this.mText = text;
        }

        public boolean isSelected() {
            return this.selected;
        }

        public void setSelected(boolean selected2) {
            this.selected = selected2;
        }

        public void setText(String text) {
            this.mText = text;
        }
    }

    public static class TabItemDecoration extends RecyclerView.ItemDecoration {
        private int mSpace;

        public TabItemDecoration(int space) {
            this.mSpace = space;
        }

        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.right = this.mSpace;
        }
    }

    public TabLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public TabLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabLayout(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.hasAddDecoration = false;
    }

    @CallSuper
    public void initTabLayout(@NonNull List<TabInfo> data) {
        this.mTabInfoList = data;
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), 0, false);
        setHasFixedSize(true);
        setLayoutManager(manager);
        this.mTabAdapter = new TabAdapter(this.mTabInfoList);
        setAdapter(this.mTabAdapter);
        setClickListenerForAdapter();
        if (!this.hasAddDecoration) {
            addItemDecoration(new TabItemDecoration(-1));
            this.hasAddDecoration = true;
        }
    }

    public void setCurrentItem(int position) {
        Log.d("DEBUG", "position = " + position);
        int i = 0;
        while (i < this.mTabInfoList.size()) {
            this.mTabInfoList.get(i).setSelected(position == i);
            i++;
        }
        this.mTabAdapter.notifyDataSetChanged();
    }

    public void setOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        this.mOnTabSelectedListener = onTabSelectedListener;
        if (this.mTabAdapter != null) {
            setClickListenerForAdapter();
        }
    }

    private void setClickListenerForAdapter() {
        this.mTabAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                TabLayout.this.setCurrentItem(position);
                TabLayout.this.mOnTabSelectedListener.onTabSelected(position);
            }
        });
    }

    class TabAdapter extends BaseQuickAdapter<TabInfo, BaseViewHolder> {
        public TabAdapter(@NonNull List<TabInfo> data) {
            super(R.layout.item_tab, data);
        }

        /* access modifiers changed from: protected */
        public void convert(BaseViewHolder helper, TabInfo item) {
            int resBackgroundDrawable;
            int count = getItemCount();
            if (count > 0) {
                helper.setText((int) R.id.txt_title, (CharSequence) item.mText);
                ((TextView) helper.getView(R.id.txt_title)).setTextColor(TabLayout.this.getResources().getColorStateList(R.color.color_text_indecator_answered_selector));
                helper.getView(R.id.txt_title).setSelected(item.isSelected());
                if (count == 1) {
                    resBackgroundDrawable = R.drawable.bg_indecator_answered_only_one;
                } else {
                    int position = helper.getAdapterPosition();
                    if (position > 0 && position < count - 1) {
                        resBackgroundDrawable = R.drawable.bg_indecator_answered_middle;
                    } else if (position == 0) {
                        resBackgroundDrawable = R.drawable.bg_indecator_answered_first;
                    } else {
                        resBackgroundDrawable = R.drawable.bg_indecator_answered_end;
                    }
                }
                helper.itemView.setBackground(TabLayout.this.getResources().getDrawable(resBackgroundDrawable));
                helper.itemView.setSelected(item.isSelected());
            }
        }
    }
}
