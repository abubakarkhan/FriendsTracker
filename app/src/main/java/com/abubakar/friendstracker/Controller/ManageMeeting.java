package com.abubakar.friendstracker.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abubakar.friendstracker.Model.Friend;
import com.abubakar.friendstracker.Model.FriendData;
import com.abubakar.friendstracker.Model.Meeting;
import com.abubakar.friendstracker.Model.MeetingData;
import com.abubakar.friendstracker.R;
import com.abubakar.friendstracker.View.EditMeetingActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ManageMeeting {
    private static final ManageMeeting ourInstance = new ManageMeeting();

    public static ManageMeeting getInstance() {
        return ourInstance;
    }
    public void populateMeetingDialog(int i, AlertDialog.Builder mBuilder){
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        mBuilder.setMessage(MeetingData.getInstance().getMeetingArrayList().get(i).getTitle() +"\n"
                + "Location: "+MeetingData.getInstance().getMeetingArrayList().get(i).getLocation() + "\n"
                + "Date: "+ dateFormat.format(MeetingData.getInstance().getMeetingArrayList().get(i).getStartTime())
                +"\n" +"Start Time: "+ timeFormat.format(MeetingData.getInstance().getMeetingArrayList().get(i).getStartTime())
                +"\n" + "End Time: "+ timeFormat.format(MeetingData.getInstance().getMeetingArrayList().get(i).getEndTime()));
    }
    public void editMeeting(Context context, int positionToRemove, Activity activity, DialogInterface dialogInterface){
        Intent intent = new Intent(context, EditMeetingActivity.class);
        intent.putExtra("id", MeetingData.getInstance().getMeetingArrayList().get(positionToRemove).getMeetingID());
        activity.startActivity(intent);
        dialogInterface.dismiss();
    }
    public void displayAttendees(ArrayList<Friend> attendeesList, TextView attendees){
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
            friendsAdded = friendsAdded.trim();
            attendees.setText(friendsAdded);
        }
    }
    public boolean saveMeeting(String title, String location, String date, String startTime, String endTime,
                               EditText meetingDate, EditText meetingStartTime, EditText meetingEndTime, Context context,
                               ArrayList<Friend> attendeesList){
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
                Toast.makeText(context, "Please make sure meeting times are correct", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                Meeting meeting = new Meeting(title,startDateAndTime,endDateAndTime,location);
                meeting.setMeetingAttendees(attendeesList);
                MeetingData.getInstance().addNewMeeting(meeting);
                Toast.makeText(context, "New Meeting Added", Toast.LENGTH_SHORT).show();
                return true;
            }
        } else {
            Toast.makeText(context, "Please fill in all of the fields", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public boolean saveMeetingChanges(String title, String location, String date, String startTime,String endTime,
                                      Meeting meeting, EditText editDate, EditText editStartTime,EditText editEndTime,
                                      String meetingID, Context context,ArrayList<Friend> attendeesList){
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
            MeetingData.getInstance().getMeetingArrayList().get(positionIndex).setTitle(title);
            MeetingData.getInstance().getMeetingArrayList().get(positionIndex).setLocation(location);
            if(endDateAndTime.before(startDateAndTime) || endDateAndTime.equals(startDateAndTime)){
                Toast.makeText(context, "Please make sure meeting times are correct", Toast.LENGTH_SHORT).show();
                return false;
            }else {
                MeetingData.getInstance().getMeetingArrayList().get(positionIndex).setStartTime(startDateAndTime);
                MeetingData.getInstance().getMeetingArrayList().get(positionIndex).setEndTime(endDateAndTime);
                MeetingData.getInstance().getMeetingArrayList().get(positionIndex).setMeetingAttendees(attendeesList);
                Toast.makeText(context, "Changes Saved", Toast.LENGTH_SHORT).show();
                return true;
            }
        }else {
            Toast.makeText(context, "Please fill in all of the fields", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private ManageMeeting() {
    }
}
