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
import android.widget.LinearLayout;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

abstract class AbstractListViewActivity<E, T> extends Activity {
    protected Button backButton;
    protected EditText search;
    protected LinearLayout listLayout;
    protected TreeMap<E, T> list;

    protected Integer userId;
    protected UserInformation user;

    protected int activityId;
    protected int listItemId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activityId);

        Intent thisIntent = getIntent();
        userId = thisIntent.getIntExtra("userId", 0);
        user = AllUsersInformation.getUserById(userId);

        fieldsInitialization();

        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                listLayout.removeAllViews();
                listCreate(cs.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        listCreate();
    }

    abstract void fieldsInitialization();

    protected void listCreate() {
        listCreate("");
    }

    protected void listCreate(String sortingString) {
        LayoutInflater layoutInflater = getLayoutInflater();
        Set<Map.Entry<E, T>> usersEntry = list.entrySet();
        for (Map.Entry<E, T> entry : usersEntry) {
            final T value = entry.getValue();
            if (!fitsSearch(value, sortingString)) {
                continue;
            }
            View item = layoutInflater.inflate(listItemId, listLayout, false);
            setListFields(value, item);
            listLayout.addView(item);
            setOnCLickListeners(value, item);
        }
    }

    abstract boolean fitsSearch(T value, String sortingString);

    abstract void setListFields(T value, View item);

    abstract void setOnCLickListeners(T value, View item);
}
