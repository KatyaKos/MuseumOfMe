package me.spbau.katyakos.android.museumofme;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class AddBookActivity extends AbstractAddInterestActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        activityId = R.layout.activity_add_book;
        type = "book";
        super.onCreate(savedInstanceState);
    }

    @Override
    void fieldsInitialization() {
        backButton = (Button) findViewById(R.id.add_book_back_button);
        changePhoto = (Button) findViewById(R.id.add_book_change_photo);
        saveButton = (Button) findViewById(R.id.add_book_save_button);
        photo = (ImageView) findViewById(R.id.add_book_photo);
        nameText = (EditText) findViewById(R.id.add_book_name);
        author = (EditText) findViewById(R.id.add_book_author);
        charactersText = (EditText) findViewById(R.id.add_book_characters);
        review = (EditText) findViewById(R.id.add_book_review);
        ratingSpinner = (Spinner) findViewById(R.id.add_book_rating_spinner);
    }
}
