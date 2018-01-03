package com.chinaso.toutiao.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.mvp.data.NewsChannelItem;

import java.util.List;

public class RecyclerDragAdapter extends RecyclerView.Adapter<RecyclerDragAdapter.GridViewHolder> {
    private Context mContext;
    private List<NewsChannelItem> mList;
    private LayoutInflater inflater;

    public RecyclerDragAdapter(Context context, List<NewsChannelItem> list) {
        mContext = context;
        mList = list;
        inflater = LayoutInflater.from(mContext);
    }


    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.grid_item_selected_channel, parent, false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        holder.tv.setText(mList.get(position).getName());
        holder.rtv.setEnabled(false);
        if (mList.get(position).getAdded()) {
            holder.img.setVisibility(View.VISIBLE);
        }else {
            holder.img.setVisibility(View.INVISIBLE);
        }
        if (mList.get(position).getLock()) {
            holder.tv.setEnabled(false);
        } else {
            holder.tv.setEnabled(true);
        }


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public List<NewsChannelItem> getList() {
        return mList;
    }


    class GridViewHolder extends RecyclerView.ViewHolder {

        TextView tv;
        ImageView img;
        FrameLayout rtv;

        public GridViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.grid_text);
            img = (ImageView) view.findViewById(R.id.img_new_channel_func);
            rtv = (FrameLayout) view.findViewById(R.id.rl_subscribe);
        }

    }

}
