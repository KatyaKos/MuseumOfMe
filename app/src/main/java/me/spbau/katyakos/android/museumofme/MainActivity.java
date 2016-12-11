package me.spbau.katyakos.android.museumofme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

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

        registerButton(MainActivity.class, R.id.main_button_menu);
        registerButtonForResult(ProfileActivity.class, R.id.main_button_profile);
        registerButton(FriendsActivity.class, R.id.main_button_friends);
        registerButton(DiaryActivity.class, R.id.main_button_diary);
        registerButton(MapActivity.class, R.id.main_button_map);
        registerButton(MoviesActivity.class, R.id.main_button_movies);
        registerButton(BooksActivity.class, R.id.main_button_books);

        fieldsInitialization();

        //buttonsListeners();
    }

    private void registerButton(final Class<?> clazz, int id) {
        final Intent intent = new Intent(getApplicationContext(), clazz);
        Button button = (Button) findViewById(id);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (clazz.equals(MainActivity.class)) {
                    finish();
                }
                startActivity(intent);
            }
        });
    }

    private void registerButtonForResult(Class<?> clazz, int id) {
        final Intent intent = new Intent(getApplicationContext(), clazz);
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
        TextView userNickname = (TextView) findViewById(R.id.main_user_nickname);
        TextView userName = (TextView) findViewById(R.id.main_user_name);

        int idUserPhoto = getResources().getIdentifier(UserInformation.getUserPhoto(), "drawable", getPackageName());
        userPhoto.setImageResource(idUserPhoto);
        userNickname.setText(UserInformation.getUserNickname());
        userName.setText(UserInformation.getUserName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            fieldsInitialization();
        }
    }
}
