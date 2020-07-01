package com.boll.tyelauncher.adapter;

package com.toycloud.launcher.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.toycloud.launcher.R;
import com.toycloud.launcher.model.AppContants;
import com.toycloud.launcher.model.WifiBean;
import com.toycloud.launcher.util.WifiSupport;
import java.util.ArrayList;
import java.util.List;

public class WifiListAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context mContext;
    /* access modifiers changed from: private */
    public onItemClickListener onItemClickListener;
    private List<WifiBean> resultList;

    public interface onItemClickListener {
        void onItemClick(View view, int i, Object obj);
    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener2) {
        this.onItemClickListener = onItemClickListener2;
    }

    public WifiListAdapter(Context mContext2, List<WifiBean> resultList2) {
        this.mContext = mContext2;
        this.resultList = resultList2;
    }

    public void setResultList(List<WifiBean> resultList2) {
        if (this.resultList == null) {
            this.resultList = new ArrayList();
        } else {
            this.resultList.clear();
        }
        this.resultList.addAll(resultList2);
        notifyDataSetChanged();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.item_wifi_list, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final WifiBean bean = this.resultList.get(position);
        holder.tvItemWifiName.setText(bean.getWifiName());
        if (AppContants.WIFI_STATE_ON_CONNECTING.equals(bean.getState()) || AppContants.WIFI_STATE_CONNECT.equals(bean.getState())) {
            holder.tvItemWifiName.setTextColor(Color.parseColor("#0578FA"));
            holder.tvItemWifiStatus.setTextColor(Color.parseColor("#0578FA"));
            holder.tvItemWifiStatus.setText("(" + bean.getState() + ")");
            holder.tvItemWifiStatus.setVisibility(0);
            this.resultList.get(position).setConnect(true);
        } else if (AppContants.WIFI_STATE_FAIl.equals(bean.getState())) {
            holder.iv_connectstatus.setVisibility(4);
            holder.tvItemWifiStatus.setVisibility(0);
            holder.tvItemWifiName.setTextColor(this.mContext.getResources().getColor(R.color.gray_home));
            holder.tvItemWifiStatus.setTextColor(Color.parseColor("#3F3F3F"));
            holder.tvItemWifiStatus.setText("  (请检查密码，然后重试)");
            this.resultList.get(position).setConnect(false);
        } else {
            holder.iv_connectstatus.setVisibility(4);
            holder.tvItemWifiStatus.setVisibility(4);
            holder.tvItemWifiName.setTextColor(this.mContext.getResources().getColor(R.color.gray_home));
            holder.tvItemWifiStatus.setTextColor(this.mContext.getResources().getColor(R.color.gray_home));
            this.resultList.get(position).setConnect(false);
        }
        holder.itemview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (WifiListAdapter.this.onItemClickListener != null) {
                    WifiListAdapter.this.onItemClickListener.onItemClick(view, position, bean);
                }
            }
        });
        switch (Integer.parseInt(bean.getLevel())) {
            case 1:
            case 2:
                holder.icon_wifi_right.setImageResource(R.drawable.wifi_full);
                break;
            case 3:
                holder.icon_wifi_right.setImageResource(R.drawable.wifi_3);
                break;
            case 4:
                holder.icon_wifi_right.setImageResource(R.drawable.wifi_2);
                break;
            case 5:
            case 6:
            case 7:
                holder.icon_wifi_right.setImageResource(R.drawable.wifi_1);
                break;
            default:
                holder.icon_wifi_right.setImageResource(R.drawable.wifi_3);
                break;
        }
        if (WifiSupport.getWifiCipher(bean.getCapabilities()) == WifiSupport.WifiCipherType.WIFICIPHER_NOPASS) {
            holder.status_wifi_cap.setImageResource(R.drawable.unlock);
        } else {
            holder.status_wifi_cap.setImageResource(R.drawable.lock);
        }
        Log.e("bean-->", bean.getCapabilities() + ":" + bean.getLevel() + ":" + bean.getWifiName());
    }

    public void replaceAll(List<WifiBean> datas) {
        if (this.resultList.size() > 0) {
            this.resultList.clear();
        }
        this.resultList.addAll(datas);
        notifyDataSetChanged();
    }

    public int getItemCount() {
        if (this.resultList == null) {
            return 0;
        }
        return this.resultList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView icon_wifi_right;
        View itemview;
        ImageView iv_connectstatus;
        ImageView status_wifi_cap;
        TextView tvItemWifiName;
        TextView tvItemWifiStatus;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemview = itemView;
            this.tvItemWifiName = (TextView) itemView.findViewById(R.id.tv_item_wifi_name);
            this.tvItemWifiStatus = (TextView) itemView.findViewById(R.id.tv_item_wifi_status);
            this.iv_connectstatus = (ImageView) itemView.findViewById(R.id.iv_connectstatus);
            this.status_wifi_cap = (ImageView) itemView.findViewById(R.id.status_wifi_cap);
            this.icon_wifi_right = (ImageView) itemView.findViewById(R.id.icon_wifi_right);
        }
    }
}