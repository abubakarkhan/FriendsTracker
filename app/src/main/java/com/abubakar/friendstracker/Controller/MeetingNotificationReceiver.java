package com.abubakar.friendstracker.Controller;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.abubakar.friendstracker.Model.DatabaseHelper;
import com.abubakar.friendstracker.Model.Meeting;
import com.abubakar.friendstracker.R;
import com.abubakar.friendstracker.View.MainActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MeetingNotificationReceiver extends BroadcastReceiver {

    private static final String TAG = "MeetingNotificationRece";
    private DatabaseHelper myDB;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: STARTED");
        myDB = new DatabaseHelper(context);

        int requestCode = intent.getExtras().getInt("requestCode");
        String meetingId = intent.getExtras().getString("meetingId");
        Meeting meeting = myDB.getMeeting(meetingId);
        meetingId = meeting.getMeetingID();
        if (meeting == null) {
            return;
        }
        Log.d(TAG, "onReceive: req code " + requestCode + meetingId);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        //Launch App Intent
        Intent launchIntent = new Intent(context, MainActivity.class);
        launchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //Notification builder Properties
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentIntent(pendingIntent).setSmallIcon(R.drawable.icon_planner)
                .setContentTitle(meeting.getTitle())
                .setContentText("Meeting at Location: " + meeting.getLat() + "," + meeting.getLon())
                .setAutoCancel(true);
        //Add Action Buttons
        mBuilder.addAction(R.drawable.icon_dismiss, "Dismiss", getDismissIntent(context, requestCode));
        mBuilder.addAction(R.drawable.icon_delete, "Cancel", getCancelIntent(context, requestCode, meetingId));
        mBuilder.addAction(R.drawable.icon_snooze, "Snooze", getSnoozeIntent(context, requestCode, meetingId));

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);
        mBuilder.setLights(Color.BLUE, 500, 500);
        mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

        notificationManager.notify(requestCode, mBuilder.build());
    }

    public PendingIntent getDismissIntent(Context context, int requestCode) {
        //Dismiss Intent
        Intent dismissIntent = new Intent("dismiss_click");
        dismissIntent.putExtra("requestCode", requestCode);
        return PendingIntent.getBroadcast(context, requestCode, dismissIntent, 0);
    }

    public PendingIntent getCancelIntent(Context context, int requestCode, String meetingId) {
        //Cancel Intent
        Intent cancelIntent = new Intent("cancel_click");
        cancelIntent.putExtra("requestCode", requestCode);
        cancelIntent.putExtra("meetingID", meetingId);
        return PendingIntent.getBroadcast(context, requestCode, cancelIntent, 0);
    }

    public PendingIntent getSnoozeIntent(Context context, int requestCode, String meetingId) {
        //Snooze Intent
        Intent snoozeIntent = new Intent("snooze_click");
        snoozeIntent.putExtra("requestCode", requestCode);
        snoozeIntent.putExtra("meetingID", meetingId);
        return PendingIntent.getBroadcast(context, requestCode, snoozeIntent, 0);
    }
}
