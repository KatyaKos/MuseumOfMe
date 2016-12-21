package me.spbau.katyakos.android.museumofme;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.Toast;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static android.graphics.Color.GRAY;
import static android.graphics.Color.GREEN;

abstract class AbstractFriendActivity extends Activity {
    protected Button backButton;
    protected EditText userSearch;
    protected LinearLayout usersListLayout;
    protected TreeMap<String, UserInformation> usersList;

    protected Integer userId;
    protected UserInformation user;

    protected int activityId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activityId);

        Intent thisIntent = getIntent();
        userId = thisIntent.getIntExtra("userId", 0);
        user = AllUsersInformation.getUserById(userId);

        fieldsInitialization();

        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                usersListLayout.removeAllViews();
                listCreate(cs.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        listCreate();
    }

    abstract void fieldsInitialization();

    protected void listCreate() {
        listCreate("");
    }

    protected void setListFields(UserInformation currentUser, View item) {
        ImageView photo = (ImageView) item.findViewById(R.id.listview_photo);
        int id = getResources().getIdentifier(currentUser.getUserPhoto(), "drawable", getPackageName());
        photo.setImageResource(id);

        TextView nickname = (TextView) item.findViewById(R.id.listview_text1);
        nickname.setText(currentUser.getUserNickname());

        TextView name = (TextView) item.findViewById(R.id.listview_text2);
        name.setText(currentUser.getUserName());
        name.setVisibility(View.VISIBLE);

        Button button = (Button) item.findViewById(R.id.listview_button);
        button.setBackgroundColor(GRAY);
        button.setText("—");
    }

    protected void listCreate(String sort) {
        LayoutInflater layoutInflater = getLayoutInflater();
        Set<Map.Entry<String, UserInformation>> usersEntry = usersList.entrySet();

        for (Map.Entry<String, UserInformation> entry : usersEntry) {
            final String currentUserNickname = entry.getKey();
            final UserInformation currentUser = entry.getValue();

            if (!currentUserNickname.contains(sort.toLowerCase())) {
                continue;
            }
            View item = layoutInflater.inflate(R.layout.item_listview, usersListLayout, false);
            setListFields(currentUser, item);
            usersListLayout.addView(item);

            TextView name = (TextView) item.findViewById(R.id.listview_text2);
            Button button = (Button) item.findViewById(R.id.listview_button);
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getBaseContext(), currentUserNickname, Toast.LENGTH_SHORT).show();
                }
            });

            final Integer currentUserId = currentUser.getUserId();
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button clicked = (Button) v;
                    if (clicked.getText().toString().equals("+")) {
                        addFriend(currentUserId);
                        clicked.setBackgroundColor(GRAY);
                        clicked.setText("—");
                    } else if (clicked.getText().toString().equals("—")) {
                        removeFriend(currentUserId);
                        clicked.setBackgroundColor(GREEN);
                        clicked.setText("+");
                    }
                }
            });
        }
    }

    protected void addFriend(Integer id) {
        user.addFriend(id);
    }

    protected void removeFriend(Integer id) {
        user.removeFriend(id);
    }
}
