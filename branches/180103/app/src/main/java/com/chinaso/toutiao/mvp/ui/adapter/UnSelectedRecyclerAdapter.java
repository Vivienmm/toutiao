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
import com.chinaso.toutiao.mvp.data.NewsChannelItem;
import com.chinaso.toutiao.mvp.listener.OnRecyclerItemClickListener;

import java.util.List;

public class UnSelectedRecyclerAdapter extends RecyclerView.Adapter<UnSelectedRecyclerAdapter.MyViewHolder> {

    private List<NewsChannelItem> mDatas;
    private Context mContext;
    private LayoutInflater inflater;
    private OnRecyclerItemClickListener mOnItemClickListener;

    public UnSelectedRecyclerAdapter(Context context, List<NewsChannelItem> list) {
        this.mContext = context;
        this.mDatas = list;
        inflater = LayoutInflater.from(mContext);
    }

    public List<NewsChannelItem> getChannnelList() {

        return mDatas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.channel_other_list, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        NewsChannelItem channel = mDatas.get(position);
        holder.channelName.setText(channel.getName());
        if (channel.getAdded()) {
            holder.showNew.setVisibility(View.VISIBLE);
        } else {
            holder.showNew.setVisibility(View.INVISIBLE);
        }
        holder.addImg.setTag(position);
        if (mOnItemClickListener != null) {
            holder.allRtv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onRecyclerClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setOnItemClickListener(OnRecyclerItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void addData(NewsChannelItem newItem) {
        int position = mDatas.size();
        mDatas.add(newItem);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, mDatas.size());
    }

    public void removeData(int position) {
        mDatas.remove(position);
        notifyDataSetChanged();
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDatas.size());
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView addImg;
        ImageView showNew;
        TextView channelName;
        RelativeLayout allRtv;

        public MyViewHolder(View view) {
            super(view);
            allRtv = (RelativeLayout) view.findViewById(R.id.rtv_item);
            addImg = (ImageView) view.findViewById(R.id.add_img);
            showNew = (ImageView) view.findViewById(R.id.img_new_channel_func);
            channelName = (TextView) view.findViewById(R.id.channel_name);

        }

    }
}
