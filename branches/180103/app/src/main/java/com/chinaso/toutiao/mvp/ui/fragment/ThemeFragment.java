package com.chinaso.toutiao.mvp.ui.fragment;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.mvp.entity.user.TopicPerson;
import com.chinaso.toutiao.mvp.ui.adapter.ThemeDomainGVAdapter;
import com.chinaso.toutiao.mvp.ui.adapter.PersonRecyclerViewAdapter;
import com.chinaso.toutiao.mvp.ui.fragment.base.BaseFragment;
import com.chinaso.toutiao.view.ScrollRecycleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ThemeFragment extends BaseFragment {

    @BindView(R.id.themeSearchImg)
    ImageView themeSearchImg;
    @BindView(R.id.gridView)
    ScrollRecycleView domGridView;
    @BindView(R.id.arrow)
    ImageView controlArw;
    @BindView(R.id.themeRecyclerView)
    ScrollRecycleView themeRecyclerView;

    private PersonRecyclerViewAdapter recyclerAdapter;
    private List<String> domList = new ArrayList<>();
    private List<TopicPerson> personList = new ArrayList<>();
    private ThemeDomainGVAdapter domAdapter;
    private boolean unfoldFlag = false;

    @Override
    public int initLayout() {
        return R.layout.fragment_theme;
    }

    @Override
    public void initViews(View view) {
        initDomData();
        intPersonData();
        initDomainView();
        initPersonView();
    }

    private void initPersonView() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        themeRecyclerView.setLayoutManager(llm);
        recyclerAdapter = new PersonRecyclerViewAdapter();
        recyclerAdapter.setLists(personList);
        themeRecyclerView.setAdapter(recyclerAdapter);
    }

    private void initDomainView() {
        domAdapter = new ThemeDomainGVAdapter();
        domGridView.addItemDecoration(new SpacesItemDecoration(0, 8, 0, 2));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        domGridView.setLayoutManager(gridLayoutManager);
        domGridView.setAdapter(domAdapter);
        domAdapter.setLists(domList.subList(0,5));
    }

    private void intPersonData() {
        TopicPerson sPerson = new TopicPerson();
        sPerson.setDomain("科技");
        sPerson.setName("小安卓");
        sPerson.setTitle("开发者");
        sPerson.setIntroduce("谷歌亲儿子，收到各手机厂商的青睐，开源框架，最新版本牛轧糖");
        sPerson.setHeadImgUrl("http://tse1.mm.bing.net/th?id=OET.3e53dd0f5a1547d582b126ccb52425ce&w=135&h=135&c=7&rs=1&qlt=90&o=4&pid=1.9");
        sPerson.setFollowNum("2万关注");
        sPerson.setInquiryNum("4万提问");

        TopicPerson person = new TopicPerson();
        person.setDomain("家具");
        person.setName("张伟强");
        person.setTitle("设计总监");
        person.setIntroduce("设计来源于生活 生活因设计而改变");
        person.setHeadImgUrl("http://storage.shjhome.com/designer/avatar/70199b588a6d4aedae3698f5f711eb63.jpg");
        person.setFollowNum("1万关注");
        person.setInquiryNum("2365万提问");
        person.setIntroduceImgUrl("http://storage.shjhome.com/case/homedisplay/dir/2038227ed829435fb006941c5edb0fde.jpg");

        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                personList.add(person);
            } else {
                personList.add(sPerson);
            }
        }
    }

    private void initDomData() {
        for (int i = 0; i < 15; i++) {
            domList.add("安卓"+i);
        }
    }

    @OnClick({R.id.themeSearchImg, R.id.arrow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.themeSearchImg:
                Toast.makeText(getContext(), getResources().getString(R.string.not_impl_function), Toast.LENGTH_SHORT).show();
                break;
            case R.id.arrow:
                arrowAction();
                break;
        }
    }

    private void arrowAction() {
        if (unfoldFlag) {
            controlArw.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand_more));
            domAdapter.setLists(domList.subList(0, 5));
            domAdapter.notifyItemRangeChanged(5, domList.size());
        } else {
            controlArw.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand_less));
            domAdapter.setLists(domList);
            domAdapter.notifyItemRangeChanged(5, domList.size());
        }
        unfoldFlag = !unfoldFlag;
    }

    class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int spaceLeft;
        private int spaceRight;
        private int spaceBottom;
        private int spaceTop;
        public SpacesItemDecoration(int spaceLeft, int spaceTop, int spaceRight, int spaceBottom) {
            this.spaceLeft = spaceLeft;
            this.spaceTop = spaceTop;
            this.spaceRight = spaceRight;
            this.spaceBottom = spaceBottom;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = spaceLeft;
            outRect.right = spaceRight;
            outRect.bottom = spaceBottom;
            if (parent.getChildPosition(view) == 0) {
                outRect.top = spaceTop;
            }
        }
    }
}
