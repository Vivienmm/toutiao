package com.chinaso.toutiao.mvp.ui.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chinaso.toutiao.R;
import com.chinaso.toutiao.app.TTApplication;
import com.chinaso.toutiao.mvp.entity.user.TopicPerson;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class PersonRecyclerViewAdapter extends BaseRecyclerViewAdapter<TopicPerson> {

    public PersonRecyclerViewAdapter() {
        super(null);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(TTApplication.getApp()).inflate(R.layout.cardview_person_item, parent, false);
        return new PersonViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PersonViewHolder) {
            setItemValues((PersonViewHolder) holder, position);
        }
    }

    private void setItemValues(final PersonViewHolder holder, int position) {
        TopicPerson topicPerson = mList.get(position);
        String introImg = topicPerson.getIntroduceImgUrl();
        if (introImg != null) {
            Glide.with(TTApplication.getApp()).load(introImg).into(holder.introImageView);
        } else {
            holder.introImageView.setBackground(TTApplication.getApp().getResources().getDrawable(R.drawable.qinghai));
        }
        String headImg = topicPerson.getHeadImgUrl();
        if (headImg != null) {
            Glide.with(TTApplication.getApp()).load(headImg).into(holder.headImageView);
        } else {
            holder.headImageView.setImageResource(R.mipmap.boy_selected);
        }

        holder.profileInfoTv.setText(topicPerson.getName() + "/ " + topicPerson.getTitle());
        holder.profileIntroTv.setText(topicPerson.getIntroduce());
        holder.columnTv.setText(topicPerson.getDomain());
        holder.followNumTv.setText(topicPerson.getFollowNum());
        holder.inquiryNumTv.setText(topicPerson.getInquiryNum());
        holder.followTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.followTv.setText("已关注");
                holder.followTv.setBackground(TTApplication.getApp().getResources().getDrawable(R.drawable.follow_icon_selected));
                holder.followTv.setTextColor(TTApplication.getApp().getResources().getColor(R.color.black));
            }
        });
    }

    static class PersonViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.introImageView)
        ImageView introImageView;
        @BindView(R.id.headImageView)
        CircleImageView headImageView;
        @BindView(R.id.profileInfoTv)
        TextView profileInfoTv;
        @BindView(R.id.profileIntroTv)
        TextView profileIntroTv;
        @BindView(R.id.columnTv)
        TextView columnTv;
        @BindView(R.id.followNumTv)
        TextView followNumTv;
        @BindView(R.id.inquiryNumTv)
        TextView inquiryNumTv;
        @BindView(R.id.followTv)
        TextView followTv;
        @BindView(R.id.cardView)
        CardView cardView;

        public PersonViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
