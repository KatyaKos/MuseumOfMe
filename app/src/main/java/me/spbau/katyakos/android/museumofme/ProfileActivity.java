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

public class ProfileActivity extends Activity {

    private static final int REQUEST_SAVE = 0;

    private Button setsButton;
    private Button menuButton;

    private ImageView headerImage;
    private ImageView profileImage;
    private TextView userNickname;
    private TextView userBio;

    private TextView nameField;
    private TextView birthField;
    private TextView userAbout;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initialization();
        fieldsInitialization();
        buttonsListener();
    }

    private void initialization() {
        headerImage = (ImageView) findViewById(R.id.profile_header);
        profileImage = (ImageView) findViewById(R.id.profile_photo);
        userNickname = (TextView) findViewById(R.id.profile_user_nickname);
        userBio = (TextView) findViewById(R.id.profile_user_bio);

        nameField = (TextView) findViewById(R.id.profile_name_field);
        birthField = (TextView) findViewById(R.id.profile_age_field);
        userAbout = (TextView) findViewById(R.id.profile_about_field);

        setsButton = (Button) findViewById(R.id.profile_button_sets);
        menuButton = (Button) findViewById(R.id.profile_button_menu);
    }

    private void fieldsInitialization() {
        int idHeader = getResources().getIdentifier(UserInformation.getUserHeader(), "drawable", getPackageName());
        headerImage.setImageResource(idHeader);
        int idPhoto = getResources().getIdentifier(UserInformation.getUserPhoto(), "drawable", getPackageName());
        profileImage.setImageResource(idPhoto);

        userNickname.setText(UserInformation.getUserNickname());
        userBio.setText(UserInformation.getUserBio());

        nameField.setText(UserInformation.getUserName());
        birthField.setText(UserInformation.getUserBirth());
        userAbout.setText(UserInformation.getUserAbout());
    }

    private void buttonsListener() {
        menuButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Go to MainActivity
                finish();
                /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);*/
            }
        });

        setsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileSettingsActivity.class);
                startActivityForResult(intent, REQUEST_SAVE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SAVE) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                fieldsInitialization();
            }
        }
    }
}
