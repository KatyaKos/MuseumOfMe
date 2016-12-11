package me.spbau.katyakos.android.museumofme;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by KatyaKos on 26.11.2016.
 */

public class AddNoteActivity extends Activity {

    private Button backButton;
    private Button addButton;
    private EditText nameText;
    private EditText contentText;
    private EditText tagsText;
    private String note;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        backButton = (Button) findViewById(R.id.add_note_back_button);
        addButton = (Button) findViewById(R.id.add_note_add_button);
        nameText = (EditText) findViewById(R.id.add_note_name_field);
        contentText = (EditText) findViewById(R.id.add_note_content_field);
        tagsText = (EditText) findViewById(R.id.add_note_tags_field);

        buttonsListener();
    }

    private void buttonsListener() {
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!validate()) {
                    Toast.makeText(getBaseContext(), "Check all fields", Toast.LENGTH_LONG).show();
                    return;
                }
                DateFormat df = new SimpleDateFormat("yy.MM.dd 'at' HH:mm");
                String date = df.format(Calendar.getInstance().getTime());
                note = date + "::" + nameText.getText().toString() +
                        "::" + contentText.getText().toString() + "::" + tagsText.getText().toString() + "\0";
                UserInformation.addNote(note);
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private boolean validate() {
        boolean valid = true;

        if (nameText.getText().toString().isEmpty()) {
            nameText.setError("name is required");
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (contentText.getText().toString().isEmpty()) {
            contentText.setError("write something");
            valid = false;
        } else {
            contentText.setError(null);
        }

        if (tagsText.getText().toString().isEmpty()) {
            tagsText.setError(null);
            return valid;
        }
        String[] tags = tagsText.getText().toString().trim().split("\\s+");
        boolean tagsValid = true;
        for (String tag : tags) {
            if (!tag.equals("") && !tag.startsWith("#")) {
                tagsValid = false;
            }
        }
        if (!tagsValid) {
            tagsText.setError("all tags should start with '#'");
            valid = false;
        } else {
            tagsText.setError(null);
        }

        return valid;
    }
}
