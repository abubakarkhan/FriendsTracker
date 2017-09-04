package com.abubakar.friendstracker.Controller;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.abubakar.friendstracker.Model.Meeting;
import com.abubakar.friendstracker.Model.MeetingData;
import com.abubakar.friendstracker.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditMeetingActivity extends AppCompatActivity {

    private EditText editTitle;
    private EditText editLocation;
    private EditText editDate;
    private EditText editStartTime;
    private EditText editEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meeting);
        //Get meeting id to load
        Bundle bundle = getIntent().getExtras();
        final String meetingID = bundle.getString("id");

        //Attach UI
        Button saveEdit = (Button) findViewById(R.id.btn_saveAddMeeting);
        Button cancelEdit = (Button) findViewById(R.id.btn_cancelEditMeetingSave);
        editTitle = (EditText) findViewById(R.id.editMeetingTitleED);
        editLocation = (EditText) findViewById(R.id.editMeetingLocation);
        editDate = (EditText) findViewById(R.id.editMeetingDate);
        editStartTime = (EditText) findViewById(R.id.editMeetingStartTime);
        editEndTime = (EditText) findViewById(R.id.editMeetingEndTime);
        //Fill up fields with current information
        DateFormat dateFormat = new SimpleDateFormat("MMM,dd,yyyy");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        final Meeting meeting = MeetingData.getInstance().getMeetingByID(meetingID);
        editTitle.setText(meeting.getTitle());
        editLocation.setText(meeting.getLocation());
        editDate.setText(dateFormat.format(meeting.getStartTime()));
        editStartTime.setText(timeFormat.format(meeting.getStartTime()));
        editEndTime.setText(timeFormat.format(meeting.getEndTime()));
        //Button Listeners
        cancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        saveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTitle.getText().toString().trim();
                String location = editLocation.getText().toString().trim();
                String date = editDate.getText().toString().trim();
                String startTime = editStartTime.getText().toString().trim();
                String endTime = editEndTime.getText().toString().trim();

                if(!title.isEmpty() && !location.isEmpty() && !date.isEmpty() && !startTime.isEmpty()
                        && !endTime.isEmpty()){
                    //Parse start date and time
                    SimpleDateFormat dateAndTimeFormat = new SimpleDateFormat("MMM,dd,yyyy HH:mm");
                    Date startDateAndTime = meeting.getStartTime();
                    Date endDateAndTime = meeting.getEndTime();
                    try{
                        startDateAndTime = dateAndTimeFormat.parse(editDate.getText().toString()+" "+ editStartTime.getText().toString());
                        endDateAndTime = dateAndTimeFormat.parse(editDate.getText().toString()+" "+editEndTime.getText().toString());
                    }catch (ParseException e){
                        e.printStackTrace();
                    }
                    //Get index of editable item and save changes
                    int positionIndex = MeetingData.getInstance().getMeetingListIndex(meetingID);
                    MeetingData.getInstance().getMeetingArrayList().get(positionIndex).setTitle(editTitle.getText().toString());
                    MeetingData.getInstance().getMeetingArrayList().get(positionIndex).setLocation(editLocation.getText().toString());
                    if(endDateAndTime.before(startDateAndTime) || endDateAndTime.equals(startDateAndTime)){
                        Toast.makeText(getApplicationContext(), "Please make sure meeting times are correct", Toast.LENGTH_SHORT).show();
                    }else {
                        MeetingData.getInstance().getMeetingArrayList().get(positionIndex).setStartTime(startDateAndTime);
                        MeetingData.getInstance().getMeetingArrayList().get(positionIndex).setEndTime(endDateAndTime);
                        Toast.makeText(getApplicationContext(), "Changes Saved", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Please fill in all of the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Date edit text
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCall("editMeeting","datePicker");
            }
        });
        editDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    dialogCall("editMeeting","datePicker");
                }
            }
        });
        editStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCall("editMeetingStartTime", "timePicker");
            }
        });
        editStartTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    dialogCall("editMeetingStartTime", "timePicker");
                }
            }
        });
        editEndTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    dialogCall("editMeetingEndTime", "timePicker");
                }
            }
        });
        editEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCall("editMeetingEndTime", "timePicker");
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
            newFragment.show(fragmentManager,dialogType);
        }
    }
}
