package com.example.henu.criminalintent.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.example.henu.criminalintent.R;

import java.util.Calendar;
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

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time,null);

        mTimePicker = (TimePicker)v.findViewById(R.id.dialog_time_picker);
        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(minute);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.time_picker_title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int hour = mTimePicker.getCurrentHour();
                        int minute = mTimePicker.getCurrentMinute();

                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.setTime(time);
                        calendar1.set(Calendar.HOUR_OF_DAY,hour);
                        calendar1.set(Calendar.MINUTE,minute);
                        Date time = calendar1.getTime();

                        sendResult(Activity.RESULT_OK,time);
                    }
                })
                .create();

    }
    private void sendResult(int resultCode,Date time)
    {
        if(getTargetFragment() == null)
        {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME,time);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}
