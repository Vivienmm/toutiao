package com.chinaso.toutiao.mvp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.mvp.data.readhistory.ReadHistoryEntity;
import com.chinaso.toutiao.mvp.ui.activity.VerticalDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsHistoryAdapter extends BaseRecyclerViewAdapter<ReadHistoryEntity> {
    private Context mContext;
    public NewsHistoryAdapter() {
        super(null);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new HistoryViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_collection, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HistoryViewHolder) {
            setItemData((HistoryViewHolder) holder, position);
        }
    }

    private void setItemData(HistoryViewHolder holder, final int position) {
        final ReadHistoryEntity entity = mList.get(position);
        holder.collectionItemTitle.setText(entity.getTitle());
        holder.collectionItemUrl.setText(entity.getUrl());
        holder.collectionItemTitle.setTextSize(16);
        holder.collectionItemUrl.setVisibility(View.GONE);
        holder.collectionItemTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VerticalDetailActivity.class);
                intent.putExtra("newsShowUrl", entity.getUrl());
                mContext.startActivity(intent);
            }
        });
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.collection_item_title)
        TextView collectionItemTitle;
        @BindView(R.id.collection_item_url)
        TextView collectionItemUrl;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
