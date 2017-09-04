package com.abubakar.friendstracker.Model;


import java.util.ArrayList;
import java.util.Date;

public class MeetingData {
    private static final MeetingData ourInstance = new MeetingData();
    private ArrayList<Meeting> meetingArrayList = new ArrayList<>();
    private boolean dataAdded = false;

    public static MeetingData getInstance() {
        return ourInstance;
    }

    private MeetingData() {
    }

    public ArrayList<Meeting> getMeetingArrayList() {
        return meetingArrayList;
    }
    public ArrayList<Meeting> sortMeetingsByDateDesc(){
        Meeting temp = null;
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
        Meeting temp = null;
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
    public void addNewMeeting(String title, Date startTime, Date endTime, String location){
        meetingArrayList.add(new Meeting(title,startTime,endTime,location));
    }
    public void addMeetingFriend(Meeting meeting,String friendID){
        meeting.addFriendToMeeting(friendID);
    }
    public void addSampleMeeting(){
        if(!dataAdded){
            meetingArrayList.add(new Meeting("Chai Meetup",new Date(2017-1900,9,1,10,30),new Date(2017-1900,9,1,11,30),"Lat-34.12, Lon-144.12"));
            meetingArrayList.add(new Meeting("Coffee Meetup",new Date(2017-1900,9,2,10,30),new Date(2017-1900,9,1,11,30),"Lat-34.12, Lon-144.12"));
            meetingArrayList.add(new Meeting("Design Meeting",new Date(2017-1900,9,3,10,30),new Date(2017-1900,9,1,11,30),"Lat-34.12, Lon-144.12"));
            dataAdded = true;
        }
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
