package com.abubakar.friendstracker.Controller;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.abubakar.friendstracker.Model.MeetingData;
import com.abubakar.friendstracker.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddMeeting extends AppCompatActivity {

    private EditText meetingTitle;
    private EditText meetingLocation;
    private EditText meetingDate;
    private EditText meetingStartTime;
    private EditText meetingEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);
        //Attach UI
        meetingTitle = (EditText) findViewById(R.id.addMeetingTitleED);
        meetingLocation = (EditText) findViewById(R.id.addMeetingLocation);
        meetingDate = (EditText) findViewById(R.id.addMeetingDate);
        meetingStartTime = (EditText) findViewById(R.id.addMeetingStartTime);
        meetingEndTime = (EditText) findViewById(R.id.addMeetingEndTime);
        Button cancelBtn = (Button) findViewById(R.id.btn_cancelNewMeetingSave);
        Button saveNewMeetingBtn = (Button) findViewById(R.id.btn_saveAddMeeting);
        //Button Listeners
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        saveNewMeetingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = meetingTitle.getText().toString().trim();
                String location = meetingLocation.getText().toString().trim();
                String date = meetingDate.getText().toString().trim();
                String startTime = meetingStartTime.getText().toString().trim();
                String endTime = meetingEndTime.getText().toString().trim();

                if (!title.isEmpty() && !location.isEmpty() && !date.isEmpty()
                        && !startTime.isEmpty() && !endTime.isEmpty()){
                    //Parse start date and time
                    SimpleDateFormat dateAndTimeFormat = new SimpleDateFormat("MMM,dd,yyyy HH:mm");
                    Date startDateAndTime = null;
                    Date endDateAndTime = null;
                    try{
                        startDateAndTime = dateAndTimeFormat.parse(meetingDate.getText().toString() + " "
                        + meetingStartTime.getText().toString());
                        endDateAndTime = dateAndTimeFormat.parse(meetingDate.getText().toString() + " "
                        + meetingEndTime.getText().toString());
                    }catch (ParseException e){
                        e.printStackTrace();
                    }
                    if (endDateAndTime.before(startDateAndTime) || endDateAndTime.equals(startDateAndTime)){
                        Toast.makeText(getApplicationContext(), "Please make sure meeting times are correct", Toast.LENGTH_SHORT).show();
                    }else {
                        MeetingData.getInstance().addNewMeeting(title,startDateAndTime,endDateAndTime,location);
                        Toast.makeText(getApplicationContext(), "New Meeting Added", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }else {
                    Toast.makeText(getApplicationContext(), "Please fill in all of the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Time field listeners
        meetingStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCall("addMeetingStartTime","timePicker");
            }
        });
        meetingStartTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    dialogCall("addMeetingStartTime","timePicker");
                }
            }
        });
        meetingEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCall("addMeetingEndTime","timePicker");
            }
        });
        meetingEndTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    dialogCall("addMeetingEndTime","timePicker");
                }
            }
        });
        //Date Field Listeners
        meetingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCall("addMeeting","datePicker");
            }
        });
        meetingDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    dialogCall("addMeeting","datePicker");
                }
            }
        });
    }
    public void dialogCall(String callType,String dialogType){
        if(dialogType.equalsIgnoreCase("timePicker")){
            DialogFragment newFragment = new TimeDialog();
            Bundle bundle = new Bundle();
            bundle.putString("callType",callType);
            newFragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            newFragment.show(fragmentManager,dialogType);
        }else {
            DialogFragment newFragment = new DateDialog();
            Bundle bundle = new Bundle();
            bundle.putString("callType",callType);
            newFragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            newFragment.show(fragmentManager,"datePicker");
        }
    }
}
