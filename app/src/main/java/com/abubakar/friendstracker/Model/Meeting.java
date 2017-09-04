package com.abubakar.friendstracker.Model;

import java.util.ArrayList;
import java.util.Date;


public class Meeting {

    private String meetingID;
    private String title;
    private Date startTime;
    private Date endTime;
    private String location;
    private ArrayList<String> meetingFriendsID = new ArrayList<>();
    private static int counter = 0;

    public Meeting(String title, Date startTime, Date endTime, String location) {
        this.title = title;
        counter +=1;
        if (title.length() == 1){
            this.meetingID = counter + Character.toString(title.charAt(0));
        }else {
            this.meetingID = Character.toString(title.charAt(0)) + counter + Character.toString(title.charAt(1));
        }
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
    }
    public void addFriendToMeeting(String friendID){
        meetingFriendsID.add(friendID);
    }
    public void removeFriendFromMeeting(String friendRemoved){
        for(String friendAttending : meetingFriendsID){
            if(friendAttending.equalsIgnoreCase(friendRemoved)){
                meetingFriendsID.remove(friendAttending);
            }
            break;
        }
    }

    public String getMeetingID() {
        return meetingID;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<String> getMeetingFriendsID() {
        return meetingFriendsID;
    }

    public void setMeetingFriendsID(ArrayList<String> meetingFriendsID) {
        this.meetingFriendsID = meetingFriendsID;
    }
}
