package com.chinaso.toutiao.mvp.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.mvp.entity.SubScribeColumn;
import com.chinaso.toutiao.mvp.entity.SubscribeChannel;
import com.chinaso.toutiao.mvp.entity.SubscribeDesc;
import com.chinaso.toutiao.mvp.listener.OnRecyclerItemClickListener;
import com.chinaso.toutiao.mvp.ui.adapter.SubscribeAdapter;
import com.chinaso.toutiao.mvp.ui.adapter.SubscribeContntAdapter;
import com.chinaso.toutiao.mvp.ui.fragment.base.BaseFragment;
import com.chinaso.toutiao.net.SubScribeService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscribeFragment extends BaseFragment {
    @BindView(R.id.subscribeChannelRV)
    RecyclerView subscribeChannelRV;
    @BindView(R.id.subscribeChannelContentRV)
    RecyclerView subscribeChannelContentRV;

    private List<SubscribeChannel> subChnls = new ArrayList<>();
    private List<SubscribeDesc> chnlContents = new ArrayList<>();
    private SubscribeContntAdapter sccontntAdapter;
    private SubscribeAdapter subAadapter;

    @Override
    public int initLayout() {
        return R.layout.fragment_subscribe;
    }

    @Override
    public void initViews(View view) {
        initColumns();
        initSubscribeChannelRV();
        initSubscribeChannelContentRV();
    }

    private void initColumns() {

        Call<List<SubScribeColumn>> call = SubScribeService.getInstance().getSubscribColumn();
        call.enqueue(new Callback<List<SubScribeColumn>>() {
            @Override
            public void onResponse(Call<List<SubScribeColumn>> call, Response<List<SubScribeColumn>> response) {
                if (response.isSuccessful()) {
                    List<SubScribeColumn> result = response.body();
                    SubscribeDesc entery;
                    SubscribeChannel channel;
                    for (int i = 0; i < result.size(); i++) {
                        SubScribeColumn column = result.get(i);
                        channel = new SubscribeChannel();
                        channel.setSelected(i == 0);
                        channel.setName(column.getName());
                        subChnls.add(channel);
                        for (int m = 0; m < column.getColumns().size(); m++) {
                            entery = new SubscribeDesc();
                            entery.setColumn(column.getName());
                            entery.setDesc(column.getColumns().get(m).getAbstractX());
                            entery.setSubscribe(column.getColumns().get(m).getIsSubscribe());
                            entery.setName(column.getColumns().get(m).getColumn_name());
                            chnlContents.add(entery);
                        }
                    }
                    subAadapter.setLists(subChnls);
                    sccontntAdapter.setLists(getColumnData("我的"));
                }
            }

            @Override
            public void onFailure(Call<List<SubScribeColumn>> call, Throwable t) {

            }
        });
    }

    private void initSubscribeChannelRV() {
        subscribeChannelRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        subAadapter = new SubscribeAdapter();
        subscribeChannelRV.setAdapter(subAadapter);
        subAadapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onRecyclerClick(int position) {
                sccontntAdapter.setLists(getColumnData(subChnls.get(position).getName()));
                sccontntAdapter.notifyDataSetChanged();
                subscribeChannelContentRV.scrollToPosition(0);
            }
        });
    }

    private List<SubscribeDesc> getColumnData(String s) {
        List<SubscribeDesc> mLists = new ArrayList<>();
        for (SubscribeDesc item : chnlContents) {
            if (item.getColumn().equals(s)) {
                mLists.add(item);
            }
        }
        return mLists;
    }

    private void initSubscribeChannelContentRV() {
        subscribeChannelContentRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        sccontntAdapter = new SubscribeContntAdapter();
        subscribeChannelContentRV.setAdapter(sccontntAdapter);
        sccontntAdapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onRecyclerClick(int position) {
                Toast.makeText(getActivity(), "您选择的是" + chnlContents.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
