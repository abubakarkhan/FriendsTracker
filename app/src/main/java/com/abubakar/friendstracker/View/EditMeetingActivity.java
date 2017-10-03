package com.abubakar.friendstracker.View;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.abubakar.friendstracker.Controller.ManageMeeting;
import com.abubakar.friendstracker.Model.Friend;
import com.abubakar.friendstracker.Model.FriendData;
import com.abubakar.friendstracker.Model.Meeting;
import com.abubakar.friendstracker.Model.MeetingData;
import com.abubakar.friendstracker.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class EditMeetingActivity extends AppCompatActivity {

    private EditText editTitle;
    private EditText editLocation;
    private EditText editDate;
    private EditText editStartTime;
    private EditText editEndTime;
    private TextView attendees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meeting);
        //Get meeting id to load
        Bundle bundle = getIntent().getExtras();
        final String meetingID = bundle.getString("id");
        final Meeting meeting = MeetingData.getInstance().getMeetingByID(meetingID);
        //Store IDS
        final ArrayList<Friend> attendeesList = new ArrayList<>();
        //List View for dialog
        final ListView listView = new ListView(this);
        AddToMeetingListAdapter adapter = new AddToMeetingListAdapter(getApplicationContext(),
                FriendData.getInstance().getFriendArrayList(),meeting.getMeetingAttendees());
        listView.setAdapter(adapter);
        //Attach UI
        Button saveEdit = (Button) findViewById(R.id.btn_saveAddMeeting);
        Button cancelEdit = (Button) findViewById(R.id.btn_cancelEditMeetingSave);
        Button manageAttendees = (Button) findViewById(R.id.btn_addFriendsToMeeting_edit);
        editTitle = (EditText) findViewById(R.id.editMeetingTitleED);
        editLocation = (EditText) findViewById(R.id.editMeetingLocation);
        editDate = (EditText) findViewById(R.id.editMeetingDate);
        editStartTime = (EditText) findViewById(R.id.editMeetingStartTime);
        editEndTime = (EditText) findViewById(R.id.editMeetingEndTime);
        attendees = (TextView) findViewById(R.id.tv_attendeesEdit);
        //Fill up fields with current information
        DateFormat dateFormat = new SimpleDateFormat("MMM,dd,yyyy");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        editTitle.setText(meeting.getTitle());
        editLocation.setText(meeting.getLocation());
        editDate.setText(dateFormat.format(meeting.getStartTime()));
        editStartTime.setText(timeFormat.format(meeting.getStartTime()));
        editEndTime.setText(timeFormat.format(meeting.getEndTime()));
        ManageMeeting.getInstance().displayAttendees(meeting.getMeetingAttendees(),attendees);
        // Dialog : setup the alert builder
        final AlertDialog.Builder builder = new AlertDialog.Builder(EditMeetingActivity.this);
        builder.setIcon(R.drawable.friend);
        builder.setTitle("Add friends to meeting");
        // add a checkbox list
        builder.setView(listView);
        // Done
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ManageMeeting.getInstance().displayAttendees(meeting.getMeetingAttendees(),attendees);
            }
        });
        final AlertDialog alertDialog = builder.create();
        //Button Listeners
        manageAttendees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();

            }
        });
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
                boolean valid = ManageMeeting.getInstance().saveMeetingChanges(title,location,date,startTime,endTime,
                        meeting,editDate,editStartTime,editEndTime,meetingID,getApplicationContext(),meeting.getMeetingAttendees());
                //Save Changes
                if (valid){finish();}
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
