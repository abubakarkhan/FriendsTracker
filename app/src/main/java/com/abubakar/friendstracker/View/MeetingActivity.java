package com.abubakar.friendstracker.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.abubakar.friendstracker.Controller.ManageMeeting;
import com.abubakar.friendstracker.Model.DatabaseHelper;
import com.abubakar.friendstracker.Model.MeetingData;
import com.abubakar.friendstracker.R;

public class MeetingActivity extends AppCompatActivity {

    private DatabaseHelper myDB;
    private MeetingListAdapter adapter;
    private BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_contacts:
                    finish();
                    return true;
                case R.id.navigation_meetings:
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        //Database
        myDB = new DatabaseHelper(this);

        //Link UI
        Button addNewMeeting = (Button) findViewById(R.id.addNewMeetingBtn);
        navigation = (BottomNavigationView) findViewById(R.id.navigation_meetings);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_meetings);
        ListView list = (ListView) findViewById(R.id.meetingListView);
        adapter = new MeetingListAdapter(getApplicationContext(), MeetingData.getInstance().getMeetingArrayList());
        list.setAdapter(adapter);

        // Button Testing toast
        addNewMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddMeetingActivity.class);
                startActivity(intent);
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MeetingActivity.this);
                mBuilder.setTitle("Manage Meetings");
                final int positionToRemove = i;
                ManageMeeting.getInstance().populateMeetingDialog(positionToRemove,mBuilder);
                mBuilder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ManageMeeting.getInstance().editMeeting(getApplicationContext(),positionToRemove,MeetingActivity.this,dialogInterface);
                    }
                });
                mBuilder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MeetingData.getInstance().getMeetingArrayList().remove(positionToRemove);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(),"Meeting Deleted", Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    }
                });
                mBuilder.setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = mBuilder.create();
                alertDialog.show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.meeting_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.btn_sortDateDescending:
                MeetingData.getInstance().sortMeetingsByDateDesc();
                adapter.notifyDataSetChanged();
                return true;
            case R.id.btn_sortDateAscending:
                MeetingData.getInstance().sortMeetingsByDateAscending();
                adapter.notifyDataSetChanged();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigation.setSelectedItemId(R.id.navigation_meetings);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MeetingData.getInstance().saveMeetingDatabase(myDB);
    }
}
