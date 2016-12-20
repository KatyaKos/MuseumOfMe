package me.spbau.katyakos.android.museumofme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private Intent intentMain = registerIntent(MainActivity.class);
    private Intent intentProfile = registerIntent(ProfileActivity.class);
    private Intent intentFriends = registerIntent(FriendsActivity.class);
    private Intent intentDiary = registerIntent(DiaryActivity.class);
    private Intent intentMap = registerIntent(MapActivity.class);
    private Intent intentMovies = registerIntent(MoviesActivity.class);
    private Intent intentBooks = registerIntent(BooksActivity.class);

    private Integer userId;
    private UserInformation user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent thisIntent = getIntent();
        userId = thisIntent.getIntExtra("userId", 0);
        user = AllUsersInformation.getUserById(userId);

        registerButton(intentMain, R.id.main_button_menu);
        registerButtonForResult(intentProfile, R.id.main_button_profile);
        registerButton(intentFriends, R.id.main_button_friends);
        registerButton(intentDiary, R.id.main_button_diary);
        registerButton(intentMap, R.id.main_button_map);
        registerButton(intentMovies, R.id.main_button_movies);
        registerButton(intentBooks, R.id.main_button_books);

        fieldsInitialization();
    }

    private Intent registerIntent(Class<?> clazz) {
        Intent intent = new Intent(getApplicationContext(), clazz);
        intent.putExtra("userId", userId);
        return intent;
    }

    private void registerButton(final Intent intent, int id) {
        Button button = (Button) findViewById(id);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (intent.equals(intentMain)) {
                    finish();
                }
                startActivity(intent);
            }
        });
    }

    private void registerButtonForResult(final Intent intent, int id) {
        Button button = (Button) findViewById(id);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivityForResult(intent, 1);
            }
        });
    }

    private void fieldsInitialization() {
        ImageView userPhoto = (ImageView) findViewById(R.id.main_user_photo);
        int idUserPhoto = getResources().getIdentifier(user.getUserPhoto(), "drawable", getPackageName());
        userPhoto.setImageResource(idUserPhoto);

        TextView userNickname = (TextView) findViewById(R.id.main_user_nickname);
        userNickname.setText(user.getUserNickname());
        TextView userName = (TextView) findViewById(R.id.main_user_name);
        userName.setText(user.getUserName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            fieldsInitialization();
        }
    }
}
