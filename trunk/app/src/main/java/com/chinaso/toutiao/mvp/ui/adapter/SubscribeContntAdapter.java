package com.chinaso.toutiao.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.mvp.entity.SubscribeDesc;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubscribeContntAdapter extends BaseRecyclerViewAdapter<SubscribeDesc> {

    private Context mContext;
    public SubscribeContntAdapter() {
        super(null);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext=parent.getContext();
        return new SubscribeContntHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.subscribe_content_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SubscribeContntHolder) {
            setItemData((SubscribeContntHolder) holder, position);
        }
    }

    private void setItemData(SubscribeContntHolder holder, final int position) {
        SubscribeDesc item = mList.get(position);
        holder.titleTV.setText(item.getName());
        holder.descTV.setText(item.getDesc());
        if(item.isSubscribe()){
            holder.scribeTV.setText("已订阅");
        }else{
            holder.scribeTV.setText("待订阅");
        }

        holder.subscribeContntLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecyclerClick(position);
            }
        });
        switch(position%4){
            case 0:
                holder.subscribeImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.sub1));
                break;
            case 1:
                holder.subscribeImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.sub2));
                break;
            case 2:
                holder.subscribeImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.sub3));
                break;
            case 3:
                holder.subscribeImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.sub4));
                break;
            default:
                break;
        }
    }

    public static class SubscribeContntHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.subscribeImg)
        ImageView subscribeImg;
        @BindView(R.id.titleTV)
        TextView titleTV;
        @BindView(R.id.descTV)
        TextView descTV;
        @BindView(R.id.scribeTV)
        TextView scribeTV;
        @BindView(R.id.subscribeContntLayout)
        RelativeLayout subscribeContntLayout;
        public SubscribeContntHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
