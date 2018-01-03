package com.chinaso.toutiao.mvp.ui.module;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.chinaso.toutiao.mvp.data.NewsChannelItem;
import com.chinaso.toutiao.mvp.ui.adapter.RecyclerDragAdapter;

import java.util.List;

import static android.content.Context.VIBRATOR_SERVICE;

public class DragTouchHelper extends ItemTouchHelper.Callback {

    private List<NewsChannelItem> mData;
    private Context mContext;
    private RecyclerDragAdapter mAdapter;
    private Boolean isChange = false;

    public DragTouchHelper(Context context, List<NewsChannelItem> list, RecyclerDragAdapter adapter) {
        mContext = context;
        mData = list;
        mAdapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int dragFlags;
        final int swipeFlags;
        int position = viewHolder.getAdapterPosition();
        if (!mData.get(position).getLock()) {
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                swipeFlags = 0;
            } else {
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            }

            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            return makeMovementFlags(0, 0);
        }


    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        if (!mData.get(fromPosition).getLock() && !mData.get(toPosition).getLock()) {
            isChange = true;
            Log.i("onMove-log", "onMove ");
            mData.add(toPosition, mData.remove(fromPosition));
            Log.i("onMove-log", "onMove1 ");
            mAdapter.notifyItemMoved(fromPosition, toPosition);
            Log.i("onMove-log", "onMove1 ");
        }

        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        mAdapter.notifyItemRemoved(position);
        mData.remove(position);
    }

    //是否可以拖拽移动位置
    @Override
    public boolean isLongPressDragEnabled() {

        return true;
    }


    //当长按的时候调用
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            viewHolder.itemView.setEnabled(true);
        }
        Vibrator vibrator = (Vibrator) mContext.getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(70);
        super.onSelectedChanged(viewHolder, actionState);
    }

    //当手指松开的时候调用
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setEnabled(false);
        Log.i("log", "clearView: ");

    }

    public Boolean isChange() {
        return isChange;
    }
}
