package com.abubakar.friendstracker.View;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.abubakar.friendstracker.Model.FriendData;
import com.abubakar.friendstracker.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddFriendActivity extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText dateOfBirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        //Attach UI
        Button saveFriendBtn = (Button)findViewById(R.id.saveFriend);
        Button cancel = (Button) findViewById(R.id.cancelBtn);
        name = (EditText) findViewById(R.id.nameEditText);
        email = (EditText) findViewById(R.id.emailEditText);
        dateOfBirth = (EditText) findViewById(R.id.birthdayEditText);
        //Button listeners
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        saveFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameText = name.getText().toString().trim();
                String emailText = email.getText().toString().trim();
                String dobText = dateOfBirth.getText().toString().trim();
                if(!nameText.isEmpty() && !emailText.isEmpty() && !dobText.isEmpty()){
                    // Parse Date
                    SimpleDateFormat format = new SimpleDateFormat("MMM, dd, yyyy");
                    Date dob = null;
                    try{
                        dob = format.parse(dateOfBirth.getText().toString());
                    }catch (ParseException e){
                        e.printStackTrace();
                    }
                    FriendData.getInstance().addNewFriend(name.getText().toString(),email.getText().toString(),dob);
                    Toast.makeText(getApplicationContext(), "Friend Added", Toast.LENGTH_SHORT).show();
                    //Save and go back to main
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "Please fill in all of the fields", Toast.LENGTH_LONG).show();
                }
            }
        });
        //Date edit text
        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DateDialog();
                Bundle bundle = new Bundle();
                String callType = "addFriend";
                bundle.putString("callType",callType);
                newFragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                newFragment.show(fragmentManager,"datePicker");
            }
        });
        dateOfBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    DialogFragment newFragment = new DateDialog();
                    Bundle bundle = new Bundle();
                    String callType = "addFriend";
                    bundle.putString("callType",callType);
                    newFragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    newFragment.show(fragmentManager,"datePicker");
                }
            }
        });
    }
}
