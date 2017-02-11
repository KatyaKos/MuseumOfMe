package me.spbau.katyakos.android.museumofme;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
    private final String LOGGED_USER = "logged_user"; //"-1" if no user is in
    private final String PREF_FILE = "loggedUser";
    private SharedPreferences sPref;

    private Intent intentMain;
    private Intent intentProfile;
    private Intent intentFriends;
    private Intent intentDiary;
    private Intent intentMap;
    private Intent intentMovies;
    private Intent intentBooks;

    private String userId;
    private UserInformation user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent thisIntent = getIntent();
        userId = thisIntent.getStringExtra("userId");
        AllUsersInformation.downloadUserById(userId);
        user = AllUsersInformation.getUserById(userId);

        sPref = getSharedPreferences(PREF_FILE, MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(LOGGED_USER, userId);
        ed.apply();

        intentMain = registerIntent(MainActivity.class);
        intentProfile = registerIntent(ProfileActivity.class);
        intentFriends = registerIntent(FriendsActivity.class);
        intentDiary = registerIntent(DiaryActivity.class);
        intentMap = registerIntent(MapActivity.class);
        intentMovies = registerIntent(MoviesActivity.class);
        intentBooks = registerIntent(BooksActivity.class);

        registerButton(intentMain, R.id.main_button_menu);
        registerButtonForResult(intentProfile, R.id.main_button_profile);
        registerButton(intentFriends, R.id.main_button_friends);
        registerButton(intentDiary, R.id.main_button_diary);
        registerButton(intentMap, R.id.main_button_map);
        registerButton(intentMovies, R.id.main_button_movies);
        registerButton(intentBooks, R.id.main_button_books);

        fieldsInitialization();
    }

    private Intent registerIntent(Class clazz) {
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
        userPhoto.setImageBitmap(decodeSampledBitmapFromFile(user.getUserPhoto()));

        TextView userNickname = (TextView) findViewById(R.id.main_user_nickname);
        userNickname.setText(user.getUserNickname());
        TextView userName = (TextView) findViewById(R.id.main_user_name);
        userName.setText(user.getUserName());
    }

    private Bitmap decodeSampledBitmapFromFile(String file) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, options);

        int reqWidth = 140;
        int reqHeight = 140;
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file, options);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            fieldsInitialization();
        }
    }
}
