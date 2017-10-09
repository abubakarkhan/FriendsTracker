package com.abubakar.friendstracker.Controller;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;

public class snoozeActionReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String meetingID = intent.getExtras().getString("meetingID");
        int requestCode = intent.getExtras().getInt("requestCode");
        int min = snoozeNotification(context, meetingID, requestCode);
        manager.cancel(requestCode);
        Toast.makeText(context, "Snoozed for " + min + " minutes", Toast.LENGTH_SHORT).show();
    }

    public int snoozeNotification(Context context, String meetingID, int requestCode) {
        SharedPreferences preferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        int min = preferences.getInt("snoozeTime", 1);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, MeetingNotificationReceiver.class);
        intent.putExtra("requestCode", requestCode);
        intent.putExtra("meetingId", meetingID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long ONE_MINUTE_IN_MILLIS = 60000;
        Date snoozeTime = new Date(System.currentTimeMillis() + (ONE_MINUTE_IN_MILLIS * min));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(snoozeTime);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        return min;
    }
}
