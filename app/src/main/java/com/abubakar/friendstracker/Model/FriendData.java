package com.abubakar.friendstracker.Model;

import java.util.ArrayList;
import java.util.Date;

public class FriendData {

    private static final FriendData ourInstance = new FriendData();
    private ArrayList<Friend> friendArrayList = new ArrayList<>();
    private boolean dataAdded = false;

    public static FriendData getInstance() {
        return ourInstance;
    }

    private FriendData() {
    }

    public ArrayList<Friend> getFriendArrayList() {
        return friendArrayList;
    }
    public void addNewFriend(String name, String email, Date date ){
        friendArrayList.add(new Friend(name, email, date));
    }
    public void addNewFriend(String name, String email){
        friendArrayList.add(new Friend(name, email));
    }

    public void addSampleFriends(){
        if (!dataAdded) {
            friendArrayList.add(new Friend("Jon Snow","jon@email.com",new Date(1989-1900,11,1)));
            friendArrayList.add(new Friend("Tyrion Lannister","tyrion@email.com",new Date(1988-1900,5,1)));
            friendArrayList.add(new Friend("Eddard Stark","ned@email.com",new Date(1987-1900,5,1)));
            friendArrayList.add(new Friend("Arya Stark","arya@email.com",new Date(1995-1900,5,1)));
            friendArrayList.add(new Friend("Sansa Stark","arya@email.com",new Date(1992-1900,2,12)));
            friendArrayList.add(new Friend("Jon Snow","jon@email.com",new Date(1989-1900,11,1)));
            friendArrayList.add(new Friend("Tyrion Lannister","tyrion@email.com",new Date(1988-1900,5,1)));
            friendArrayList.add(new Friend("Eddard Stark","ned@email.com",new Date(1987-1900,5,1)));
            friendArrayList.add(new Friend("Arya Stark","arya@email.com",new Date(1995-1900,5,1)));
            friendArrayList.add(new Friend("Sansa Stark","arya@email.com",new Date(1992-1900,2,12)));
            dataAdded = true;
        }

    }
    public Friend getFriendByID(String ID){
        for (Friend friend: friendArrayList){
            if (friend.getID().equals(ID)){
                return friend;
            }
        }
        return null;
    }
    public int getFriendListIndex(String ID){
        for (Friend friend: friendArrayList){
            if (friend.getID().equals(ID)){
                return friendArrayList.indexOf(friend);
            }
        }
        return -1;
    }
}
