package me.spbau.katyakos.android.museumofme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ProfileActivity extends Activity {

    @InjectView(R.id.profile_button_sets)
    Button setsButton;
    @InjectView(R.id.profile_button_menu)
    Button menuButton;

    @InjectView(R.id.profile_header)
    ImageView headerImage;
    @InjectView(R.id.profile_photo)
    ImageView profileImage;
    @InjectView(R.id.profile_user_nickname)
    TextView userNickname;
    @InjectView(R.id.profile_user_bio)
    TextView userBio;

    @InjectView(R.id.profile_name_field)
    TextView nameField;
    @InjectView(R.id.profile_age_field)
    TextView birthField;
    @InjectView(R.id.profile_about_field)
    TextView userAbout;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.inject(this);

        fieldsInitialization();

        menuButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileSettingsActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void fieldsInitialization() {
        registerImage(headerImage, UserInformation.getUserHeader());
        registerImage(profileImage, UserInformation.getUserPhoto());

        userNickname.setText(UserInformation.getUserNickname());
        userBio.setText(UserInformation.getUserBio());
        nameField.setText(UserInformation.getUserName());
        birthField.setText(UserInformation.getUserBirth());
        userAbout.setText(UserInformation.getUserAbout());
    }

    private void registerImage(ImageView image, String name) {
        int imageId = getResources().getIdentifier(name, "drawable", getPackageName());
        image.setImageResource(imageId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            fieldsInitialization();
        }
    }
}
