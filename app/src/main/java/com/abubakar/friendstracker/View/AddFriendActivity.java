package com.abubakar.friendstracker.View;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.abubakar.friendstracker.Controller.ManageFriend;
import com.abubakar.friendstracker.R;

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
                boolean validData = ManageFriend.getInstance().saveNewFriend(nameText,emailText,dobText,dateOfBirth,getApplicationContext());
                //Save and go back to main
                if (validData){finish();}
            }
        });
        //Date edit text
        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildDateDialog("addFriend");
            }
        });
        dateOfBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    buildDateDialog("addFriend");
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
