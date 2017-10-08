package com.abubakar.friendstracker.Model;


import android.database.Cursor;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MeetingData {

    private static final String TAG = "MeetingData";
    private static final MeetingData ourInstance = new MeetingData();
    private ArrayList<Meeting> meetingArrayList = new ArrayList<>();

    public static MeetingData getInstance() {
        return ourInstance;
    }

    private MeetingData() {
    }

    public ArrayList<Meeting> getMeetingArrayList() {
        return meetingArrayList;
    }
    public ArrayList<Meeting> sortMeetingsByDateDesc(){
        Meeting temp;
        for(int i=0; i < meetingArrayList.size(); i++){
            for(int j=0; j < meetingArrayList.size()-i-1;j++){
                if (meetingArrayList.get(j).getStartTime().before(meetingArrayList.get(j+1).getStartTime())){
                    temp = meetingArrayList.get(j);
                    meetingArrayList.set(j,meetingArrayList.get(j+1));
                    meetingArrayList.set(j+1,temp);
                }
            }
        }
        return meetingArrayList;
    }
    public ArrayList<Meeting> sortMeetingsByDateAscending(){
        Meeting temp;
        for(int i=0; i < meetingArrayList.size(); i++){
            for(int j=0; j < meetingArrayList.size()-i-1;j++){
                if (meetingArrayList.get(j).getStartTime().after(meetingArrayList.get(j+1).getStartTime())){
                    temp = meetingArrayList.get(j);
                    meetingArrayList.set(j,meetingArrayList.get(j+1));
                    meetingArrayList.set(j+1,temp);
                }
            }
        }
        return meetingArrayList;
    }
    public void addNewMeeting(Meeting meeting){
        meetingArrayList.add(meeting);
    }

    public void populateMeetingList(DatabaseHelper db) {
        meetingArrayList.clear();
        Cursor cursor = db.getAllMeetingData();
        if (cursor.getCount() == 0) {
            Log.d(TAG, "populateMeetingList: No Entries In DB");
            return;
        }
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String title = cursor.getString(1);
            String startTime = cursor.getString(2);
            String endTime = cursor.getString(3);
            Date meetingStart;
            Date meetingEnd;
            if (startTime != null && !startTime.isEmpty() && endTime != null && !endTime.isEmpty()) {
                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy, mm, dd HH:mm");
                    meetingStart = format.parse(startTime);
                    meetingEnd = format.parse(endTime);
                } catch (ParseException e) {
                    Log.d(TAG, "populateMeetingList: PARSE ERROR");
                    e.printStackTrace();
                    meetingStart = null;
                    meetingEnd = null;
                }
                Double lat = null;
                Double lon = null;
                if (cursor.getString(4) != null && cursor.getString(5) != null) {
                    lat = Double.valueOf(cursor.getString(4));
                    lon = Double.valueOf(cursor.getString(5));
                }
                Meeting meeting = new Meeting(id, title, meetingStart, meetingEnd, lat, lon);
                meetingArrayList.add(meeting);
            }
        }
        ArrayList<Attendees> attendeesArrayList = loadAttendeesData(db);
        addAttendeesToMeeting(attendeesArrayList);
    }

    public ArrayList<Attendees> loadAttendeesData(DatabaseHelper db) {
        ArrayList<Attendees> attendeesArrayList = new ArrayList<>();
        Cursor cursor = db.getAllAttendeesData();
        if (cursor.getCount() == 0) {
            Log.d(TAG, "loadAttendeesData: NO DATA AVAILABLE");
            return attendeesArrayList;
        }
        while (cursor.moveToNext()) {
            String meetingID = cursor.getString(1);
            String friendID = cursor.getString(2);
            Attendees attendee = new Attendees(meetingID, friendID);
            attendeesArrayList.add(attendee);
        }
        return attendeesArrayList;
    }

    public void addAttendeesToMeeting(ArrayList<Attendees> list) {
        for (Meeting m : meetingArrayList) {
            for (Attendees attendee : list) {
                if (attendee.getMeetingID().equalsIgnoreCase(m.getMeetingID())) {
                    Log.d(TAG, "addAttendeesToMeeting: ******** FRINDS ADDIND" + attendee.getFriendID());
                    m.addFriendToMeeting(FriendData.getInstance().getFriendByID(attendee.getFriendID()));
                }
            }
        }
    }

    public void saveAttendeesData(DatabaseHelper db) {
        Log.d(TAG, "saveAttendeesData: CALLED");
        db.clearAttendeesData();
        ArrayList<Attendees> attendeesArrayList = new ArrayList<>();
        for (Meeting m : meetingArrayList) {
            for (Friend f : m.getMeetingAttendees()) {
                attendeesArrayList.add(new Attendees(m.getMeetingID(), f.getID()));
            }
        }
        for (Attendees attendees : attendeesArrayList) {
            Log.d(TAG, "saveAttendeesData: DATA >>>>" + attendees.getMeetingID() + attendees.getFriendID());
            db.insertAttendeesData(attendees.getMeetingID(), attendees.getFriendID());
        }
    }

    public void saveMeetingDatabase(DatabaseHelper db) {
        saveAttendeesData(db);
        db.clearMeetingTable();
        for (Meeting m : meetingArrayList) {
            DateFormat format = new SimpleDateFormat("yyyy, mm, dd HH:mm");
            Date startTime = m.getStartTime();
            Date endTime = m.getEndTime();
            String startString = null;
            String endString = null;
            if (startTime != null && endTime != null) {
                startString = format.format(startTime);
                endString = format.format(endTime);
            }
            db.insertMeetingData(m.getMeetingID(), m.getTitle(), startString, endString, m.getLat(), m.getLon());
        }
    }

    public void addSampleMeeting() {
        meetingArrayList.add(new Meeting("Chai Meet up", new Date(2017 - 1900, 9, 1, 10, 30), new Date(2017 - 1900, 9, 1, 11, 30), 34.12, -144.12));
        meetingArrayList.add(new Meeting("Coffee Meet up", new Date(2017 - 1900, 9, 2, 10, 30), new Date(2017 - 1900, 9, 1, 11, 30), -34.12, -144.12));
    }
    public Meeting getMeetingByID(String ID){
        for (Meeting meeting: meetingArrayList){
            if (meeting.getMeetingID().equals(ID)){
                return meeting;
            }
        }
        return null;
    }
    public int getMeetingListIndex(String ID){
        for (Meeting meeting: meetingArrayList){
            if (meeting.getMeetingID().equals(ID)){
                return meetingArrayList.indexOf(meeting);
            }
        }
        return -1;
    }
}
