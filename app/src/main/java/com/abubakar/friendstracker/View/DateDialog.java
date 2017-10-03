package com.abubakar.friendstracker.View;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;

import com.abubakar.friendstracker.R;

import java.util.Date;
import java.util.Locale;

public class DateDialog extends DialogFragment implements android.app.DatePickerDialog.OnDateSetListener {

    String callType;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        //get from bundle
        callType = getArguments().getString("callType");
        if (callType.equalsIgnoreCase("addMeeting") || callType.equalsIgnoreCase("editMeeting")){
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        }
        // Create a new instance of DateDialog and return it
        return datePickerDialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        String date = new SimpleDateFormat("MMM, dd, yyyy", Locale.ENGLISH)
                .format(new Date(year-1900,month,day));
        EditText dateInput;
        if(callType.equalsIgnoreCase("addFriend")){
            dateInput = getActivity().findViewById(R.id.birthdayEditText);
            dateInput.setText(date);
        }else if (callType.equalsIgnoreCase("editFriend")){
            dateInput = getActivity().findViewById(R.id.editFriendBirthday);
            dateInput.setText(date);
        }else if (callType.equalsIgnoreCase("addMeeting")){
            dateInput = getActivity().findViewById(R.id.addMeetingDate);
            dateInput.setText(date);
        }else if (callType.equalsIgnoreCase("editMeeting")){
            dateInput = getActivity().findViewById(R.id.editMeetingDate);
            dateInput.setText(date);
        }
    }
}
