package com.example.henu.criminalintent.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.util.Date;

/**
 * Created by hppc on 2017/7/5.
 */

public class TimePickerFragment extends DialogFragment {
    private static final String ARG_TIME = "time";
    //传递的日期数据
    public static final String EXTRA_TIME ="com.example.henu.criminalintent.fragment.time";

    private TimePicker mTimePicker;
    //构造TimePickerFragment的方法
    public static TimePickerFragment newInstance(Date time)
    {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME,time);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Date time = (Date)getArguments().getSerializable(ARG_TIME);
        return super.onCreateDialog(savedInstanceState);
    }
}
