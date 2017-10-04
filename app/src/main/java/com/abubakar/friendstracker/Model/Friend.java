package com.abubakar.friendstracker.Model;

import java.util.Date;

public class Friend {

    private String ID;
    private String name;
    private String email;
    private Date birthday;
    private static int counter = 0;

    public Friend(String name, String email, Date birthday) {
        counter += 1;
        if (name.length() == 1){
            this.ID = Character.toString(name.charAt(0)) + counter;
        }else {
            this.ID = Character.toString(name.charAt(0)) + Character.toString(name.charAt(1)) + counter;
        }
        this.name = name;
        this.email = email;
        this.birthday = birthday;
    }

    public Friend(String name, String email) {
        counter += 1;
        if (name.length() == 1){
            this.ID = Character.toString(name.charAt(0)) + counter;
        }else {
            this.ID = Character.toString(name.charAt(0)) + Character.toString(name.charAt(1)) + counter;
        }
        this.name = name;
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Date getBirthday() {
        return birthday;
    }
}
