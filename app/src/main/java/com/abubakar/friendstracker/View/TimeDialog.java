package com.abubakar.friendstracker.View;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.TimePicker;

import com.abubakar.friendstracker.R;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TimeDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private String callType;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        callType = getArguments().getString("callType");
        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }
    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        Time t = new Time(i,i1,0);//seconds by default set to zero
        Format formatter;
        formatter = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        String time = formatter.format(t);
        if (callType.equalsIgnoreCase("addMeetingStartTime")){
            EditText addStartTime = getActivity().findViewById(R.id.addMeetingStartTime);
            addStartTime.setText(time);
        }else if(callType.equalsIgnoreCase("addMeetingEndTime")){
            EditText addEndTime = getActivity().findViewById(R.id.addMeetingEndTime);
            addEndTime.setText(time);
        }else if(callType.equalsIgnoreCase("editMeetingStartTime")){
            EditText editStartTime = getActivity().findViewById(R.id.editMeetingStartTime);
            editStartTime.setText(time);
        }else if (callType.equalsIgnoreCase("editMeetingEndTime")){
            EditText editEndTime = getActivity().findViewById(R.id.editMeetingEndTime);
            editEndTime.setText(time);
        }

    }
}
