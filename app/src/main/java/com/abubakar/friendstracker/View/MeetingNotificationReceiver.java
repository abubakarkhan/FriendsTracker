package com.abubakar.friendstracker.View;

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

import com.abubakar.friendstracker.Model.Meeting;
import com.abubakar.friendstracker.Model.MeetingData;
import com.abubakar.friendstracker.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MeetingNotificationReceiver extends BroadcastReceiver {

    private static final String TAG = "MeetingNotificationRece";

    @Override
    public void onReceive(Context context, Intent intent) {
        int requestCode = intent.getExtras().getInt("requestCode");
        String meetingId = intent.getExtras().getString("meetingId");
//        MeetingData.getInstance().addSampleMeeting(); NEEDS FIX
        Meeting meeting = MeetingData.getInstance().getMeetingByID(meetingId);

        Log.d(TAG, "onReceive: req code " + requestCode + meetingId);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        Intent launchIntent = new Intent(context, MeetingActivity.class);
        launchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentIntent(pendingIntent).setSmallIcon(R.drawable.icon_planner)
                .setContentTitle(meeting.getTitle())
                .setContentText("Upcoming Meeting")
                .setAutoCancel(true);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);
        mBuilder.setLights(Color.BLUE, 500, 500);
        mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

        notificationManager.notify(requestCode, mBuilder.build());
    }
}
