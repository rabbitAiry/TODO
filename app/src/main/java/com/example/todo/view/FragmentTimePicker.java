package com.example.todo.view;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class FragmentTimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private TimePickerListener listener;
    public FragmentTimePicker(TimePickerListener listener) {
        this.listener = listener;
    }

    public interface TimePickerListener {
        void onTimeChosen(String time);
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    // 24hour
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        listener.onTimeChosen(hourOfDay+":"+minute);
    }
}
