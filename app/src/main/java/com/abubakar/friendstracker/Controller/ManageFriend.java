package com.abubakar.friendstracker.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.abubakar.friendstracker.Model.FriendData;
import com.abubakar.friendstracker.Model.Meeting;
import com.abubakar.friendstracker.Model.MeetingData;
import com.abubakar.friendstracker.View.EditFriendActivity;
import com.abubakar.friendstracker.View.FriendListAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class ManageFriend {
    private static final ManageFriend ourInstance = new ManageFriend();

    public static ManageFriend getInstance() {
        return ourInstance;
    }
    public void populateDialog(final int positionToRemove,AlertDialog.Builder mBuilder){
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

    private ManageFriend() {
    }
}
