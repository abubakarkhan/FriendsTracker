package com.abubakar.friendstracker.View;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.abubakar.friendstracker.Controller.ManageFriend;
import com.abubakar.friendstracker.Model.Friend;
import com.abubakar.friendstracker.Model.FriendData;
import com.abubakar.friendstracker.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class EditFriendActivity extends AppCompatActivity {

    private EditText editName;
    private EditText editEmail;
    private EditText editDateOfBirth;
    private EditText editLat;
    private EditText editLon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friend);
        //Get friend id to load
        final Bundle bundle = getIntent().getExtras();
        final String id = bundle.getString("id");

        //Attach UI
        Button saveChanges = (Button)findViewById(R.id.btnEditFriendSave);
        Button cancelEdit = (Button) findViewById(R.id.btnEditFriendCancel);
        editName = (EditText) findViewById(R.id.editFrientName);
        editEmail = (EditText) findViewById(R.id.editFrientEmail);
        editDateOfBirth = (EditText) findViewById(R.id.editFriendBirthday);
        editLat = (EditText) findViewById(R.id.edit_frn_lat);
        editLon = (EditText) findViewById(R.id.edit_frn_lon);
        //Fill up fields with current information
        DateFormat dateFormat = new SimpleDateFormat("MMM, dd, yyyy");
        final Friend friend   = FriendData.getInstance().getFriendByID(id);
        editName.setText(friend.getName());
        editEmail.setText(friend.getEmail());
        if (friend.getBirthday() == null){
            editDateOfBirth.setText("");
        }else {
            editDateOfBirth.setText(dateFormat.format(friend.getBirthday()));
        }
        try {
            editLat.setText(friend.getLat().toString());
            editLon.setText(friend.getLon().toString());
        } catch (Exception e) {
            editLat.setText("");
            editLon.setText("");
        }

        //Button listener
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameText = editName.getText().toString().trim();
                String emailText = editEmail.getText().toString().trim();
                String dobText = editDateOfBirth.getText().toString().trim();
                Double lat;
                Double lon;
                try {
                    lat = Double.valueOf(editLat.getText().toString().trim());
                    lon = Double.valueOf(editLon.getText().toString().trim());
                } catch (Exception e) {
                    lat = null;
                    lon = null;
                }
                boolean validChanges = ManageFriend.getInstance().saveEditFriendChanges(nameText,
                        emailText, dobText, friend, editDateOfBirth, lat, lon, id, getApplicationContext());
                if (validChanges){finish();}
            }
        });
        cancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //Date edit text
        editDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildDateDialog("editFriend");
            }
        });
        editDateOfBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    buildDateDialog("editFriend");
                }
            }
        });
    }
    public void buildDateDialog(String callType){
        DialogFragment newFragment = new DateDialog();
        Bundle bundle = new Bundle();
        bundle.putString("callType",callType);
        newFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        newFragment.show(fragmentManager,"datePicker");
    }
}
