package me.spbau.katyakos.android.museumofme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

abstract class AbstractInterestActivity extends AbstractListViewActivity<Integer, UserInformation.Interest> {
    protected Button addButton;
    protected Spinner sortSpinner;
    protected Class addInterestClass;
    protected Class viewInterestClass;

    private ArrayAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        listItemId = R.layout.item_listview;
        super.onCreate(savedInstanceState);

        adapter = ArrayAdapter.createFromResource(this, R.array.spinner_list, R.layout.item_spinner);
        adapter.setDropDownViewResource(R.layout.item_dropdown_spinner);
        setSpinner();

        if (!changeable) {
            addButton.setVisibility(View.GONE);
            return;
        }
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), addInterestClass);
                intent.putExtra("userId", userId);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void setSpinner() {
        sortSpinner.setAdapter(adapter);
    }

    @Override
    abstract void fieldsInitialization();

    @Override
    boolean fitsSearch(UserInformation.Interest value, String sortingString) {
        sortingString = sortingString.toLowerCase();
        return value.getName().contains(sortingString) || value.getAuthor().contains(sortingString);
    }

    @Override
    void setListFields(UserInformation.Interest value, View item) {
        ImageView photo = (ImageView) item.findViewById(R.id.listview_photo);
        photo.setImageBitmap(value.getPhoto());

        TextView author = (TextView) item.findViewById(R.id.listview_text2);
        author.setVisibility(View.VISIBLE);
        author.setText(value.getAuthor());

        TextView name = (TextView) item.findViewById(R.id.listview_text1);
        name.setText(value.getName());

        Button deleteButton = (Button) item.findViewById(R.id.listview_button);
        deleteButton.setText("—");
    }

    @Override
    void setOnCLickListeners(final UserInformation.Interest value, View item) {
        ImageView photo = (ImageView) item.findViewById(R.id.listview_photo);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), viewInterestClass);
                intent.putExtra("userId", userId);
                intent.putExtra("interestId", value.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            listCreate();
            setSpinner();
            search.setText("");
        }
    }
}
