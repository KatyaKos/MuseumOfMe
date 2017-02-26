package me.spbau.katyakos.android.museumofme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.TreeMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DiaryActivity extends AbstractListViewActivity<Integer, UserInformation.Note> {

    @InjectView(R.id.diary_button_add)
    Button addButton;

    public void onCreate(Bundle savedInstanceState) {
        activityId = R.layout.activity_diary;
        listItemId = R.layout.item_diary;
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        ImageView userPhoto = (ImageView) findViewById(R.id.diary_user_photo);
        userPhoto.setImageBitmap(decodeSampledBitmapFromFile(user.getUserPhoto()));
        TextView userNickname = (TextView) findViewById(R.id.diary_user_nickname);
        userNickname.setText(user.getUserNickname());
        TextView userName = (TextView) findViewById(R.id.diary_user_name);
        userName.setText(user.getUserName());

        if (!changeable) {
            addButton.setVisibility(View.GONE);
            return;
        }
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
                intent.putExtra("userId", userId);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    void fieldsInitialization() {
        backButton = (Button) findViewById(R.id.diary_button_menu);
        addButton = (Button) findViewById(R.id.diary_button_add);
        search = (EditText) findViewById(R.id.diary_search_field);
        listLayout = (LinearLayout) findViewById(R.id.diary_note_list);
        list = user.getUserNotes();
    }

    @Override
    boolean fitsSearch(UserInformation.Note note, String sortingString) {
        String tags = note.getTags().toLowerCase();
        return tags.contains(sortingString.toLowerCase());
    }

    @Override
    void setListFields(UserInformation.Note note, View item) {
        TreeMap<String, String> noteContent = note.getNote();
        TextView title = (TextView) item.findViewById(R.id.diary_note_title);
        title.setText(noteContent.get("name"));
        TextView date = (TextView) item.findViewById(R.id.diary_note_date);
        date.setText(noteContent.get("date"));
        TextView content = (TextView) item.findViewById(R.id.diary_note_text);
        content.setText(noteContent.get("content") + "\n\n" + noteContent.get("tags"));
    }

    @Override
    void setOnCLickListeners(UserInformation.Note note, View item) {
        Button delete = (Button) item.findViewById(R.id.diary_note_delete);

        if (!changeable) {
            delete.setVisibility(View.GONE);
        }
        final Integer noteId = note.getId();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.removeNote(noteId);
                list = user.getUserNotes();
                listLayout.removeAllViews();
                listCreate();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            list = user.getUserNotes();
            listLayout.removeAllViews();
            listCreate();
        }
    }
}
