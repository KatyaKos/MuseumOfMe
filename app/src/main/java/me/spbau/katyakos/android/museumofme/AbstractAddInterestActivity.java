package me.spbau.katyakos.android.museumofme;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

abstract class AbstractAddInterestActivity extends Activity {
    protected Button backButton;
    protected ImageView photo;
    protected Button changePhoto;
    protected EditText nameText;
    protected EditText author;
    protected EditText charactersText;
    protected EditText review;
    protected Spinner ratingSpinner;
    protected Button saveButton;
    protected int activityId;
    protected String type;
    private Float rating;
    private UserInformation user;
    private String image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activityId);

        Intent thisIntent = getIntent();
        String userId = thisIntent.getStringExtra("userId");
        user = AllUsersInformation.getUserById(userId);

        fieldsInitialization();

        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isInterestValid()) {
                    return;
                }
                addInterestLogic();
                finish();
            }
        });

        changePhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                changePhoto.setClickable(false);
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 1);
            }
        });

        ratingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View itemSelected, int selectedItemPosition, long selectedId) {
                String selected = parent.getItemAtPosition(selectedItemPosition).toString();
                rating = Float.valueOf(selected);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    image = cursor.getString(columnIndex);
                    cursor.close();
                    photo.setImageBitmap(decodeSampledBitmapFromFile(image));
                }
            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Loading went wrong", Toast.LENGTH_LONG).show();
        }
        changePhoto.setClickable(true);
    }

    abstract void fieldsInitialization();

    private boolean isInterestValid() {
        if (!validate()) {
            Toast.makeText(getBaseContext(), "Check all fields", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void addInterestLogic() {
        TreeMap<String, String> interest = new TreeMap<>();
        interest.put("name", getStringEditText(nameText));
        interest.put("authorName", getStringEditText(author));
        interest.put("review", getStringEditText(review));
        interest.put("photo", image);
        String[] charactersContent = getStringEditText(charactersText).split("; ");
        ArrayList<String> characters = new ArrayList<>(Arrays.asList(charactersContent));
        user.addInterest(type, interest, rating, characters);
        setResult(RESULT_OK);
    }

    private String getStringEditText(EditText textView) {
        return textView.getText().toString();
    }

    private boolean validate() {
        boolean valid = true;

        String name = getStringEditText(nameText);
        if (name.isEmpty()) {
            nameText.setError("enter name");
            valid = false;
        }

        return valid;
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
