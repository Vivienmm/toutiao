package com.chinaso.toutiao.view.searchresult;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

import com.chinaso.toutiao.view.BaseWebView;


public class NotifyingWebView extends BaseWebView {

    private OnScrollChangedListener mOnScrollChangedListener;
    private OnTouchEventListener mOnTouchEventListener;
//	private int lastY=0;
//	private enum MoveStatus{
//		Default,MoveUp, MoveDown
//	}
//	private final static int DISTANCE=68*3;
//	private MoveStatus ms=MoveStatus.Default;

    public NotifyingWebView(Context context) {
        super(context);
    }

    public NotifyingWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NotifyingWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnScrollChangedListener(OnScrollChangedListener listener) {
        mOnScrollChangedListener = listener;
    }

    public void setOnTouchEventListener(OnTouchEventListener listener) {
        mOnTouchEventListener = listener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangedListener != null /*&& ms!=MoveStatus.Default*/) {
            mOnScrollChangedListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }

    public interface OnScrollChangedListener {
        void onScrollChanged(WebView who, int l, int t, int oldl, int oldt);
    }

    public interface OnTouchEventListener {
        void onTouchEvent(MotionEvent event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (mOnTouchEventListener != null) {
            mOnTouchEventListener.onTouchEvent(event);
        }
//		switch (event.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			lastY=(int) event.getY();
//			break;
//		case MotionEvent.ACTION_MOVE:
//			int y=(int) event.getY();
//			if(y-lastY>DISTANCE){
//				ms=MoveStatus.MoveDown;
//			}else if(lastY-y>DISTANCE){
//				ms=MoveStatus.MoveUp;
//			}else{
//				ms=MoveStatus.Default;
//			}
//			break;
//
//		default:
//			break;
//		}
        return super.onTouchEvent(event);
    }
}
