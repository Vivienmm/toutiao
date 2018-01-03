package com.chinaso.toutiao.mvp.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.mvp.entity.SubscribeChannel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubscribeAdapter extends BaseRecyclerViewAdapter<SubscribeChannel> {
    private int mSelectedPos = 0;
    public SubscribeAdapter() {
        super(null);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SubScribeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.subscribe_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SubScribeViewHolder) {
            setItemData((SubScribeViewHolder)holder, position);
        }
    }

    private void setItemData(SubScribeViewHolder holder, final int position) {
        SubscribeChannel channel = mList.get(position);
        holder.subscribeChannelTV.setText(channel.getName());
        holder.subscribeChannelTV.setSelected(channel.isSelected());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecyclerClick(position);
                if (mSelectedPos != position) {
                    mList.get(mSelectedPos).setSelected(false);
                    notifyItemChanged(mSelectedPos);
                    mSelectedPos = position;
                    mList.get(mSelectedPos).setSelected(true);
                    notifyItemChanged(mSelectedPos);
                }
            }
        });
    }

    public static class SubScribeViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.subscribeChannelTV)
        TextView subscribeChannelTV;
        public SubScribeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
