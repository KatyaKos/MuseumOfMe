package me.spbau.katyakos.android.museumofme;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by KatyaKos on 22.11.2016.
 */

public class MoviesActivity extends Activity {

    private Button menuButton;
    private Button addButton;
    private Spinner sortSpinner;
    private ArrayList<String> userMovies;
    LinearLayout moviesList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        initialize();
        buttonsListener();
        moviesListCreate();
    }

    private void initialize() {
        moviesList = (LinearLayout) findViewById(R.id.movies_list);
        menuButton = (Button) findViewById(R.id.movies_button_menu);
        addButton = (Button) findViewById(R.id.movies_button_add);
        sortSpinner = (Spinner) findViewById(R.id.movies_sort);

        userMovies = UserInformation.getUserMovies();

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.spinner_list, R.layout.item_spinner);
        adapter.setDropDownViewResource(R.layout.item_dropdown_spinner);
        sortSpinner.setAdapter(adapter);
    }

    private void buttonsListener() {
        menuButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Go to MainActivity
                finish();
                /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);*/
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getApplicationContext(), AddMovieActivity.class);
                //startActivity(intent);
            }
        });
    }

    private void moviesListCreate() {
        LayoutInflater layoutInflater = getLayoutInflater();

        for (String friend : userMovies) {
            String[] pieces = friend.split("::");
            View item = layoutInflater.inflate(R.layout.item_listview, moviesList, false);

            ImageView photo = (ImageView) item.findViewById(R.id.listview_photo);
            int id = getResources().getIdentifier(pieces[2], "drawable", getPackageName());
            photo.setImageResource(id);

            TextView name = (TextView) item.findViewById(R.id.listview_text1);
            name.setText(pieces[1]);

            Button deleteButton = (Button) item.findViewById(R.id.listview_button);
            deleteButton.setText("—");

            moviesList.addView(item);

            final String[] data = {name.getText().toString()};
            photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getBaseContext(), data[0], Toast.LENGTH_SHORT).show();
                }
            });

            final String movieDate = pieces[0];
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserInformation.removeMovie(movieDate);
                    userMovies = UserInformation.getUserMovies();
                    moviesList.removeAllViews();
                    moviesListCreate();
                }
            });
        }
    }
}
