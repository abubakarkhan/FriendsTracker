package com.abubakar.friendstracker.Controller;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.abubakar.friendstracker.Model.Friend;
import com.abubakar.friendstracker.R;

import java.util.ArrayList;

public class AddToMeetingListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Friend> friendArrayList;
    private ArrayList<Friend> attendingArrayList;

    public AddToMeetingListAdapter(Context context, ArrayList<Friend> friendArrayList, ArrayList<Friend> attendingArrayList) {
        this.context = context;
        this.friendArrayList = friendArrayList;
        this.attendingArrayList = attendingArrayList;
    }

    @Override
    public int getCount() {
        return friendArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return friendArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.add_friend_to_meeting_list_row, null);
        TextView friendName = v.findViewById(R.id.tv_friend_row_name);
        CheckBox cb_attending = v.findViewById(R.id.cb_addToMeeting);
        friendName.setText(friendArrayList.get(i).getName());
        for(Friend friend: attendingArrayList){
            if (friendArrayList.get(i).getID().equalsIgnoreCase(friend.getID())){
                cb_attending.setChecked(true);
                break;
            }else {
                cb_attending.setChecked(false);
            }
        }
        final int index = i;
        cb_attending.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    attendingArrayList.add(friendArrayList.get(index));
                    System.out.println("Added: " + friendArrayList.get(index).getID());
                }else {
                    attendingArrayList.remove(friendArrayList.get(index));
                    System.out.println("Removed: " + friendArrayList.get(index).getID());
                }
            }
        });
        return v;
    }

}
