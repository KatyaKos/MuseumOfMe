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

public class BooksActivity extends Activity {

    private Button menuButton;
    private Button addButton;
    private Spinner sortSpinner;
    private ArrayList<String> userBooks;
    LinearLayout booksList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        initialize();
        buttonsListener();
        booksListCreate();
    }

    private void initialize() {
        booksList = (LinearLayout) findViewById(R.id.books_list);
        menuButton = (Button) findViewById(R.id.books_button_menu);
        addButton = (Button) findViewById(R.id.books_button_add);
        sortSpinner = (Spinner) findViewById(R.id.books_sort);

        userBooks = UserInformation.getUserBooks();

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

    private void booksListCreate() {
        LayoutInflater layoutInflater = getLayoutInflater();

        for (String book : userBooks) {
            String[] pieces = book.split("::");
            View item = layoutInflater.inflate(R.layout.item_listview, booksList, false);

            ImageView photo = (ImageView) item.findViewById(R.id.listview_photo);
            int id = getResources().getIdentifier(pieces[3], "drawable", getPackageName());
            photo.setImageResource(id);

            TextView author = (TextView) item.findViewById(R.id.listview_text2);
            author.setText(pieces[2]);

            TextView name = (TextView) item.findViewById(R.id.listview_text1);
            name.setText(pieces[1]);

            Button deleteButton = (Button) item.findViewById(R.id.listview_button);
            deleteButton.setText("—");

            booksList.addView(item);

            final String[] data = {author.getText().toString(), name.getText().toString()};
            photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getBaseContext(), data[1], Toast.LENGTH_SHORT).show();
                }
            });

            final String bookDate = pieces[0];
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserInformation.removeBook(bookDate);
                    userBooks = UserInformation.getUserBooks();
                    booksList.removeAllViews();
                    booksListCreate();
                }
            });
        }
    }
}
