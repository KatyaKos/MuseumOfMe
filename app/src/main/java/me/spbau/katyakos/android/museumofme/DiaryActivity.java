package me.spbau.katyakos.android.museumofme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by KatyaKos on 20.11.2016.
 */

public class DiaryActivity extends Activity {

    private static final int REQUEST_ADD = 0;

    private Button menuButton;
    private Button addButton;
    private EditText noteSearch;
    private LinearLayout notesList;
    private ArrayList<String> userNotes;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        fieldsInitialization();

        notesListCreate("");

        buttonsOnClick();
    }

    private void fieldsInitialization() {
        menuButton = (Button) findViewById(R.id.diary_button_menu);
        addButton = (Button) findViewById(R.id.diary_button_add);
        noteSearch = (EditText) findViewById(R.id.diary_search_field);
        notesList = (LinearLayout) findViewById(R.id.diary_note_list);

        userNotes = UserInformation.getUserNotes();


        ImageView userPhoto = (ImageView) findViewById(R.id.diary_user_photo);
        TextView userNickname = (TextView) findViewById(R.id.diary_user_nickname);
        TextView userName = (TextView) findViewById(R.id.diary_user_name);

        int id = getResources().getIdentifier(UserInformation.getUserPhoto(), "drawable", getPackageName());
        userPhoto.setImageResource(id);
        userNickname.setText(UserInformation.getUserNickname());
        userName.setText(UserInformation.getUserName());
    }

    private void buttonsOnClick() {
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
                Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
                startActivityForResult(intent, REQUEST_ADD);
            }
        });

        noteSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                notesList.removeAllViews();
                notesListCreate(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }

    private void notesListCreate(CharSequence sort) {
        LayoutInflater layoutInflater = getLayoutInflater();

        for (String note : userNotes) {
            String[] pieces = note.split("::");
            if (!pieces[3].toLowerCase().contains(sort.toString().toLowerCase())) {
                continue;
            }
            View item = layoutInflater.inflate(R.layout.item_diary, notesList, false);

            TextView title = (TextView) item.findViewById(R.id.diary_note_title);
            title.setText(pieces[1]);
            TextView date = (TextView) item.findViewById(R.id.diary_note_date);
            date.setText(pieces[0]);
            TextView content = (TextView) item.findViewById(R.id.diary_note_text);
            content.setText(pieces[2] + "\n\n" + pieces[3]);

            Button delete = (Button) item.findViewById(R.id.diary_note_delete);

            notesList.addView(item);

            final String noteDate = pieces[0];
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserInformation.removeNote(noteDate);
                    userNotes = UserInformation.getUserNotes();
                    notesList.removeAllViews();
                    notesListCreate("");
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ADD) {
            if (resultCode == RESULT_OK) {
                userNotes = UserInformation.getUserNotes();
                notesList.removeAllViews();
                notesListCreate("");
            }
        }
    }
}
