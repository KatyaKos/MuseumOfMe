package me.spbau.katyakos.android.museumofme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AbstractLoginActivity {
    private final String LOGGED_USER = "logged_user"; //"-1" if no user is in
    private final String PREF_FILE = "loggedUser";
    private SharedPreferences sPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        sPref = getSharedPreferences(PREF_FILE, MODE_PRIVATE);
        String savedText = sPref.getString(LOGGED_USER, "-1");
        if (!savedText.equals("-1")) {
            loggedUserSuccess(savedText);
        }

        activityId = R.layout.activity_login;
        super.onCreate(savedInstanceState);

        passwordText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    verification();
                    return true;
                }
                return false;
            }
        });
    }

    private void loggedUserSuccess(String idString) {
        this.finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("userId", Integer.valueOf(idString));
        startActivity(intent);
    }

    @Override
    void fieldsInitialization() {
        emailText = (EditText) findViewById(R.id.login_email);
        passwordText = (EditText) findViewById(R.id.login_password);
        link = (TextView) findViewById(R.id.login_link_signup);
        button = (Button) findViewById(R.id.login_button);
        onVerificationFailMessage = "Login failed";
    }

    @Override
    void linkClick() {
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    void onVerificationSuccess() {
        String email = getStringTextView(emailText);
        Integer userId = AllUsersInformation.getIdByEmail(email);
        this.finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    @Override
    boolean checkFields() {
        String email = getStringTextView(emailText);
        String password = getStringTextView(passwordText);
        return AllUsersInformation.checkEmailPasswordPair(email, password);
    }

    @Override
    void onCheckFail() {
        emailText.setError("Check email address");
        emailText.requestFocus();
        passwordText.setError("Check password");
        passwordText.requestFocus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String email = data.getStringExtra("email");
            String password = data.getStringExtra("password");
            emailText.setText(email);
            passwordText.setText(password);
            onVerificationSuccess();
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}

