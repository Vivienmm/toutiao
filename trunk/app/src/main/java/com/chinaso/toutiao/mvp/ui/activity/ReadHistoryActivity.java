package com.chinaso.toutiao.mvp.ui.activity;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.mvp.data.readhistory.ReadHistoryEntity;
import com.chinaso.toutiao.mvp.data.readhistory.ReadHistoryManageDao;
import com.chinaso.toutiao.mvp.ui.activity.base.BaseActivity;
import com.chinaso.toutiao.mvp.ui.adapter.NewsHistoryAdapter;
import com.chinaso.toutiao.view.CustomActionBar;
import com.chinaso.toutiao.view.RecycleViewDivider;
import com.chinaso.toutiao.view.calendardecorator.EventDecorator;
import com.chinaso.toutiao.view.calendardecorator.MySelectorDecorator;
import com.chinaso.toutiao.view.calendardecorator.OneDayDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;

public class ReadHistoryActivity extends BaseActivity implements OnDateSelectedListener {
    @BindView(R.id.readHistoryBar)
    CustomActionBar readHistoryBar;
    @BindView(R.id.readHistoryCalendar)
    MaterialCalendarView readHistoryCalendar;
    @BindView(R.id.weekModeImg)
    ImageView weekModeImg;
    @BindView(R.id.historyRV)
    RecyclerView historyRV;

    private boolean weekModeFlag = true;
    private static final DateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    private ReadHistoryManageDao readHistoryManageDao;
    private NewsHistoryAdapter adapter = new NewsHistoryAdapter();

    @Override
    public int getLayoutId() {
        return R.layout.activity_read_history;
    }

    @Override
    public void initViews() {
        readHistoryBar.setTitleView("阅读日历");
        readHistoryBar.setLeftViewImg(R.mipmap.actionbar_back);
        readHistoryBar.setOnClickListener(new CustomActionBar.ActionBarLeftClick() {
            @Override
            public void leftViewClick() {
                ReadHistoryActivity.this.finish();
            }
        });

        readHistoryManageDao = new ReadHistoryManageDao();

        initCalendar();

        initRV();

    }

    private void initCalendar() {
        readHistoryCalendar.setOnDateChangedListener(this);
        readHistoryCalendar.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        Calendar instance = Calendar.getInstance();
        readHistoryCalendar.setSelectedDate(instance.getTime());
        readHistoryCalendar.addDecorators(new MySelectorDecorator(this), oneDayDecorator);
        readHistoryCalendar.setTileHeightDp(36);
        initCalendarData();
    }

    private void initCalendarData() {

        Observable.create(new Observable.OnSubscribe<ArrayList<CalendarDay>>() {

            @Override
            public void call(Subscriber<? super ArrayList<CalendarDay>> subscriber) {

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, -2);
                ArrayList<CalendarDay> dates = new ArrayList<>();
                List<String> historyDates = readHistoryManageDao.getAllDate();
                Date date;
                CalendarDay day;
                for (String itemDate : historyDates) {
                    try {
                        date = FORMATTER.parse(itemDate);
                        day = CalendarDay.from(date);
                        dates.add(day);
                        calendar.add(Calendar.DATE, 5);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                subscriber.onNext(dates);
            }
        }).subscribe(new Observer<ArrayList<CalendarDay>>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ArrayList<CalendarDay> calendarDays) {
                if (isFinishing()) {
                    return;
                }
                readHistoryCalendar.addDecorator(new EventDecorator(Color.RED, calendarDays));
            }
        });
    }

    private void initRV() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        historyRV.setLayoutManager(manager);
        historyRV.addItemDecoration(new RecycleViewDivider(this, RecycleViewDivider.VERTICAL_LIST));
        historyRV.setAdapter(adapter);
        Date date = new Date(System.currentTimeMillis());
        String result = FORMATTER.format(date);
        List<ReadHistoryEntity> datas = readHistoryManageDao.getSelectedHistory(result);
        adapter.setLists(datas);
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        oneDayDecorator.setDate(date.getDate());
        String result = FORMATTER.format(date.getDate());
        List<ReadHistoryEntity> mList = readHistoryManageDao.getSelectedHistory(result);
        adapter.setLists(mList);
        widget.invalidateDecorators();
    }

    @OnClick(R.id.weekModeImg)
    public void onClick() {
        readHistoryCalendar.state().edit().setCalendarDisplayMode(weekModeFlag ? CalendarMode.WEEKS : CalendarMode.MONTHS).commit();
        weekModeImg.setImageResource(weekModeFlag ? R.drawable.ic_expand_more : R.drawable.ic_expand_less);
        weekModeFlag = !weekModeFlag;
    }
}