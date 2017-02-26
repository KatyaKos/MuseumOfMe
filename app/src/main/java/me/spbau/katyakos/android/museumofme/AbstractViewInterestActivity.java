package me.spbau.katyakos.android.museumofme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
        String userId = thisIntent.getStringExtra("userId");
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

    private void registerImage(ImageView image, Bitmap photo) {
        image.setImageBitmap(photo);
    }

    private void registerText(TextView view, TextView disclaimer, String content) {
        if (content.isEmpty()) {
            disclaimer.setVisibility(GONE);
            view.setVisibility(GONE);
        }
        view.setText(content);
    }

    protected Bitmap decodeSampledBitmapFromFile(String file) {
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
}
