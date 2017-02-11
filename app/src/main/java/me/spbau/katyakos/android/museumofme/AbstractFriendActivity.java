package me.spbau.katyakos.android.museumofme;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.graphics.Color.GRAY;
import static android.graphics.Color.GREEN;

abstract class AbstractFriendActivity extends AbstractListViewActivity<String, UserInformation> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        listItemId = R.layout.item_listview;
        super.onCreate(savedInstanceState);
    }

    @Override
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

    @Override
    boolean fitsSearch(UserInformation currentUser, String sortingString) {
        String nickname = currentUser.getUserNickname();
        return nickname.contains(sortingString.toLowerCase());
    }

    @Override
    void setOnCLickListeners(UserInformation currentUser, View item) {
        TextView name = (TextView) item.findViewById(R.id.listview_text2);
        Button button = (Button) item.findViewById(R.id.listview_button);
        final String currentUserNickname = currentUser.getUserNickname();
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), currentUserNickname, Toast.LENGTH_SHORT).show();
            }
        });

        final String currentUserId = currentUser.getUserId();
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

    abstract void addFriend(String id);

    abstract void removeFriend(String id);
}
