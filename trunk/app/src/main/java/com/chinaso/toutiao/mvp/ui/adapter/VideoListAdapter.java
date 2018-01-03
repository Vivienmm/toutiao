package com.chinaso.toutiao.mvp.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chinaso.toutiao.R;
import com.chinaso.toutiao.app.TTApplication;
import com.chinaso.toutiao.app.entity.VideoListItem;
import com.chinaso.toutiao.mvp.entity.ShareInfoEntity;
import com.chinaso.toutiao.util.Utils;
import com.chinaso.toutiao.util.shareutil.ShareContentUtil;
import com.chinaso.toutiao.util.shareutil.ShareContentUtilInterface;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoListAdapter extends BaseRecyclerViewAdapter<VideoListItem> {
    private Context mContext;
    private ShareContentUtilInterface shareContentUtil;
    private ShareInfoEntity mShareInfoEntity;
    public VideoListAdapter() {
        super(null);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new VideoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VideoViewHolder) {
            setItemValues((VideoViewHolder)holder, position);

        }
    }

    private void setItemValues(final VideoViewHolder holder, final int position) {
        final VideoListItem item = mList.get(position);
        holder.nameTV.setText(item.getMname());


        if (!TextUtils.isEmpty(item.getTime())) {
            holder.timeTV.setText(Utils.diffDate(item.getTime()));
        }
        Glide.with(TTApplication.getApp()).load(item.getPicture()).into(holder.videoPic);
        holder.videoPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRecyclerClick(position);
            }
        });

        holder.shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareContentUtil = new ShareContentUtil((Activity) mContext);
                mShareInfoEntity = new ShareInfoEntity();
                mShareInfoEntity.setTitle(item.getTitle());
                mShareInfoEntity.setTargetUrl(item.getUrl());
                mShareInfoEntity.setContent(item.getDescription());
                mShareInfoEntity.setPicUrl(item.getPicture());
                shareContentUtil.startShare(mShareInfoEntity, 1);
            }
        });
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.videoPic)
        ImageView videoPic;
        @BindView(R.id.timeTV)
        TextView timeTV;
        @BindView(R.id.nameTV)
        TextView nameTV;
        @BindView(R.id.shareImageView)
        ImageView shareImageView;

        public VideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
