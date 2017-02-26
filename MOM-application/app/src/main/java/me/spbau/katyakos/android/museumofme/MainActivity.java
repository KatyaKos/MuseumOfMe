package me.spbau.katyakos.android.museumofme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

    protected Intent intentHome;
    protected Intent intentProfile;
    protected Intent intentFriends;
    protected Intent intentDiary;
    protected Intent intentMap;
    protected Intent intentMovies;
    protected Intent intentBooks;

    protected String userId;
    protected UserInformation user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent thisIntent = getIntent();
        userId = thisIntent.getStringExtra("userId");
        AllUsersInformation.downloadUserById(getApplicationContext(), userId);
        user = AllUsersInformation.getUserById(userId);

        intentHome = registerIntent(HomeActivity.class);
        intentProfile = registerIntent(ProfileActivity.class);
        intentFriends = registerIntent(FriendsActivity.class);
        intentDiary = registerIntent(DiaryActivity.class);
        intentMap = registerIntent(MapActivity.class);
        intentMovies = registerIntent(MoviesActivity.class);
        intentBooks = registerIntent(BooksActivity.class);

        registerButton(intentHome, R.id.main_button_menu);
        registerButton(intentProfile, R.id.main_button_profile);
        registerButton(intentFriends, R.id.main_button_friends);
        registerButton(intentDiary, R.id.main_button_diary);
        registerButton(intentMap, R.id.main_button_map);
        registerButton(intentMovies, R.id.main_button_movies);
        registerButton(intentBooks, R.id.main_button_books);

        fieldsInitialization();
    }

    protected Intent registerIntent(Class clazz) {
        Intent intent = new Intent(getApplicationContext(), clazz);
        intent.putExtra("userId", userId);
        intent.putExtra("changeable", false);
        return intent;
    }

    protected void registerButton(final Intent intent, int id) {
        Button button = (Button) findViewById(id);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (intent.equals(intentHome)) {
                    finish();
                } else {
                    startActivity(intent);
                }
            }
        });
    }

    protected void fieldsInitialization() {
        ImageView userPhoto = (ImageView) findViewById(R.id.main_user_photo);
        userPhoto.setImageBitmap(user.getUserPhoto());

        TextView userNickname = (TextView) findViewById(R.id.main_user_nickname);
        userNickname.setText(user.getUserNickname());
        TextView userName = (TextView) findViewById(R.id.main_user_name);
        userName.setText(user.getUserName());
    }
}
