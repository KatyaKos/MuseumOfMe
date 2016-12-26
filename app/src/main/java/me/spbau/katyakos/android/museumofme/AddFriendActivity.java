package me.spbau.katyakos.android.museumofme;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import static android.graphics.Color.GRAY;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.WHITE;

public class AddFriendActivity extends AbstractFriendActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        activityId = R.layout.activity_add_friend;
        super.onCreate(savedInstanceState);
    }

    @Override
    void fieldsInitialization() {
        search = (EditText) findViewById(R.id.add_friends_search_field);
        backButton = (Button) findViewById(R.id.add_friends_button_back);
        list = AllUsersInformation.getUsersListByNickname();
        listLayout = (LinearLayout) findViewById(R.id.add_friends_list);
    }

    @Override
    protected void setListFields(UserInformation currentUser, View item) {
        super.setListFields(currentUser, item);
        Button button = (Button) item.findViewById(R.id.listview_button);

        if (userId.equals(currentUser.getUserId())) {
            button.setBackgroundColor(WHITE);
        } else if (user.hasFriend(currentUser.getUserNickname())) {
            button.setBackgroundColor(GRAY);
            button.setText("—");
        } else {
            button.setBackgroundColor(GREEN);
            button.setText("+");
        }
    }

    @Override
    protected void addFriend(Integer id) {
        user.addFriend(id);
        setResult(RESULT_OK);
    }

    @Override
    protected void removeFriend(Integer id) {
        user.removeFriend(id);
        setResult(RESULT_OK);
    }

}
