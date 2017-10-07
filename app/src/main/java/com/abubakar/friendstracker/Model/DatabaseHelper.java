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

    //Friends Table
    private static final String TABLE_NAME_FRIENDS = "Friends";
    private static final String COL_FRIEND_ID = "FriendID";
    private static final String COL_FRIEND_NAME = "Name";
    private static final String COL_FRIEND_DOB = "DOB";
    private static final String COL_FRIEND_EMAIL = "Email";
    private static final String COL_FRIEND_LATITUDE = "Latitude";
    public static final String COL_FRIEND_LONGITUDE = "Longitude";
    private static final String COL_FRIEND_TIMESTAMP = "Timestamp";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "DatabaseHelper: CONSTRUCTOR");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: DB CALLED");
        String sSQL = "CREATE TABLE " + TABLE_NAME_FRIENDS + " (" +
                COL_FRIEND_ID + " TEXT PRIMARY KEY NOT NULL, " +
                COL_FRIEND_NAME + " TEXT NOT NULL, " +
                COL_FRIEND_DOB + " TEXT," +
                COL_FRIEND_EMAIL + " TEXT, " +
                COL_FRIEND_LATITUDE + " REAL, " +
                COL_FRIEND_LONGITUDE + " REAL, " +
                COL_FRIEND_TIMESTAMP + " TEXT)";
        Log.d(TAG, sSQL);
        db.execSQL(sSQL);
        Log.d(TAG, "onCreate: DB EXIT");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.d(TAG, "onUpgrade: Called");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FRIENDS);
        onCreate(db);
        Log.d(TAG, "onUpgrade: EXit");
    }

    public boolean insertFriendData(String friendID, String name, String dob,
                                    String email, double lat, double lon, String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_FRIEND_ID, friendID);
        values.put(COL_FRIEND_NAME, name);
        values.put(COL_FRIEND_DOB, dob);
        values.put(COL_FRIEND_EMAIL, email);
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
