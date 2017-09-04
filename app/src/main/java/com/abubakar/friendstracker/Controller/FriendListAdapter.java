package com.abubakar.friendstracker.Controller;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.abubakar.friendstracker.Model.Friend;
import com.abubakar.friendstracker.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FriendListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Friend> friendArrayList;

    public FriendListAdapter(Context context, ArrayList<Friend> friendArrayList) {
        this.context = context;
        this.friendArrayList = friendArrayList;
    }

    @Override
    public int getCount() {
        return friendArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return friendArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.friend_list_row, null);
        TextView name = v.findViewById(R.id.nameTextView);
        TextView email = v.findViewById(R.id.emailTextView);
        TextView birthday  = v.findViewById(R.id.birthdayTextView);

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        name.setText(friendArrayList.get(i).getName());
        email.setText(friendArrayList.get(i).getEmail());
        if (friendArrayList.get(i).getBirthday() == null){
            birthday.setText("N/A");
        }else {
            birthday.setText(dateFormat.format(friendArrayList.get(i).getBirthday()));
        }
        v.setTag(friendArrayList.get(i).getID());
        return v;
    }

    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
