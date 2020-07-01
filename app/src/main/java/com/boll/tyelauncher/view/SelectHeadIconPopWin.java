package com.boll.tyelauncher.view;

package com.toycloud.launcher.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import com.toycloud.launcher.R;
import com.toycloud.launcher.adapter.HeadIconSelectAdapter;
import com.toycloud.launcher.common.GlobalVariable;
import com.toycloud.launcher.util.SharepreferenceUtil;
import framework.hz.salmon.util.GlideImageLoader;
import java.util.ArrayList;

public class SelectHeadIconPopWin extends PopupWindow implements View.OnClickListener {
    private Button btu_confirm;
    private Context context;
    private HeadIconSelectAdapter headIconSelectAdapter;
    /* access modifiers changed from: private */
    public ImageView iv_selectheadicon;
    /* access modifiers changed from: private */
    public ArrayList<Integer> list_default_id = new ArrayList<>();
    /* access modifiers changed from: private */
    public onWhichHeadIconSelect onWhichHeadIconSelect;
    private RecyclerView rv_selectphoto;
    /* access modifiers changed from: private */
    public int selectPosition = -1;

    public interface onWhichHeadIconSelect {
        void onHeadIconSelect(int i, boolean z, int i2);
    }

    public void onClick(View v) {
    }

    public SelectHeadIconPopWin(Context context2, onWhichHeadIconSelect onWhichHeadIconSelect2) {
        super(context2);
        this.context = context2;
        this.onWhichHeadIconSelect = onWhichHeadIconSelect2;
        View view = View.inflate(context2, R.layout.layout_popwin_selectheadicon, (ViewGroup) null);
        initpopuWin(view);
        initData();
        initView(view);
    }

    private void initData() {
        this.list_default_id.clear();
        this.list_default_id.add(Integer.valueOf(R.drawable.pic_tx_01));
        this.list_default_id.add(Integer.valueOf(R.drawable.pic_tx_02));
        this.list_default_id.add(Integer.valueOf(R.drawable.pic_tx_03));
        this.list_default_id.add(Integer.valueOf(R.drawable.pic_tx_04));
        this.list_default_id.add(Integer.valueOf(R.drawable.but_pz));
        this.list_default_id.add(Integer.valueOf(R.drawable.but_tjtx));
    }

    private void initpopuWin(View view) {
        setContentView(view);
        setWidth(-1);
        setHeight(-1);
        setFocusable(true);
        setClippingEnabled(false);
        setBackgroundDrawable(new ColorDrawable(0));
        setOutsideTouchable(true);
    }

    private void initView(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SelectHeadIconPopWin.this.dissMissPopWin();
            }
        });
        this.iv_selectheadicon = (ImageView) view.findViewById(R.id.iv_selectheadicon);
        this.rv_selectphoto = (RecyclerView) view.findViewById(R.id.rv_selectphoto);
        this.btu_confirm = (Button) view.findViewById(R.id.btu_confirm);
        if (GlobalVariable.LOGIN_USER != null) {
            GlideImageLoader.getInstance().displayImageCircle((Activity) this.context, SharepreferenceUtil.getSharepferenceInstance(this.context).getUserInfo().getUsericonpath(), this.iv_selectheadicon, R.drawable.pic_tx_default);
        }
        this.headIconSelectAdapter = new HeadIconSelectAdapter(R.layout.layout_item_headicon, this.list_default_id, new HeadIconSelectAdapter.clickHeadIcon() {
            public void headIconClick(int position) {
                if (position == 5) {
                    int unused = SelectHeadIconPopWin.this.selectPosition = position;
                    SelectHeadIconPopWin.this.onWhichHeadIconSelect.onHeadIconSelect(((Integer) SelectHeadIconPopWin.this.list_default_id.get(SelectHeadIconPopWin.this.selectPosition)).intValue(), true, SelectHeadIconPopWin.this.selectPosition);
                } else if (position == 4) {
                    int unused2 = SelectHeadIconPopWin.this.selectPosition = position;
                    SelectHeadIconPopWin.this.onWhichHeadIconSelect.onHeadIconSelect(((Integer) SelectHeadIconPopWin.this.list_default_id.get(SelectHeadIconPopWin.this.selectPosition)).intValue(), true, SelectHeadIconPopWin.this.selectPosition);
                } else {
                    SelectHeadIconPopWin.this.iv_selectheadicon.setImageResource(((Integer) SelectHeadIconPopWin.this.list_default_id.get(position)).intValue());
                    int unused3 = SelectHeadIconPopWin.this.selectPosition = position;
                }
            }
        });
        this.rv_selectphoto.setLayoutManager(new GridLayoutManager(this.context, 3));
        this.rv_selectphoto.setAdapter(this.headIconSelectAdapter);
        this.btu_confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (SelectHeadIconPopWin.this.selectPosition != -1) {
                    if (SelectHeadIconPopWin.this.selectPosition == 5 || SelectHeadIconPopWin.this.selectPosition == 4) {
                        SelectHeadIconPopWin.this.dissMissPopWin();
                    } else {
                        SelectHeadIconPopWin.this.onWhichHeadIconSelect.onHeadIconSelect(((Integer) SelectHeadIconPopWin.this.list_default_id.get(SelectHeadIconPopWin.this.selectPosition)).intValue(), false, SelectHeadIconPopWin.this.selectPosition);
                    }
                    SelectHeadIconPopWin.this.dissMissPopWin();
                    return;
                }
                SelectHeadIconPopWin.this.dissMissPopWin();
            }
        });
    }

    public void dissMissPopWin() {
        dismiss();
    }
}
