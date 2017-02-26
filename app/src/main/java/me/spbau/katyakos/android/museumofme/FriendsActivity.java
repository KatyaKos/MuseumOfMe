package me.spbau.katyakos.android.museumofme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.view.View.GONE;

public class FriendsActivity extends AbstractFriendActivity {

    @InjectView(R.id.friends_button_add)
    Button addButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        activityId = R.layout.activity_friends;
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        if (!changeable) {
            addButton.setVisibility(GONE);
            return;
        }
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
    void addFriend(String id) {
        user.addFriend(id);
    }

    @Override
    void removeFriend(String id) {
        user.removeFriend(id);
    }

    @Override
    void fieldsInitialization() {
        backButton = (Button) findViewById(R.id.friends_button_menu);
        search = (EditText) findViewById(R.id.friends_search_field);
        listLayout = (LinearLayout) findViewById(R.id.friends_list);
        list = user.getUserFriends();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK || changedInThisActivity) {
            list = user.getUserFriends();
            listLayout.removeAllViews();
            listCreate();
        }
    }

}
