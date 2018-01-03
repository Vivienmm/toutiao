package com.chinaso.toutiao.view.calendardecorator;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.chinaso.toutiao.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

/**
 * Use a custom selector
 */
public class MySelectorDecorator implements DayViewDecorator {
    private CalendarDay date;
    private final Drawable drawable;

    public MySelectorDecorator(Activity context) {
        drawable = context.getResources().getDrawable(R.drawable.calendar_bg);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return true;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
    }
}
