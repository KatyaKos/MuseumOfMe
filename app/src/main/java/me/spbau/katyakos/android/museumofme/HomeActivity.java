package me.spbau.katyakos.android.museumofme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends MainActivity {
    private final String LOGGED_USER = "logged_user"; //"-1" if no user is in
    private final String PREF_FILE = "loggedUser";
    private SharedPreferences sPref;

    private Intent intentLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sPref = getSharedPreferences(PREF_FILE, MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(LOGGED_USER, userId);
        ed.apply();

        registerButtonForResult(intentProfile, R.id.main_button_profile);

        intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
        Button logout = (Button) findViewById(R.id.main_button_logout);
        logout.setVisibility(View.VISIBLE);
        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sPref = getSharedPreferences(PREF_FILE, MODE_PRIVATE);
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString(LOGGED_USER, "-1");
                ed.apply();
                finish();
                startActivity(intentLogin);
            }
        });
    }

    @Override
    protected Intent registerIntent(Class clazz) {
        Intent intent = super.registerIntent(clazz);
        intent.putExtra("changeable", true);
        return intent;
    }

    @Override
    protected void registerButton(final Intent intent, int id) {
        Button button = (Button) findViewById(id);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!intent.equals(intentHome)) {
                    startActivity(intent);
                }
            }
        });
    }

    private void registerButtonForResult(final Intent intent, int id) {
        Button button = (Button) findViewById(id);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            fieldsInitialization();
        }
    }
}
