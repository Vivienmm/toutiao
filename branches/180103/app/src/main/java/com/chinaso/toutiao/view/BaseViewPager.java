package com.chinaso.toutiao.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 自定义viewpapger
 */
public class BaseViewPager extends ViewPager {
	private boolean scrollable = true;

	public BaseViewPager(Context context) {
		super(context);
	}

	public BaseViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 设置viewpager是否可以滚动
	 *
	 * @param enable
	 */
	public void setScrollable(boolean enable) {
		this.scrollable = enable;
	}


	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		return !scrollable || super.onInterceptTouchEvent(event);
	}
}