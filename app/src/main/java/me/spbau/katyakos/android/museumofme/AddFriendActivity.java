package me.spbau.katyakos.android.museumofme;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static android.graphics.Color.GRAY;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.WHITE;

/**
 * Created by KatyaKos on 25.11.2016.
 */

public class AddFriendActivity extends Activity {

    private EditText userSearch;
    private Button backButton;
    private ArrayList<String> userFriends;
    private ArrayList<String> allUsers;
    LinearLayout usersList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        initialize();

        usersListCreate("");
        buttonsListener();
    }

    private void initialize() {
        userSearch = (EditText) findViewById(R.id.add_friends_search_field);
        backButton = (Button) findViewById(R.id.add_friends_button_back);
        usersList = (LinearLayout) findViewById(R.id.add_friends_list);
        userFriends = UserInformation.getUserFriends();
        allUsers = AllUsersInformation.getUsers();
    }

    private void buttonsListener() {
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                usersList.removeAllViews();
                usersListCreate(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }

    private void usersListCreate(CharSequence sort) {
        LayoutInflater layoutInflater = getLayoutInflater();

        for (final String user : allUsers) {
            String[] pieces = user.split("::");
            if (!(pieces[0].toLowerCase().contains(sort.toString().toLowerCase()) ||
                    pieces[1].toLowerCase().contains(sort.toString().toLowerCase()))) {
                continue;
            }
            View item = layoutInflater.inflate(R.layout.item_listview, usersList, false);

            ImageView photo = (ImageView) item.findViewById(R.id.listview_photo);
            int id = getResources().getIdentifier(pieces[2], "drawable", getPackageName());
            photo.setImageResource(id);

            TextView nickname = (TextView) item.findViewById(R.id.listview_text1);
            nickname.setText(pieces[0]);

            TextView name = (TextView) item.findViewById(R.id.listview_text2);
            name.setText(pieces[1]);
            name.setVisibility(View.VISIBLE);

            Button button = (Button) item.findViewById(R.id.listview_button);
            if (UserInformation.getUser().equals(user)) {
                button.setBackgroundColor(WHITE);
            } else if (userFriends.contains(user)) {
                button.setBackgroundColor(GRAY);
                button.setText("—");
            } else {
                button.setBackgroundColor(GREEN);
                button.setText("+");
            }

            usersList.addView(item);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button clicked = (Button) v;
                    if (clicked.getText().toString().equals("+")) {
                        addFriend(user);
                        clicked.setBackgroundColor(GRAY);
                        clicked.setText("—");
                    } else if (clicked.getText().toString().equals("—")) {
                        removeFriend(user);
                        clicked.setBackgroundColor(GREEN);
                        clicked.setText("+");
                    }
                }
            });
        }
    }

    private void addFriend(String user) {
        UserInformation.addFriend(user);
        setResult(RESULT_OK);
    }

    private void removeFriend(String user) {
        UserInformation.removeFriend(user);
        setResult(RESULT_OK);
    }

}
