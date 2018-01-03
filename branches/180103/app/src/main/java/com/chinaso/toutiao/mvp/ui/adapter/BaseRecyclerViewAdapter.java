package com.chinaso.toutiao.mvp.ui.adapter;

import android.support.v7.widget.RecyclerView;

import com.chinaso.toutiao.mvp.listener.OnRecyclerItemClickListener;

import java.util.List;

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public List<T> mList ;
    public OnRecyclerItemClickListener listener;

    public void setOnItemClickListener(OnRecyclerItemClickListener listener) {
        this.listener = listener;
    }
    public BaseRecyclerViewAdapter(List<T> lists) {
        this.mList = lists;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void setLists(List<T> items) {
        mList = items;
        notifyDataSetChanged();
    }

    public void add(int position, T item) {
        mList.add(position, item);
        notifyItemInserted(position);
    }

    public void addMore(List<T> items) {
        int startPosition = mList.size();
        mList.addAll(items);
        notifyItemRangeInserted(startPosition, mList.size());
    }

    public void delete(int item ) {
        mList.remove(item);
        notifyItemRemoved(item);
    }

    public List<T> getLists() {
        return mList;
    }

}
