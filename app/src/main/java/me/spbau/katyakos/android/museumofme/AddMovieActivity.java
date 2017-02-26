package me.spbau.katyakos.android.museumofme;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class AddMovieActivity extends AbstractAddInterestActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        activityId = R.layout.activity_add_movie;
        type = "movie";
        super.onCreate(savedInstanceState);
    }

    @Override
    void fieldsInitialization() {
        backButton = (Button) findViewById(R.id.add_movie_back_button);
        changePhoto = (Button) findViewById(R.id.add_movie_change_photo);
        saveButton = (Button) findViewById(R.id.add_movie_save_button);
        photo = (ImageView) findViewById(R.id.add_movie_photo);
        nameText = (EditText) findViewById(R.id.add_movie_name);
        author = (EditText) findViewById(R.id.add_movie_author);
        charactersText = (EditText) findViewById(R.id.add_movie_characters);
        review = (EditText) findViewById(R.id.add_movie_review);
        ratingSpinner = (Spinner) findViewById(R.id.add_movie_rating_spinner);
    }
}
