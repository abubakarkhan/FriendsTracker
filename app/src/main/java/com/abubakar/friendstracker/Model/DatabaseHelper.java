package com.abubakar.friendstracker.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    // If database schema changed, increment the database version.
    private static final String DATABASE_NAME = "FriendsTracker.db";
    private static final int DATABASE_VERSION = 1;

    //Table Names
    private static final String TABLE_NAME_FRIENDS = "Friends";
    private static final String TABLE_NAME_MEETINGS = "Meetings";
    private static final String TABLE_NAME_ATTENDEES = "Attendees";

    //Friends Table Columns
    private static final String COL_FRIEND_ID = "FriendID";
    private static final String COL_FRIEND_NAME = "Name";
    private static final String COL_FRIEND_EMAIL = "Email";
    private static final String COL_FRIEND_DOB = "DOB";
    private static final String COL_FRIEND_LATITUDE = "Latitude";
    public static final String COL_FRIEND_LONGITUDE = "Longitude";
    private static final String COL_FRIEND_TIMESTAMP = "Timestamp";

    //Meetings Table Columns
    private static final String COL_MEETING_ID = "MeetingID";
    private static final String COL_MEETING_TITLE = "Title";
    private static final String COL_MEETING_START_TIME = "StartTime";
    private static final String COL_MEETING_END_TIME = "EndTIme";
    private static final String COL_MEETING_LATITUDE = "Latitude";
    private static final String COL_MEETING_LONGITUDE = "Longitude";

    //Attendees Table Columns
    private static final String COL_ATTENDEES_ID = "ID";
    private static final String COL_ATTENDEES_MEETING_ID = "MeetingID";
    private static final String COL_ATTENDEES_FRIEND_ID = "FriendID";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "DatabaseHelper: CONSTRUCTOR");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: DB CALLED");

        //Create Friends Table
        String sSQL = "CREATE TABLE " + TABLE_NAME_FRIENDS + " (" +
                COL_FRIEND_ID + " TEXT PRIMARY KEY NOT NULL, " +
                COL_FRIEND_NAME + " TEXT NOT NULL, " +
                COL_FRIEND_EMAIL + " TEXT, " +
                COL_FRIEND_DOB + " TEXT," +
                COL_FRIEND_LATITUDE + " REAL, " +
                COL_FRIEND_LONGITUDE + " REAL, " +
                COL_FRIEND_TIMESTAMP + " TEXT)";
        Log.d(TAG, sSQL);
        db.execSQL(sSQL);

        //Create Meetings Table
        sSQL = "CREATE TABLE " + TABLE_NAME_MEETINGS + " (" +
                COL_MEETING_ID + " TEXT PRIMARY KEY NOT NULL, " +
                COL_MEETING_TITLE + " TEXT NOT NULL, " +
                COL_MEETING_START_TIME + " TEXT, " +
                COL_MEETING_END_TIME + " TEXT," +
                COL_MEETING_LATITUDE + " REAL, " +
                COL_MEETING_LONGITUDE + " REAL)";
        Log.d(TAG, sSQL);
        db.execSQL(sSQL);

        //Create Meeting Attendees Table
        sSQL = "CREATE TABLE " + TABLE_NAME_ATTENDEES + " (" +
                COL_ATTENDEES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ATTENDEES_MEETING_ID + " TEXT NOT NULL, " +
                COL_ATTENDEES_FRIEND_ID + " TEXT NOT NULL)";
        Log.d(TAG, sSQL);
        db.execSQL(sSQL);
        Log.d(TAG, "onCreate: DB EXIT");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.d(TAG, "onUpgrade: Called");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FRIENDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MEETINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ATTENDEES);
        //Create New Tables
        onCreate(db);
        Log.d(TAG, "onUpgrade: EXit");
    }

    public boolean insertAttendeesData(String meetingID, String friendID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ATTENDEES_MEETING_ID, meetingID);
        values.put(COL_ATTENDEES_FRIEND_ID, friendID);

        long result = db.insert(TABLE_NAME_ATTENDEES, null, values);
        if (result == -1) {
            Log.d(TAG, "insertAttendeesData: FAILED");
            return false;
        } else {
            Log.d(TAG, "insertAttendeesData: SUCCESS");
            return true;
        }
    }

    public Cursor getAllAttendeesData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_ATTENDEES + ";", null);
        return cursor;
    }

    public void clearAttendeesData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_ATTENDEES, null, null);
        Log.d(TAG, "clearAttendeesData: CLEARED");
    }


    public boolean insertMeetingData(String meetingID, String title, String startTime, String endTime, Double lat, Double lon) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_MEETING_ID, meetingID);
        values.put(COL_MEETING_TITLE, title);
        values.put(COL_MEETING_START_TIME, startTime);
        values.put(COL_MEETING_END_TIME, endTime);
        values.put(COL_MEETING_LATITUDE, lat);
        values.put(COL_MEETING_LONGITUDE, lon);

        long result = db.insert(TABLE_NAME_MEETINGS, null, values);
        if (result == -1) {
            Log.d(TAG, "insertMeetingData: FAILED");
            return false;
        } else {
            Log.d(TAG, "insertMeetingData: SUCCESSFUL");
            return true;
        }
    }

    public Cursor getAllMeetingData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_MEETINGS + ";", null);
        return cursor;
    }

    public void clearMeetingTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_MEETINGS, null, null);
        Log.d(TAG, "clearMeetingTable: CLEARED");
    }

    public boolean insertFriendData(String friendID, String name, String dob,
                                    String email, Double lat, Double lon, String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_FRIEND_ID, friendID);
        values.put(COL_FRIEND_NAME, name);
        values.put(COL_FRIEND_EMAIL, email);
        values.put(COL_FRIEND_DOB, dob);
        values.put(COL_FRIEND_LATITUDE, lat);
        values.put(COL_FRIEND_LONGITUDE, lon);
        values.put(COL_FRIEND_TIMESTAMP, timestamp);

        long result = db.insert(TABLE_NAME_FRIENDS, null, values);
        if (result == -1) {
            Log.d(TAG, "insertData: False");
            return false;
        } else {
            Log.d(TAG, "insertData: True");
            return true;
        }
    }

    public Cursor getAllFriendData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_FRIENDS + ";", null);
        return cursor;
    }

    public void clearFriendsTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_FRIENDS, null, null);
    }
}
