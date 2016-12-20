package me.spbau.katyakos.android.museumofme;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ProfileSettingsActivity extends AbstractProfileActivity {

    @InjectView(R.id.profile_sets_save_button)
    Button saveButton;
    @InjectView(R.id.profile_sets_change_photo)
    Button changePhoto;
    @InjectView(R.id.profile_sets_change_header)
    Button changeHeader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        activityId = R.layout.activity_profile_settings;
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                user.setUserBio(getStringEditText(userBio));
                user.setUserName(getStringEditText(nameField));
                user.setUserBirth(getStringEditText(birthField));
                user.setUserAbout(getStringEditText(userAbout));
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    void fieldsInitialization() {
        backButton = (Button) findViewById(R.id.profile_sets_back_button);
        headerImage = (ImageView) findViewById(R.id.profile_sets_header);
        profileImage = (ImageView) findViewById(R.id.profile_sets_photo);
        userBio = (EditText) findViewById(R.id.profile_sets_bio_field);
        nameField = (EditText) findViewById(R.id.profile_sets_name_field);
        birthField = (EditText) findViewById(R.id.profile_sets_birth_field);
        userAbout = (EditText) findViewById(R.id.profile_sets_about_field);
    }

    protected String getStringEditText(EditText textView) {
        return textView.getText().toString();
    }
}
