package com.abubakar.friendstracker.View;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.abubakar.friendstracker.Controller.ManageFriend;
import com.abubakar.friendstracker.Model.FriendData;
import com.abubakar.friendstracker.R;

public class MainActivity extends AppCompatActivity {

    ListView list;
    private FriendListAdapter adapter;
    private BottomNavigationView navigation;
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;
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
        //Add FriendData
        FriendData.getInstance().addSampleFriends();
        //Link UI
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        Button addNewFriend = (Button)findViewById(R.id.addFreindBtn);
        Button importContactsBtn = (Button) findViewById(R.id.importContactsBtn);
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
        //List item listener
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Build Dialog
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                mBuilder.setTitle("Manage Friend");
                final int positionToRemove = i;
                //Set dialog message
                ManageFriend.getInstance().populateDialog(positionToRemove,mBuilder);
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
        //permission
        getPermissionToReadUserContacts();
        //Import Phone Contacts
        importContactsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentResolver cr = getContentResolver();
                Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
                if (cur.getCount() > 0) {
                    while (cur.moveToNext()) {
                        String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                        Cursor cur1 = cr.query(
                                ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        while (cur1.moveToNext()) {
                            //to get the contact names
                            String name=cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            Log.e("Name :", name);
                            String email = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                            Log.e("Email", email);
                            if(email!=null){
                                FriendData.getInstance().addNewFriend(name,email);
                            }else {
                                FriendData.getInstance().addNewFriend(name,"N/A");
                            }
                        }
                        cur1.close();
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void getPermissionToReadUserContacts() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_CONTACTS)) {
            }
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    READ_CONTACTS_PERMISSIONS_REQUEST);
        }
    }

    // Callback with the request from calling requestPermissions(...)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == READ_CONTACTS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read Contacts permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Read Contacts permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigation.setSelectedItemId(R.id.navigation_contacts);
        adapter.notifyDataSetChanged();
    }
}
