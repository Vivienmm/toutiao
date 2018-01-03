package com.chinaso.toutiao.mvp.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.mvp.entity.PreferCustomizableChannel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreferChannelAdapter extends BaseRecyclerViewAdapter<PreferCustomizableChannel>{
    public PreferChannelAdapter() {
        super(null);
    }

    @Override
    public PreferChannelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prefer_channel_item, parent, false);
        return new PreferChannelHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PreferChannelHolder) {
            setItemValue((PreferChannelHolder)holder, position);
        }
    }

    private void setItemValue(final PreferChannelHolder holder, final int position) {
        PreferCustomizableChannel channelItem = mList.get(position);
        holder.channelItemTV.setText(channelItem.getChannel());
        holder.channelItemImg.setImageResource(channelItem.getResId());
        holder.selectedMarkImg.setSelected(channelItem.isSelected());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag = !holder.selectedMarkImg.isSelected();
                holder.selectedMarkImg.setSelected(flag);
                mList.get(position).setSelected(flag);
                notifyItemChanged(position);
                listener.onRecyclerClick(position);
            }
        });
    }

    public class PreferChannelHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.channelItemTV)
        TextView channelItemTV;
        @BindView(R.id.channelItemImg)
        ImageView channelItemImg;
        @BindView(R.id.selectedMarkImg)
        ImageView selectedMarkImg;

        public PreferChannelHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public List<PreferCustomizableChannel> getSelectedChannels() {
        List<PreferCustomizableChannel> selectedAll = new ArrayList<>();
        for (PreferCustomizableChannel channel : mList) {
            if (channel.isSelected()) {
                selectedAll.add(channel);
            }
        }
        return selectedAll;
    }
}