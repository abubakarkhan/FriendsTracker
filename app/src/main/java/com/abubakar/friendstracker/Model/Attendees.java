package com.abubakar.friendstracker.Model;

public class Attendees {

    private int ID;
    private String meetingID;
    private String friendID;

    public Attendees(int ID, String meetingID, String friendID) {
        this.ID = ID;
        this.meetingID = meetingID;
        this.friendID = friendID;
    }

    public Attendees(String meetingID, String friendID) {
        this.meetingID = meetingID;
        this.friendID = friendID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getMeetingID() {
        return meetingID;
    }

    public void setMeetingID(String meetingID) {
        this.meetingID = meetingID;
    }

    public String getFriendID() {
        return friendID;
    }

    public void setFriendID(String friendID) {
        this.friendID = friendID;
    }
}
