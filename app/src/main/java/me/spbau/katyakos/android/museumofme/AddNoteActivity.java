package me.spbau.katyakos.android.museumofme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.TreeMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static java.text.DateFormat.SHORT;

public class AddNoteActivity extends Activity {

    @InjectView(R.id.add_note_back_button)
    Button backButton;
    @InjectView(R.id.add_note_add_button)
    Button addButton;
    @InjectView(R.id.add_note_name_field)
    EditText nameText;
    @InjectView(R.id.add_note_content_field)
    EditText contentText;
    @InjectView(R.id.add_note_tags_field)
    EditText tagsText;

    private UserInformation user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        ButterKnife.inject(this);

        Intent thisIntent = getIntent();
        String userId = thisIntent.getStringExtra("userId");
        user = AllUsersInformation.getUserById(userId);

        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isNoteValid()) {
                    return;
                }
                addNoteLogic();
                finish();
            }
        });
    }

    private boolean isNoteValid() {
        if (!validate()) {
            Toast.makeText(getBaseContext(), "Check all fields", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void addNoteLogic() {
        DateFormat df = DateFormat.getDateTimeInstance(SHORT, SHORT);
        String date = df.format(Calendar.getInstance().getTime());

        TreeMap<String, String> note = new TreeMap<>();
        note.put("date", date);
        note.put("name", getStringEditText(nameText));
        note.put("content", getStringEditText(contentText));
        note.put("tags", getStringEditText(tagsText));
        user.addNote(note);
        setResult(RESULT_OK);
    }

    private String getStringEditText(EditText textView) {
        return textView.getText().toString();
    }

    private boolean validate() {
        boolean valid = true;

        String name = getStringEditText(nameText);
        if (name.length() < 3 || name.length() > 30) {
            nameText.setError("from 3 to 30 characters");
            valid = false;
        }

        if (getStringEditText(contentText).isEmpty()) {
            contentText.setError("shouldn't be empty");
            valid = false;
        }

        if (getStringEditText(tagsText).isEmpty()) {
            return valid;
        }
        String[] tags = getStringEditText(tagsText).trim().split("\\s+");
        boolean tagsValid = true;
        for (String tag : tags) {
            if (!tag.equals("") && !tag.startsWith("#")) {
                tagsValid = false;
            }
        }
        if (!tagsValid) {
            tagsText.setError("all tags should start with '#'");
            valid = false;
        }

        return valid;
    }
}
