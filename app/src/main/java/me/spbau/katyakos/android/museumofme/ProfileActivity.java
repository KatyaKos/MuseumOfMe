package me.spbau.katyakos.android.museumofme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.InjectView;

public class ProfileActivity extends AbstractProfileActivity {

    @InjectView(R.id.profile_button_sets)
    Button setsButton;
    @InjectView(R.id.profile_user_nickname)
    EditText userNickname;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        activityId = R.layout.activity_profile;
        super.onCreate(savedInstanceState);

        setsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileSettingsActivity.class);
                intent.putExtra("userId", userId);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    void fieldsInitialization() {
        backButton = (Button) findViewById(R.id.profile_button_menu);
        headerImage = (ImageView) findViewById(R.id.profile_header);
        profileImage = (ImageView) findViewById(R.id.profile_photo);
        userBio = (EditText) findViewById(R.id.profile_user_bio);
        nameField = (EditText) findViewById(R.id.profile_name_field);
        birthField = (EditText) findViewById(R.id.profile_age_field);
        userAbout = (EditText) findViewById(R.id.profile_about_field);
    }

    @Override
    protected void fieldsRegistration() {
        super.fieldsRegistration();
        userNickname.setText(user.getUserNickname());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            fieldsRegistration();
        }
    }
}
