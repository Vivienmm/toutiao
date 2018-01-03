package com.chinaso.toutiao.mvp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.mvp.data.collection.CollectionEntity;

import java.util.List;

public class CollectionAdapter extends BaseAdapter {

    private Context context;
    private List<CollectionEntity> collections;

    public CollectionAdapter(Context context, List collections) {
        this.context = context;
        this.collections = collections;
    }

    @Override
    public int getCount() {
        return collections.size();
    }

    @Override
    public Object getItem(int position) {
        return collections.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CollectionVH vh;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_collection, parent, false);
            vh = new CollectionVH(convertView);
            convertView.setTag(vh);
        } else {
            vh = (CollectionVH) convertView.getTag();
        }

        vh.titleTxt.setText(collections.get(position).getTitle());
        vh.urlTxt.setText(collections.get(position).getUrl());

        return convertView;
    }


    class CollectionVH {
        TextView urlTxt;
        TextView titleTxt;

        public CollectionVH(View v) {
            urlTxt = (TextView) v.findViewById(R.id.collection_item_url);
            titleTxt = (TextView) v.findViewById(R.id.collection_item_title);
        }

    }
}
