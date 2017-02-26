package me.spbau.katyakos.android.museumofme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.graphics.Color.GRAY;
import static android.graphics.Color.GREEN;

abstract class AbstractFriendActivity extends AbstractListViewActivity<String, UserInformation> {

    protected Boolean changedInThisActivity = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        listItemId = R.layout.item_listview;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setListFields(UserInformation currentUser, View item) {
        ImageView photo = (ImageView) item.findViewById(R.id.listview_photo);
        photo.setImageBitmap(currentUser.getUserPhoto());

        TextView nickname = (TextView) item.findViewById(R.id.listview_text1);
        nickname.setText(currentUser.getUserNickname());

        TextView name = (TextView) item.findViewById(R.id.listview_text2);
        name.setText(currentUser.getUserName());
        name.setVisibility(View.VISIBLE);

        Button button = (Button) item.findViewById(R.id.listview_button);
        if (!changeable) {
            button.setVisibility(View.GONE);
            return;
        }
        button.setBackgroundColor(GRAY);
        button.setText("—");
    }

    @Override
    boolean fitsSearch(UserInformation currentUser, String sortingString) {
        String nickname = currentUser.getUserNickname();
        return nickname.contains(sortingString.toLowerCase());
    }

    @Override
    void setOnCLickListeners(UserInformation currentUser, View item) {
        RelativeLayout showUser = (RelativeLayout) item.findViewById(R.id.listview_clickable_space);
        Button button = (Button) item.findViewById(R.id.listview_button);

        final String currentUserNickname = currentUser.getUserNickname();
        showUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("userId", AllUsersInformation.getIdByNickname(currentUserNickname));
                startActivityForResult(intent, 1);
            }
        });

        if (!changeable) {
            button.setVisibility(View.GONE);
            return;
        }
        final String currentUserId = currentUser.getUserId();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button clicked = (Button) v;
                if (clicked.getText().toString().equals("+")) {
                    changedInThisActivity = false;
                    addFriend(currentUserId);
                    clicked.setBackgroundColor(GRAY);
                    clicked.setText("—");
                } else if (clicked.getText().toString().equals("—")) {
                    changedInThisActivity = true;
                    removeFriend(currentUserId);
                    clicked.setBackgroundColor(GREEN);
                    clicked.setText("+");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    abstract void addFriend(String id);

    abstract void removeFriend(String id);
}
