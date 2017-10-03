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
import android.widget.Toast;

import com.abubakar.friendstracker.Model.Friend;
import com.abubakar.friendstracker.Model.FriendData;
import com.abubakar.friendstracker.Model.Meeting;
import com.abubakar.friendstracker.Model.MeetingData;
import com.abubakar.friendstracker.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddMeeting extends AppCompatActivity {

    private EditText meetingTitle;
    private EditText meetingLocation;
    private EditText meetingDate;
    private EditText meetingStartTime;
    private EditText meetingEndTime;
    private TextView attendees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);
        //Store IDS
        final ArrayList<Friend> attendeesList = new ArrayList<>();
        //List View for dialog
        final ListView listView = new ListView(this);
        AddToMeetingListAdapter adapter = new AddToMeetingListAdapter(getApplicationContext(),FriendData.getInstance().getFriendArrayList(),attendeesList);
        listView.setAdapter(adapter);
        //Attach UI
        meetingTitle = (EditText) findViewById(R.id.addMeetingTitleED);
        meetingLocation = (EditText) findViewById(R.id.addMeetingLocation);
        meetingDate = (EditText) findViewById(R.id.addMeetingDate);
        meetingStartTime = (EditText) findViewById(R.id.addMeetingStartTime);
        meetingEndTime = (EditText) findViewById(R.id.addMeetingEndTime);
        attendees = (TextView) findViewById(R.id.tv_attendees);
        Button cancelBtn = (Button) findViewById(R.id.btn_cancelNewMeetingSave);
        Button saveNewMeetingBtn = (Button) findViewById(R.id.btn_saveAddMeeting);
        Button addFriends = (Button) findViewById(R.id.btn_addFriendsToMeeting);
        //
        attendees.setText(R.string.no_friend_added_meeting);
        // Dialog : setup the alert builder
        final AlertDialog.Builder builder = new AlertDialog.Builder(AddMeeting.this);
        builder.setIcon(R.drawable.friend);
        builder.setTitle("Add friends to meeting");
        // add a checkbox list
        builder.setView(listView);
        // Done
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //Create attendees list for text view
                int counter = 1;
                String friendsAdded = "";
                for (Friend friend: FriendData.getInstance().getFriendArrayList()){
                    for (Friend f: attendeesList){
                        if (friend.getID().equalsIgnoreCase(f.getID()) && counter != attendeesList.size()){
                            friendsAdded += "- " + friend.getName() + "\n";
                            counter++;
                        }else if(friend.getID().equalsIgnoreCase(f.getID()) && counter == attendeesList.size()) {
                            friendsAdded += "- " + friend.getName();
                        }
                    }
                }
                if (friendsAdded.trim().equalsIgnoreCase("")){
                    attendees.setText(R.string.no_friend_added_meeting);
                }else{
                    attendees.setText(friendsAdded);
                }
            }
        });
        final AlertDialog alertDialog = builder.create();
        //Button Listeners
        addFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create and show the alert dialog
                alertDialog.show();
            }
        });
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
                        && !startTime.isEmpty() && !endTime.isEmpty()) {
                    //Parse start date and time
                    SimpleDateFormat dateAndTimeFormat = new SimpleDateFormat("MMM,dd,yyyy HH:mm");
                    Date startDateAndTime = null;
                    Date endDateAndTime = null;
                    try {
                        startDateAndTime = dateAndTimeFormat.parse(meetingDate.getText().toString() + " "
                                + meetingStartTime.getText().toString());
                        endDateAndTime = dateAndTimeFormat.parse(meetingDate.getText().toString() + " "
                                + meetingEndTime.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (endDateAndTime.before(startDateAndTime) || endDateAndTime.equals(startDateAndTime)) {
                        Toast.makeText(getApplicationContext(), "Please make sure meeting times are correct", Toast.LENGTH_SHORT).show();
                    } else {
                        Meeting meeting = new Meeting(title,startDateAndTime,endDateAndTime,location);
                        meeting.setMeetingAttendees(attendeesList);
                        MeetingData.getInstance().addNewMeeting(meeting);
                        Toast.makeText(getApplicationContext(), "New Meeting Added", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please fill in all of the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Time field listeners
        meetingStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCall("addMeetingStartTime", "timePicker");
            }
        });
        meetingStartTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    dialogCall("addMeetingStartTime", "timePicker");
                }
            }
        });
        meetingEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCall("addMeetingEndTime", "timePicker");
            }
        });
        meetingEndTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    dialogCall("addMeetingEndTime", "timePicker");
                }
            }
        });
        //Date Field Listeners
        meetingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCall("addMeeting", "datePicker");
            }
        });
        meetingDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    dialogCall("addMeeting", "datePicker");
                }
            }
        });
    }

    public void dialogCall(String callType, String dialogType) {
        if (dialogType.equalsIgnoreCase("timePicker")) {
            DialogFragment newFragment = new TimeDialog();
            Bundle bundle = new Bundle();
            bundle.putString("callType", callType);
            newFragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            newFragment.show(fragmentManager, dialogType);
        } else {
            DialogFragment newFragment = new DateDialog();
            Bundle bundle = new Bundle();
            bundle.putString("callType", callType);
            newFragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            newFragment.show(fragmentManager, "datePicker");
        }
    }
}
