package com.abubakar.friendstracker.View;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.abubakar.friendstracker.Model.Friend;
import com.abubakar.friendstracker.Model.Meeting;
import com.abubakar.friendstracker.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MeetingListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Meeting> meetingArrayList;

    public MeetingListAdapter(Context context, ArrayList<Meeting> meetingArrayList) {
        this.context = context;
        this.meetingArrayList = meetingArrayList;
    }

    @Override
    public int getCount() {
        return meetingArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return meetingArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //Meeting list custom view
        View v = View.inflate(context, R.layout.meeting_list_row, null);
        //Attach UI labels
        TextView meetingTitle = v.findViewById(R.id.meetingTitleRowTV);
        TextView meetingLocation = v.findViewById(R.id.locationRowTV);
        TextView meetingDate = v.findViewById(R.id.meetingDateRowTV);
        TextView startTime = v.findViewById(R.id.startTimeRowTV);
        TextView endTime = v.findViewById(R.id.endTimeRowTV);
        TextView attendeesList = v.findViewById(R.id.tv_attendeesListString);

        DateFormat dateFormat = new SimpleDateFormat("MMM,dd,yyyy");
        DateFormat timeFormat = new SimpleDateFormat("hh:mm");

        String friendsAttending = "";

        meetingTitle.setText(meetingArrayList.get(i).getTitle());
        meetingLocation.setText(meetingArrayList.get(i).getLocation());
        meetingDate.setText(dateFormat.format(meetingArrayList.get(i).getStartTime()));
        startTime.setText(timeFormat.format(meetingArrayList.get(i).getStartTime()));
        endTime.setText(timeFormat.format(meetingArrayList.get(i).getEndTime()));
        for (Friend friend: meetingArrayList.get(i).getMeetingAttendees()){
            friendsAttending += "- " + friend.getName()+ "\n";
        }
        if (friendsAttending.trim().equals("")){
            attendeesList.setText(R.string.no_friend_added_meeting);
        }else {
            friendsAttending = friendsAttending.trim();
            attendeesList.setText(friendsAttending);
        }
        v.setTag(meetingArrayList.get(i).getMeetingID());
        return v;
    }

    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
