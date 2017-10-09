package com.abubakar.friendstracker.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.Toast;

import com.abubakar.friendstracker.Model.Friend;
import com.abubakar.friendstracker.Model.FriendData;
import com.abubakar.friendstracker.Model.Meeting;
import com.abubakar.friendstracker.Model.MeetingData;
import com.abubakar.friendstracker.View.EditFriendActivity;
import com.abubakar.friendstracker.View.FriendListAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ManageFriend {
    private static final ManageFriend ourInstance = new ManageFriend();

    public static ManageFriend getInstance() {
        return ourInstance;
    }
    public void populateFriendDialog(final int positionToRemove, AlertDialog.Builder mBuilder){
        int i = positionToRemove;
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        //Null Email and Birthday Message
        if (FriendData.getInstance().getFriendArrayList().get(i).getBirthday() == null){
            mBuilder.setMessage(FriendData.getInstance().getFriendArrayList().get(i).getName() + "\n"
                    + FriendData.getInstance().getFriendArrayList().get(i).getEmail() + "\n"
                    + "DOB N/A");
        }else {
            mBuilder.setMessage(FriendData.getInstance().getFriendArrayList().get(i).getName() + "\n"
                    + FriendData.getInstance().getFriendArrayList().get(i).getEmail() + "\n"
                    + dateFormat.format(FriendData.getInstance().getFriendArrayList().get(i).getBirthday()));
        }
    }
    public void editFriend(Context context, int positionToRemove, Activity activity,DialogInterface dialogInterface){
        Intent intent = new Intent(context, EditFriendActivity.class);
        intent.putExtra("id", FriendData.getInstance().getFriendArrayList().get(positionToRemove).getID());
        activity.startActivity(intent);
        dialogInterface.dismiss();
    }
    public void deleteFriend(int positionToRemove, FriendListAdapter adapter, Context context, DialogInterface dialogInterface){
        String friendRemovedID = FriendData.getInstance().getFriendArrayList().get(positionToRemove).getID();
        FriendData.getInstance().getFriendArrayList().remove(positionToRemove);
        adapter.notifyDataSetChanged();
        Toast.makeText(context,"Friend Deleted", Toast.LENGTH_SHORT).show();
        dialogInterface.dismiss();
        //Remove also from associated meetings
        for (Meeting meeting: MeetingData.getInstance().getMeetingArrayList()){
            meeting.removeFriendFromMeeting(friendRemovedID);
        }
    }

    public boolean saveNewFriend(String nameText, String emailText, String dobText, EditText dateOfBirth, Double lat, Double lon, Context context) {
        if (!nameText.isEmpty() && !emailText.isEmpty() && !dobText.isEmpty() && lat != null
                && lon != null) {
            // Parse Date
            SimpleDateFormat format = new SimpleDateFormat("MMM, dd, yyyy");
            Date dob = null;
            try{
                dob = format.parse(dateOfBirth.getText().toString());
            }catch (ParseException e){
                e.printStackTrace();
            }
            Friend friend = new Friend(nameText, emailText, dob, lat, lon, null);
            FriendData.getInstance().addNewFriend(friend);
            Toast.makeText(context, "Friend Added", Toast.LENGTH_SHORT).show();
            return true;
        }else {
            Toast.makeText(context, "Please fill in all of the fields", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public boolean saveEditFriendChanges(String nameText, String emailText, String dobText, Friend friend, EditText editDateOfBirth, Double lat, Double lon, String id, Context context) {
        if (!nameText.isEmpty() && !emailText.isEmpty() && !dobText.isEmpty()
                && lat != null && lon != null) {
            // Parse Date
            SimpleDateFormat format = new SimpleDateFormat("MMM, dd, yyyy");
            Date dob = friend.getBirthday();
            try{
                dob = format.parse(editDateOfBirth.getText().toString());
            }catch (ParseException e){
                e.printStackTrace();
            }
            //Get index of editable item and save changes
            int positionIndex = FriendData.getInstance().getFriendListIndex(id);
            FriendData.getInstance().getFriendArrayList().get(positionIndex).setName(nameText);
            FriendData.getInstance().getFriendArrayList().get(positionIndex).setEmail(emailText);
            if(dob == null){
                FriendData.getInstance().getFriendArrayList().get(positionIndex).setBirthday(friend.getBirthday());
            }else {
                FriendData.getInstance().getFriendArrayList().get(positionIndex).setBirthday(dob);
            }
            FriendData.getInstance().getFriendArrayList().get(positionIndex).setLat(lat);
            FriendData.getInstance().getFriendArrayList().get(positionIndex).setLon(lon);
            //Save and go back to main
            Toast.makeText(context, "Changes Saved", Toast.LENGTH_SHORT).show();
            return true;
        }else {
            Toast.makeText(context, "Please fill in all of the fields", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private ManageFriend() {
    }
}
