package com.chinaso.toutiao.mvp.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import com.chinaso.toutiao.R;

/**
 * Created by Administrator on 17-3-10.
 */

public class SimpleCalendarDialogFragment extends AppCompatDialogFragment implements /*OnDateSelectedListener*/ CalendarView.OnDateChangeListener{
    private TextView textView;
    private OnSelectedDayChangeListener mListener;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_calendar, null);
        textView = (TextView) view.findViewById(R.id.showDateTV);
        CalendarView widget = (CalendarView) view.findViewById(R.id.calendarView);
        widget.setOnDateChangeListener(this);
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        String text = year + "年" + (month + 1) + "月" + dayOfMonth;
        textView.setText(text);
        mListener.onSelectedDay(text);
    }

    public void setOnSelectedLisnter(OnSelectedDayChangeListener mListener) {
        this.mListener = mListener;
    }

    public interface OnSelectedDayChangeListener {
        void onSelectedDay(String dataStr);
    }
}
