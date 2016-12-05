package me.spbau.katyakos.android.museumofme;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by KatyaKos on 27.11.2016.
 */

public class ProfileSettingsActivity extends Activity {

    private Button backButton;
    private Button saveButton;

    private EditText nicknameText;
    private EditText bioText;
    private EditText nameText;
    private EditText birthText;
    private EditText aboutText;

    private ImageView headerImage;
    private ImageView profileImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        initialize();

        buttonsListener();
    }

    private void initialize() {
        backButton = (Button) findViewById(R.id.profile_sets_back_button);
        saveButton = (Button) findViewById(R.id.profile_sets_save_button);

        nicknameText = (EditText) findViewById(R.id.profile_sets_nickname_field);
        nicknameText.setText(UserInformation.getUserNickname().substring(1));
        bioText = (EditText) findViewById(R.id.profile_sets_bio_field);
        bioText.setText(UserInformation.getUserBio());

        nameText = (EditText) findViewById(R.id.profile_sets_name_field);
        nameText.setText(UserInformation.getUserName());
        birthText = (EditText) findViewById(R.id.profile_sets_birth_field);
        birthText.setText(UserInformation.getUserBirth());
        aboutText = (EditText) findViewById(R.id.profile_sets_about_field);
        aboutText.setText(UserInformation.getUserAbout());
    }

    private void fieldsInitialization() {
        /*int idHeader = getResources().getIdentifier(UserInformation.getUserHeader(), "drawable", getPackageName());
        headerImage.setImageResource(idHeader);
        int idPhoto = getResources().getIdentifier(UserInformation.getUserPhoto(), "drawable", getPackageName());
        profileImage.setImageResource(idPhoto);*/

        nicknameText.setText(UserInformation.getUserNickname());
        bioText.setText(UserInformation.getUserBio());

        nameText.setText(UserInformation.getUserName());
        birthText.setText(UserInformation.getUserBirth());
        aboutText.setText(UserInformation.getUserAbout());
    }

    private void buttonsListener() {
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!validate()) {
                    Toast.makeText(getBaseContext(), "Change nickname", Toast.LENGTH_LONG).show();
                    return;
                }
                UserInformation.setUserNickname(nicknameText.getText().toString());
                UserInformation.setUserBio(bioText.getText().toString());
                UserInformation.setUserName(nameText.getText().toString());
                UserInformation.setUserBirth(birthText.getText().toString());
                UserInformation.setUserAbout(aboutText.getText().toString());
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private boolean validate() {
        String nickname = nicknameText.getText().toString();
        if (nickname.length() < 3) {
            nicknameText.setError("nickname is too short");
            return false;
        } else if (nickname.contains("@")) {
            nicknameText.setError("don't use \"@\" symbols");
            return false;
        } else if (nickname.contains(" ")) {
            nicknameText.setError("don't use spaces");
            return false;
        } else {
            nickname = "@" + nickname;
            if (!nickname.equals(UserInformation.getUserNickname()) && AllUsersInformation.usersContain(nickname)) {
                nicknameText.setError("nickname is busy");
                return false;
            } else {
                nicknameText.setError(null);
            }
        }

        return true;
    }
}
