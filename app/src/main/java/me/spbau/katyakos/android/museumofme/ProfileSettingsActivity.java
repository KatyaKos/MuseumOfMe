package me.spbau.katyakos.android.museumofme;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.InjectView;

public class ProfileSettingsActivity extends AbstractProfileActivity {

    @InjectView(R.id.profile_sets_save_button)
    Button saveButton;
    @InjectView(R.id.profile_sets_change_photo)
    Button changePhoto;
    @InjectView(R.id.profile_sets_change_header)
    Button changeHeader;

    private String header;
    private String photo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        activityId = R.layout.activity_profile_settings;
        super.onCreate(savedInstanceState);

        header = user.getUserHeader();
        photo = user.getUserPhoto();

        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!validate()) {
                    return;
                }
                user.setUserHeader(header);
                user.setUserPhoto(photo);
                user.setUserBio(getStringEditText(userBio));
                user.setUserName(getStringEditText(nameField));
                user.setUserBirth(getStringEditText(birthField));
                user.setUserAbout(getStringEditText(userAbout));
                setResult(RESULT_OK);
                finish();
            }
        });

        changeHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeHeader.setClickable(false);
                changePhoto.setClickable(false);
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 1);
            }
        });

        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeHeader.setClickable(false);
                changePhoto.setClickable(false);
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 2);
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
                    if (requestCode == 1) {
                        header = cursor.getString(columnIndex);
                        headerImage.setImageBitmap(decodeSampledBitmapFromFile(header));
                    } else {
                        photo = cursor.getString(columnIndex);
                        profileImage.setImageBitmap(decodeSampledBitmapFromFile(photo));
                    }
                    cursor.close();
                }
            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Loading went wrong", Toast.LENGTH_LONG).show();
        }
        changeHeader.setClickable(true);
        changePhoto.setClickable(true);
    }

    private boolean validate() {
        if (getStringEditText(nameField).length() < 3) {
            nameField.setError("at least 3 letters");
            nameField.requestFocus();
            return false;
        }
        return true;
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
