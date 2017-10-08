package com.abubakar.friendstracker.Controller;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.abubakar.friendstracker.Model.DatabaseHelper;

import static android.content.ContentValues.TAG;

public class cancelActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DatabaseHelper myDB = new DatabaseHelper(context);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String meetingID = intent.getExtras().getString("meetingID");
        Log.d(TAG, "onReceive: DELETING MEEITNG " + meetingID);
        myDB.deleteMeetingRow(meetingID);
        Toast.makeText(context, "Meeting Cancelled", Toast.LENGTH_LONG).show();
        manager.cancel(intent.getExtras().getInt("requestCode"));
        Toast.makeText(context, "Dismissed", Toast.LENGTH_SHORT).show();

    }
}
