package com.chinaso.toutiao.mvp.ui.adapter;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.app.TTApplication;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThemeDomainGVAdapter extends BaseRecyclerViewAdapter<String> {
    Resources resources = TTApplication.getApp().getResources();
    String[] preferChannelName = resources.getStringArray(R.array.topic_quiz_name);
    TypedArray ar = resources.obtainTypedArray(R.array.topic_quiz_icon);
    public ThemeDomainGVAdapter() {
        super(null);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DomainViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.gridview_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DomainViewHolder) {
            setItemValues((DomainViewHolder) holder, position);
        }
    }

    private void setItemValues(DomainViewHolder holder, int position) {
        String item = mList.get(position);
        holder.domTv.setText(preferChannelName[position]);
        holder.domImg.setImageResource(ar.getResourceId(position, 0));
    }

    public static class DomainViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.domImg)
        ImageView domImg;
        @BindView(R.id.domTv)
        TextView domTv;
        public DomainViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
