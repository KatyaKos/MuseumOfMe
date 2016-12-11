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

import java.util.ArrayList;

/**
 * Created by KatyaKos on 15.11.2016.
 */

public class FriendsActivity extends Activity {

    private static final int REQUEST_ADD = 0;

    Button menuButton;
    Button addButton;
    EditText friendSearch;
    LinearLayout friendsList;

    private ArrayList<String> userFriends = UserInformation.getUserFriends();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        initialize();

        friendsListCreate("");
        buttonsOnClick();
    }

    private void initialize() {
        menuButton = (Button) findViewById(R.id.friends_button_menu);
        addButton = (Button) findViewById(R.id.friends_button_add);
        friendSearch = (EditText) findViewById(R.id.friends_search_field);
        friendsList = (LinearLayout) findViewById(R.id.friends_list);
    }

    private void friendsListCreate(CharSequence sort) {
        LayoutInflater layoutInflater = getLayoutInflater();

        for (String friend : userFriends) {
            String[] pieces = friend.split("::");
            if (!(pieces[0].toLowerCase().contains(sort.toString().toLowerCase()) ||
                    pieces[1].toLowerCase().contains(sort.toString().toLowerCase()))) {
                continue;
            }
            View item = layoutInflater.inflate(R.layout.item_listview, friendsList, false);

            ImageView photo = (ImageView) item.findViewById(R.id.listview_photo);
            int id = getResources().getIdentifier(pieces[2], "drawable", getPackageName());
            photo.setImageResource(id);

            TextView nickname = (TextView) item.findViewById(R.id.listview_text1);
            nickname.setText(pieces[0]);

            TextView name = (TextView) item.findViewById(R.id.listview_text2);
            name.setText(pieces[1]);
            name.setVisibility(View.VISIBLE);

            Button button = (Button) item.findViewById(R.id.listview_button);
            button.setClickable(false);
            button.setVisibility(View.GONE);

            friendsList.addView(item);

            final String friendName = name.getText().toString();
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getBaseContext(), friendName, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void buttonsOnClick() {
        menuButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Go to MainActivity
                finish();
                /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);*/
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddFriendActivity.class);
                startActivityForResult(intent, REQUEST_ADD);
            }
        });

        friendSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                friendsList.removeAllViews();
                friendsListCreate(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ADD) {
            if (resultCode == RESULT_OK) {
                userFriends = UserInformation.getUserFriends();
                friendsList.removeAllViews();
                friendsListCreate("");
            }
        }
    }

}
