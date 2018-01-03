package com.chinaso.toutiao.mvp.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.mvp.data.collection.CollectionEntity;
import com.chinaso.toutiao.mvp.presenter.CollectionPresenter;
import com.chinaso.toutiao.mvp.presenter.impl.CollectionPresenterImpl;
import com.chinaso.toutiao.mvp.ui.activity.base.BaseActivity;
import com.chinaso.toutiao.mvp.ui.adapter.CollectionAdapter;
import com.chinaso.toutiao.mvp.view.CollectionView;
import com.chinaso.toutiao.view.CustomActionBar;
import com.chinaso.toutiao.view.swipemenulistview.SwipeMenu;
import com.chinaso.toutiao.view.swipemenulistview.SwipeMenuCreator;
import com.chinaso.toutiao.view.swipemenulistview.SwipeMenuItem;
import com.chinaso.toutiao.view.swipemenulistview.SwipeMenuListView;

import butterknife.BindView;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;

public class CollectionActivity extends BaseActivity implements CollectionView{

    CollectionPresenter collectionPresenter = new CollectionPresenterImpl();
    CollectionAdapter adapter;

    @BindView(R.id.collection_listview)
    SwipeMenuListView swipeMenuListView;
    @BindView(R.id.actionbar)
    CustomActionBar customActionBar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_collection;
    }

    @Override
    public void initViews() {
        collectionPresenter.attachView(this); //注意前后顺序
        collectionPresenter.onCreate(); //得到所有数据并初始化view

    }

    @Override
    public void initTopBar() {

        CustomActionBar bar = (CustomActionBar) findViewById(R.id.actionbar);
        bar.setTitleView("我的收藏");
        bar.setLeftViewImg(R.mipmap.actionbar_back);
        bar.setRightTV("清空");
        bar.setOnClickListener(new CustomActionBar.ActionBarInterface() {

            @Override
            public void leftViewClick() {
                CollectionActivity.this.finish();
            }

            @Override
            public void rightImgClick() {

            }

            @Override
            public void rightTVClick() {
                showDelAllDialog(CollectionActivity.this);
            }

            @Override
            public void openNewsClick() {

            }
        });
    }

    @Override
    public void initSwipeMenuListView() {

        adapter = new CollectionAdapter(this,  collectionPresenter.getAllCollections());

        swipeMenuListView.setAdapter(adapter);
    }

    @Override
    public void initMenu() {
        SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem editItem = new SwipeMenuItem(CollectionActivity.this);
                editItem.setBackground(new ColorDrawable(Color.rgb(0xA0, 0xA0,
                        0xA0)));
                editItem.setWidth(dp2px(90));
                editItem.setTitle("编辑");
                editItem.setTitleSize(18);
                editItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(editItem);

                SwipeMenuItem deleteItem = new SwipeMenuItem(CollectionActivity.this);
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                deleteItem.setWidth(dp2px(90));
                deleteItem.setTitle("删除");
                deleteItem.setTitleSize(18);
                deleteItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(deleteItem);
            }
        };
        swipeMenuListView.setMenuCreator(swipeMenuCreator);
        swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position, SwipeMenu menu, int index) {

                        //数据库删除后view未更新等情况处理
                        if (collectionPresenter.getAllCollections().size() > position) {
                            switch (index) {
                                case 0:
                                    showEditDialog(position,collectionPresenter.getSelectedCollection(position).getTitle(), collectionPresenter.getSelectedCollection(position).getUrl());
                                    break;
                                case 1:
                                    collectionPresenter.deleteCollection(position);
                                    break;
                            }
                        } else {

                        }
                    }
                });

    }

    @Override
    public void gotoSelectedCollection(CollectionEntity collectionEntity) {

    }

    @OnItemClick(R.id.collection_listview)
    void onItemClick(int position) {

        CollectionEntity entity = collectionPresenter.getSelectedCollection(position);
        if (entity != null) {
            int type = entity.getType();
            Intent intent = null;
            if (type == 2) {
                intent = new Intent(CollectionActivity.this, VerticalDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("newsShowUrl", entity.getUrl());
                intent.putExtras(bundle);
            }

            startActivity(intent);
        }
    }

    @OnItemLongClick(R.id.collection_listview)
    boolean onItemLongClick(int pos) {
        CollectionEntity entity = collectionPresenter.getSelectedCollection(pos);
        if (entity != null) {
            showEditDialog(pos, collectionPresenter.getSelectedCollection(pos).getTitle(), collectionPresenter.getSelectedCollection(pos).getUrl());
        }
        return true;
    }


    private void showEditDialog(final int pos, String title, String url) {

        View editview = LayoutInflater.from(CollectionActivity.this).inflate(
                R.layout.dialog_collection_edit, null);
        final EditText titleEditTxt = (EditText) editview
                .findViewById(R.id.editName);
        final EditText urlEditTxt = (EditText) editview.findViewById(R.id.editUrl);
        titleEditTxt.setText(title);
        urlEditTxt.setText(url);
        new AlertDialog.Builder(CollectionActivity.this).setView(editview)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        CollectionEntity collectionEntity = collectionPresenter.getSelectedCollection(pos);
//                        collectionEntity.setUrl(urlEditTxt.getText().toString());
//                        collectionEntity.setTitle(titleEditTxt.getText().toString());
//                        collectionPresenter.editCollection(collectionEntity);
                        collectionPresenter.editCollection(pos, titleEditTxt.getText().toString(), urlEditTxt.getText().toString());

                    }
                }).setNegativeButton("取消", null).show();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private void showDelAllDialog(final Activity currentAct) {
        Dialog dialog = new AlertDialog.Builder(currentAct)
                .setTitle(R.string.action_confirm_clear)
                .setIcon(0)
                .setPositiveButton(R.string.action_clear,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                collectionPresenter.clearAllCollections();
                            }
                        })
                .setNeutralButton(R.string.action_cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                            }
                        }).create();
        dialog.show();
    }


    @Override
    public void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorMsg(String message) {

    }

    @Override
    public void notifyAdapter() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            adapter.notifyDataSetInvalidated();
        }
    }
}
