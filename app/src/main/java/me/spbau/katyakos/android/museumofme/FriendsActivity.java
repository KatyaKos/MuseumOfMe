package me.spbau.katyakos.android.museumofme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FriendsActivity extends AbstractFriendActivity {

    @InjectView(R.id.friends_button_add)
    Button addButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        activityId = R.layout.activity_friends;
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddFriendActivity.class);
                intent.putExtra("userId", userId);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    void fieldsInitialization() {
        backButton = (Button) findViewById(R.id.friends_button_menu);
        userSearch = (EditText) findViewById(R.id.friends_search_field);
        usersListLayout = (LinearLayout) findViewById(R.id.friends_list);
        usersList = user.getUserFriends();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            usersList = user.getUserFriends();
            usersListLayout.removeAllViews();
            listCreate();
        }
    }

}
