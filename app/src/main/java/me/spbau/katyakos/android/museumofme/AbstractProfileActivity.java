package me.spbau.katyakos.android.museumofme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

abstract class AbstractProfileActivity extends Activity {
    protected Button backButton;

    protected ImageView headerImage;
    protected ImageView profileImage;

    protected EditText userBio;
    protected EditText nameField;
    protected EditText birthField;
    protected EditText userAbout;

    protected Integer userId;
    protected UserInformation user;

    protected int activityId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activityId);

        Intent thisIntent = getIntent();
        userId = thisIntent.getIntExtra("userId", 0);
        user = AllUsersInformation.getUserById(userId);

        fieldsInitialization();
        fieldsRegistration();

        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    abstract void fieldsInitialization();

    protected void fieldsRegistration() {
        registerImage(headerImage, user.getUserHeader());
        registerImage(profileImage, user.getUserPhoto());

        userBio.setText(user.getUserBio());
        nameField.setText(user.getUserName());
        birthField.setText(user.getUserBirth());
        userAbout.setText(user.getUserAbout());
    }

    private void registerImage(ImageView image, String name) {
        int imageId = getResources().getIdentifier(name, "drawable", getPackageName());
        image.setImageResource(imageId);
    }
}
