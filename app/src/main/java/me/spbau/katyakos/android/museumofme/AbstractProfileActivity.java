package me.spbau.katyakos.android.museumofme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.ButterKnife;

abstract class AbstractProfileActivity extends Activity {
    protected Button backButton;

    protected ImageView headerImage;
    protected ImageView profileImage;

    protected EditText userBio;
    protected EditText nameField;
    protected EditText birthField;
    protected EditText userAbout;

    protected String userId;
    protected UserInformation user;

    protected int activityId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activityId);
        ButterKnife.inject(this);

        Intent thisIntent = getIntent();
        userId = thisIntent.getStringExtra("userId");
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
        image.setImageBitmap(decodeSampledBitmapFromFile(name));
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
