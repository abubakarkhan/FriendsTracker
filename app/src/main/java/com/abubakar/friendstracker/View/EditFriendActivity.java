package com.abubakar.friendstracker.View;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.abubakar.friendstracker.Model.Friend;
import com.abubakar.friendstracker.Model.FriendData;
import com.abubakar.friendstracker.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditFriendActivity extends AppCompatActivity {

    private EditText editName;
    private EditText editEmail;
    private EditText editDateOfBirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friend);
        //Get friend id to load
        Bundle bundle = getIntent().getExtras();
        final String id = bundle.getString("id");

        //Attach UI
        Button saveChanges = (Button)findViewById(R.id.btnEditFriendSave);
        Button cancelEdit = (Button) findViewById(R.id.btnEditFriendCancel);
        editName = (EditText) findViewById(R.id.editFrientName);
        editEmail = (EditText) findViewById(R.id.editFrientEmail);
        editDateOfBirth = (EditText) findViewById(R.id.editFriendBirthday);
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

        //Button listener
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameText = editName.getText().toString().trim();
                String emailText = editEmail.getText().toString().trim();
                String dobText = editDateOfBirth.getText().toString().trim();

                if(!nameText.isEmpty() && !emailText.isEmpty() && !dobText.isEmpty()){
                    // Parse Date
                    SimpleDateFormat format = new SimpleDateFormat("MMM, dd, yyyy");
                    Date dob = friend.getBirthday();
                    try{
                        dob = format.parse(editDateOfBirth.getText().toString());
                    }catch (ParseException e){
                        e.printStackTrace();
                    }
                    //Get index of editable item and save changes
                    int positionIndex = FriendData.getInstance().getFriendListIndex(id);
                    FriendData.getInstance().getFriendArrayList().get(positionIndex).setName(editName.getText().toString());
                    FriendData.getInstance().getFriendArrayList().get(positionIndex).setEmail(editEmail.getText().toString());
                    if(dob == null){
                        FriendData.getInstance().getFriendArrayList().get(positionIndex).setBirthday(friend.getBirthday());
                    }else {
                        FriendData.getInstance().getFriendArrayList().get(positionIndex).setBirthday(dob);
                    }
                    //Save and go back to main
                    Toast.makeText(getApplicationContext(), "Changes Saved", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "Please fill in all of the fields", Toast.LENGTH_SHORT).show();
                }
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
                DialogFragment newFragment = new DateDialog();
                Bundle bundle = new Bundle();
                String callType = "editFriend";
                bundle.putString("callType",callType);
                newFragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                newFragment.show(fragmentManager,"datePicker");
            }
        });
        editDateOfBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    DialogFragment newFragment = new DateDialog();
                    Bundle bundle = new Bundle();
                    String callType = "editFriend";
                    bundle.putString("callType",callType);
                    newFragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    newFragment.show(fragmentManager,"datePicker");
                }
            }
        });
    }
}
