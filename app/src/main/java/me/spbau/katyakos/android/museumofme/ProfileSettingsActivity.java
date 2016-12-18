package me.spbau.katyakos.android.museumofme;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ProfileSettingsActivity extends Activity {

    @InjectView(R.id.profile_sets_back_button)
    Button backButton;
    @InjectView(R.id.profile_sets_save_button)
    Button saveButton;

    @InjectView(R.id.profile_sets_nickname_field)
    EditText nicknameText;
    @InjectView(R.id.profile_sets_bio_field)
    EditText bioText;

    @InjectView(R.id.profile_sets_name_field)
    EditText nameText;
    @InjectView(R.id.profile_sets_birth_field)
    EditText birthText;
    @InjectView(R.id.profile_sets_about_field)
    EditText aboutText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        ButterKnife.inject(this);

        nicknameText.setText(UserInformation.getUserNickname().substring(1));
        bioText.setText(UserInformation.getUserBio());
        nameText.setText(UserInformation.getUserName());
        birthText.setText(UserInformation.getUserBirth());
        aboutText.setText(UserInformation.getUserAbout());

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
