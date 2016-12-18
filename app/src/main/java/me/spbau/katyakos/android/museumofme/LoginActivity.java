package me.spbau.katyakos.android.museumofme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AbstractLoginActivity {

    public LoginActivity() {
        emailText = (EditText) findViewById(R.id.login_email);
        passwordText = (EditText) findViewById(R.id.login_password);
        link = (TextView) findViewById(R.id.login_link_signup);
        button = (Button) findViewById(R.id.login_button);
        activityId = R.layout.activity_login;
        onVerificationFailMessage = "Login failed";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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

    @Override
    void linkClick() {
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    void onVerificationSuccess() {
        this.finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    boolean checkFields() {
        String email = getStringTextView(emailText);
        String password = getStringTextView(passwordText);
        for (Pair<String, String> credential : DUMMY_CREDENTIALS) {
            String emailCredential = credential.first;
            String passwordCredential = credential.second;
            if (emailCredential.equals(email)) {
                return passwordCredential.equals(password);
            }
        }
        return false;
    }

    @Override
    void onCheckFail() {
        passwordText.setError(getString(R.string.error_incorrect_password));
        passwordText.requestFocus();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}

