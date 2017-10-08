package com.abubakar.friendstracker.Model;

import java.util.Date;

public class Friend {

    private String ID;
    private String name;
    private String email;
    private Date birthday;
    private Double lat;
    private Double lon;
    private String timestamp;
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

    public Friend(String name) {
        counter += 1;
        if (name.length() == 1) {
            this.ID = Character.toString(name.charAt(0)) + counter;
        } else {
            this.ID = Character.toString(name.charAt(0)) + Character.toString(name.charAt(1)) + counter;
        }
        this.name = name;
    }

    public Friend(String name, String email, Date birthday, Double lat, Double lon, String timestamp) {
        counter += 1;
        if (name.length() == 1) {
            this.ID = Character.toString(name.charAt(0)) + counter;
        } else {
            this.ID = Character.toString(name.charAt(0)) + Character.toString(name.charAt(1)) + counter;
        }
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.lat = lat;
        this.lon = lon;
        this.timestamp = timestamp;
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

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
