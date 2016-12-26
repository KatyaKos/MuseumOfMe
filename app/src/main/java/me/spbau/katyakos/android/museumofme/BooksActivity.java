package me.spbau.katyakos.android.museumofme;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class BooksActivity extends AbstractInterestActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        activityId = R.layout.activity_books;
        addInterestClass = AddBookActivity.class;
        viewInterestClass = ViewBookActivity.class;
        super.onCreate(savedInstanceState);
    }

    @Override
    void fieldsInitialization() {
        listLayout = (LinearLayout) findViewById(R.id.books_list);
        list = user.getUserBooks();
        backButton = (Button) findViewById(R.id.books_button_menu);
        search = (EditText) findViewById(R.id.books_search_field);
        addButton = (Button) findViewById(R.id.books_button_add);
        sortSpinner = (Spinner) findViewById(R.id.books_sort);
    }

    @Override
    void setOnCLickListeners(UserInformation.Interest value, View item) {
        super.setOnCLickListeners(value, item);

        Button deleteButton = (Button) item.findViewById(R.id.listview_button);
        final Integer bookId = value.getId();
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.removeBook(bookId);
                list = user.getUserBooks();
                listLayout.removeAllViews();
                listCreate();
            }
        });
    }
}
