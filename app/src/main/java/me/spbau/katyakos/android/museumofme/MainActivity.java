package me.spbau.katyakos.android.museumofme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by KatyaKos on 14.11.2016.
 */

public class MainActivity extends Activity {

    private static final int REQUEST_CHANGE = 0;

    private Intent intentMain;
    private Intent intentProfile;
    private Intent intentFriends;
    private Intent intentDiary;
    private Intent intentMap;
    private Intent intentMovies;
    private Intent intentBooks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialization();
        fieldsInitialization();

        buttonsListener();
    }

    private void initialization() {
        intentMain = new Intent(getApplicationContext(), MainActivity.class);
        intentProfile = new Intent(getApplicationContext(), ProfileActivity.class);
        intentFriends = new Intent(getApplicationContext(), FriendsActivity.class);
        intentDiary = new Intent(getApplicationContext(), DiaryActivity.class);
        intentMap = new Intent(getApplicationContext(), MapActivity.class);
        intentMovies = new Intent(getApplicationContext(), MoviesActivity.class);
        intentBooks = new Intent(getApplicationContext(), BooksActivity.class);
    }

    private void fieldsInitialization() {
        ImageView userPhoto = (ImageView) findViewById(R.id.main_user_photo);
        TextView userNickname = (TextView) findViewById(R.id.main_user_nickname);
        TextView userName = (TextView) findViewById(R.id.main_user_name);

        int id = getResources().getIdentifier(UserInformation.getUserPhoto(), "drawable", getPackageName());
        userPhoto.setImageResource(id);
        userNickname.setText(UserInformation.getUserNickname());
        userName.setText(UserInformation.getUserName());
    }

    private void buttonsListener() {
        Button profileButton = (Button) findViewById(R.id.main_button_profile);
        Button friendsButton = (Button) findViewById(R.id.main_button_friends);
        Button diaryButton = (Button) findViewById(R.id.main_button_diary);
        Button mapButton = (Button) findViewById(R.id.main_button_map);
        Button moviesButton = (Button) findViewById(R.id.main_button_movies);
        Button booksButton = (Button) findViewById(R.id.main_button_books);
        Button menuButton = (Button) findViewById(R.id.main_button_menu);

        menuButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Go to MainActivity
                finish();
                startActivity(intentMain);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Go to ProfileActivity
                startActivityForResult(intentProfile, REQUEST_CHANGE);
            }
        });

        friendsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Go to FriendsActivity
                startActivity(intentFriends);
            }
        });

        diaryButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Go to DiaryActivity
                startActivity(intentDiary);
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Go to MapActivity
                startActivity(intentMap);
            }
        });

        moviesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Go to MoviesActivity
                startActivity(intentMovies);
            }
        });

        booksButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Go to BooksActivity
                startActivity(intentBooks);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CHANGE) {
            if (resultCode == RESULT_OK) {
                fieldsInitialization();
            }
        }
    }
}
