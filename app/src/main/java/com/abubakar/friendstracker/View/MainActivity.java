package com.abubakar.friendstracker.View;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.abubakar.friendstracker.Controller.ManageFriend;
import com.abubakar.friendstracker.Model.DatabaseHelper;
import com.abubakar.friendstracker.Model.Friend;
import com.abubakar.friendstracker.Model.FriendData;
import com.abubakar.friendstracker.Model.MeetingData;
import com.abubakar.friendstracker.R;
import com.abubakar.friendstracker.SupportCode.ContactDataManager;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper myDB;
    private ListView list;
    private FriendListAdapter adapter;
    private BottomNavigationView navigation;
    protected static final int PICK_CONTACTS = 100;
    private static final String TAG = "MainActivity";
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_contacts:
                    return true;
                case R.id.navigation_meetings:
                    Intent intent = new Intent(getApplicationContext(), MeetingActivity.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Database
        myDB = new DatabaseHelper(this);
        //Populate array list from DB
        FriendData.getInstance().populateFriendsList(myDB);
        MeetingData.getInstance().populateMeetingList(myDB);

        //Link UI
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        Button addNewFriend = (Button)findViewById(R.id.addFreindBtn);
        Button importContactsBtn = (Button) findViewById(R.id.importContactsBtn);
        Button sampleDataBtn = (Button) findViewById(R.id.btn_sampleDataFriends);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //List View
        list = (ListView) findViewById(R.id.friendListView);
        adapter = new FriendListAdapter(getApplicationContext(), FriendData.getInstance().getFriendArrayList());
        list.setAdapter(adapter);

        //Add Friend listener
        addNewFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddFriendActivity.class);
                startActivity(intent);
            }
        });
        //Add sample contacts
        sampleDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FriendData.getInstance().addSampleFriends();
                adapter.notifyDataSetChanged();
            }
        });
        //List item listener
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Build Dialog
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                mBuilder.setTitle("Manage Friend");
                final int positionToRemove = i;
                //Set dialog message
                ManageFriend.getInstance().populateFriendDialog(positionToRemove,mBuilder);
                //Dialog button listeners
                mBuilder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ManageFriend.getInstance().editFriend(getApplicationContext(),positionToRemove,MainActivity.this,dialogInterface);
                    }
                });
                mBuilder.setNegativeButton("Delete", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ManageFriend.getInstance().deleteFriend(positionToRemove,adapter,getApplicationContext(),dialogInterface);
                    }
                });
                mBuilder.setNeutralButton("Dismiss", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = mBuilder.create();
                alertDialog.show();
            }
        });
        //Permission
        getPermissions();
        //Import Phone Contacts
        importContactsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readContacts();
//                Log.d(TAG, "onClick: button click");
//                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//                int requestCode = 1;
//                for (Meeting meeting : MeetingData.getInstance().getMeetingArrayList()) {
//                    Log.d(TAG, "onClick: in for loop" + requestCode);
//
//                    Intent intent = new Intent(getApplicationContext(), MeetingNotificationReceiver.class);
//                    intent.putExtra("requestCode", requestCode);
//                    intent.putExtra("meetingId", meeting.getMeetingID());
//                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                    Calendar calendar = Calendar.getInstance();
//                    calendar.set(Calendar.HOUR_OF_DAY, 14);
//                    calendar.set(Calendar.MINUTE, 10 + requestCode);
//                    calendar.set(Calendar.SECOND, 3);
//                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//
//                    requestCode++;
//                }
//                Log.d(TAG, "onClick: button click exit");
            }
        });
    }
    private void readContacts(){
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, PICK_CONTACTS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_CONTACTS){
            if(resultCode == RESULT_OK){
                ContactDataManager contactsManager = new ContactDataManager(this, data);
                String name = "";
                String email = "";
                try {
                    name = contactsManager.getContactName();
                    email = contactsManager.getContactEmail();
                    Friend friend = new Friend(name, email, null, null, null, null);
                    FriendData.getInstance().addNewFriend(friend);
                    Toast.makeText(getApplicationContext(),"Added Friend: " + name, Toast.LENGTH_LONG).show();
                    adapter.notifyDataSetChanged();
                }catch (ContactDataManager.ContactQueryException e){
                    e.printStackTrace();
                }
            }
        }
    }
    public void getPermissions() {
        int Permission_All = 1;

        String[] Permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_CONTACTS};
        if(!hasPermissions(this, Permissions)){
            ActivityCompat.requestPermissions(this, Permissions, Permission_All);
        }
    }
    public static boolean hasPermissions(Context context, String... permissions){

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && context!=null && permissions!=null){
            for(String permission: permissions){
                if(ActivityCompat.checkSelfPermission(context, permission)!= PackageManager.PERMISSION_GRANTED){
                    return  false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigation.setSelectedItemId(R.id.navigation_contacts);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        FriendData.getInstance().saveFriendDatabase(myDB);
        MeetingData.getInstance().saveMeetingDatabase(myDB);
    }
}
