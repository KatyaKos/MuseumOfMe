package me.spbau.katyakos.android.museumofme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import static android.view.View.GONE;

abstract class AbstractViewInterestActivity extends Activity {
    protected Button backButton;
    protected ImageView photo;
    protected TextView nameText;
    protected TextView author;
    protected TextView charactersText;
    protected TextView review;
    protected TextView rating;
    protected TextView authorDisclaimer;
    protected TextView charactersDisclaimer;
    protected TextView reviewDisclaimer;

    protected int activityId;
    protected String type;

    private UserInformation.Interest interest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activityId);

        Intent thisIntent = getIntent();
        Integer userId = thisIntent.getIntExtra("userId", 0);
        Integer interestId = thisIntent.getIntExtra("interestId", 0);
        UserInformation user = AllUsersInformation.getUserById(userId);
        interest = user.getInterestById(type, interestId);

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
        registerImage(photo, interest.getPhoto());

        nameText.setText(interest.getName());
        rating.setText(String.valueOf(interest.getRating()));
        registerText(author, authorDisclaimer, interest.getAuthor());
        String characters = TextUtils.join("; ", interest.getCharacters());
        registerText(charactersText, charactersDisclaimer, characters);
        registerText(review, reviewDisclaimer, interest.getReview());
    }

    private void registerImage(ImageView image, String name) {
        int imageId = getResources().getIdentifier(name, "drawable", getPackageName());
        image.setImageResource(imageId);
    }

    private void registerText(TextView view, TextView disclaimer, String content) {
        if (content.isEmpty()) {
            disclaimer.setVisibility(GONE);
            view.setVisibility(GONE);
        }
        view.setText(content);
    }
}
