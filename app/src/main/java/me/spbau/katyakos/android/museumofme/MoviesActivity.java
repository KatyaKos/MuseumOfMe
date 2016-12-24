package me.spbau.katyakos.android.museumofme;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class MoviesActivity extends AbstractInterestActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        activityId = R.layout.activity_movies;
        //addInterestClass = AddMovieActivity.class;
        super.onCreate(savedInstanceState);
    }

    @Override
    void fieldsInitialization() {
        listLayout = (LinearLayout) findViewById(R.id.movies_list);
        list = user.getUserMovies();
        backButton = (Button) findViewById(R.id.movies_button_menu);
        search = (EditText) findViewById(R.id.movies_search_field);
        addButton = (Button) findViewById(R.id.movies_button_add);
        sortSpinner = (Spinner) findViewById(R.id.movies_sort);
    }

    @Override
    void setOnCLickListeners(UserInformation.Interest value, View item) {
        super.setOnCLickListeners(value, item);

        Button deleteButton = (Button) item.findViewById(R.id.listview_button);
        final Integer movieId = value.getId();
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.removeMovie(movieId);
                list = user.getUserMovies();
                listLayout.removeAllViews();
                listCreate();
            }
        });
    }
}
