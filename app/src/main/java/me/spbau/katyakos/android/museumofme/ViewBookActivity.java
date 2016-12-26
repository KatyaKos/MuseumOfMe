package me.spbau.katyakos.android.museumofme;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewBookActivity extends AbstractViewInterestActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        activityId = R.layout.activity_view_book;
        type = "book";
        super.onCreate(savedInstanceState);
    }

    @Override
    void fieldsInitialization() {
        backButton = (Button) findViewById(R.id.view_book_back_button);
        photo = (ImageView) findViewById(R.id.view_book_photo);
        nameText = (TextView) findViewById(R.id.view_book_name);
        author = (TextView) findViewById(R.id.view_book_author);
        charactersText = (TextView) findViewById(R.id.view_book_characters);
        review = (TextView) findViewById(R.id.view_book_review);
        rating = (TextView) findViewById(R.id.view_book_rating);
        authorDisclaimer = (TextView) findViewById(R.id.view_book_author_disclaimer);
        reviewDisclaimer = (TextView) findViewById(R.id.view_book_review_disclaimer);
        charactersDisclaimer = (TextView) findViewById(R.id.view_book_characters_disclaimer);
    }
}
