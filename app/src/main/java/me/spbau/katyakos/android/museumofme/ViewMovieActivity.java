package me.spbau.katyakos.android.museumofme;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewMovieActivity extends AbstractViewInterestActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        activityId = R.layout.activity_view_movie;
        type = "movie";
        super.onCreate(savedInstanceState);
    }

    @Override
    void fieldsInitialization() {
        backButton = (Button) findViewById(R.id.view_movie_back_button);
        photo = (ImageView) findViewById(R.id.view_movie_photo);
        nameText = (TextView) findViewById(R.id.view_movie_name);
        author = (TextView) findViewById(R.id.view_movie_author);
        charactersText = (TextView) findViewById(R.id.view_movie_characters);
        review = (TextView) findViewById(R.id.view_movie_review);
        rating = (TextView) findViewById(R.id.view_movie_rating);
        authorDisclaimer = (TextView) findViewById(R.id.view_movie_author_disclaimer);
        reviewDisclaimer = (TextView) findViewById(R.id.view_movie_review_disclaimer);
        charactersDisclaimer = (TextView) findViewById(R.id.view_movie_characters_disclaimer);
    }
}
