package com.chinaso.toutiao.mvp.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chinaso.toutiao.R;
import com.chinaso.toutiao.app.TTApplication;
import com.chinaso.toutiao.app.entity.NewsListItem;
import com.chinaso.toutiao.util.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsListAdapter extends BaseRecyclerViewAdapter<NewsListItem>{
    private enum ITEM_TYPE {
        ITEM1, //主题
        ITEM2
    }

    public NewsListAdapter() {
        super(null);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM1.ordinal()) {
            return new NewsViewSubjectHolder(getView(parent, R.layout.fragment_newslist_item));
        } else if (viewType == ITEM_TYPE.ITEM2.ordinal()) {
            return new PhotoNewsViewHolder(getView(parent, R.layout.fragment_newslist_item_photo));
        } else {
            throw new RuntimeException("there is no type that matches the type " +
                    viewType + " + make sure your using types correctly");
        }
    }

    public View getView(ViewGroup group, int layoutId) {
        return LayoutInflater.from(group.getContext()).inflate(layoutId, group, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final NewsListItem item = mList.get(position);
        if (item == null) {
            return;
        }
        if (holder instanceof PhotoNewsViewHolder) {
            final PhotoNewsViewHolder newsHolder = (PhotoNewsViewHolder)holder;
            newsHolder.titleTV.setText(item.getTitle());
            Glide.with(TTApplication.getApp()).load(item.getPictureList().get(0)).into(newsHolder.img);
            Glide.with(TTApplication.getApp()).load(item.getPictureList().get(1)).into(newsHolder.img02);
            Glide.with(TTApplication.getApp()).load(item.getPictureList().get(2)).into(newsHolder.img03);
            if (!TextUtils.isEmpty(item.getTime())) {
                newsHolder.sourseTimeTV.setText(item.getMname() + " " + Utils.diffDate(item.getTime()));
            }
            newsHolder.newsitemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onRecyclerClick(position);
                }
            });
        } else if (holder instanceof NewsViewSubjectHolder) {
            NewsViewSubjectHolder subJectHolder = (NewsViewSubjectHolder) holder;
            if (!TextUtils.isEmpty(item.getTime())) {
                subJectHolder.sourseTimeTV.setText(item.getMname() + " " + Utils.diffDate(item.getTime()));
            }
            subJectHolder.subjectTV.setText(item.getSign());
            subJectHolder.titleTV.setText(item.getTitle());
            Glide.with(TTApplication.getApp()).load(item.getPictureList().get(0)).into(subJectHolder.img);
            subJectHolder.newsitemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onRecyclerClick(position);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mList == null) {
            return ITEM_TYPE.ITEM1.ordinal();
        } else if (mList.get(position) == null) {
            return ITEM_TYPE.ITEM1.ordinal();
        }
        List<String> pics = mList.get(position).getPictureList();
        return (pics == null) || pics.size() == 1?
                ITEM_TYPE.ITEM1.ordinal():ITEM_TYPE.ITEM2.ordinal();
    }

    public static class NewsViewSubjectHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.newsitemLayout)
        LinearLayout newsitemLayout;
        @BindView(R.id.titleTV)
        TextView titleTV;
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.sourseTimeTV)
        TextView sourseTimeTV;
        @BindView(R.id.subjectTV)
        TextView subjectTV;
        public NewsViewSubjectHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class PhotoNewsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.newsitemLayout)
        LinearLayout newsitemLayout;
        @BindView(R.id.titleTV)
        TextView titleTV;
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.img02)
        ImageView img02;
        @BindView(R.id.img03)
        ImageView img03;
        @BindView(R.id.sourseTimeTV)
        TextView sourseTimeTV;
        public PhotoNewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
