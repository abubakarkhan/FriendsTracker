package com.abubakar.friendstracker.View;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.abubakar.friendstracker.Controller.ManageFriend;
import com.abubakar.friendstracker.Model.FriendData;
import com.abubakar.friendstracker.R;
import com.abubakar.friendstracker.SupportCode.ContactDataManager;

public class MainActivity extends AppCompatActivity {

    ListView list;
    private FriendListAdapter adapter;
    private BottomNavigationView navigation;
    protected static final int PICK_CONTACTS = 100;
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
        getPermissionToReadUserContacts();
        //Import Phone Contacts
        importContactsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readContacts();
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
                    System.out.println(name + email);
                    FriendData.getInstance().addNewFriend(name,email);
                    Toast.makeText(getApplicationContext(),"Added Friend: " + name, Toast.LENGTH_LONG).show();
                    adapter.notifyDataSetChanged();
                }catch (ContactDataManager.ContactQueryException e){
                    e.printStackTrace();
                }
            }
        }
    }
    public void getPermissionToReadUserContacts() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_CONTACTS)) {
                // Show our own UI to explain to the user why we need to read the contacts
                // before actually requesting the permission and showing the default UI
            }

            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    READ_CONTACTS_PERMISSIONS_REQUEST);
        }
    }
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
