package me.spbau.katyakos.android.museumofme;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SignupActivity extends AbstractLoginActivity {
    @InjectView(R.id.signup_input_name)
    EditText nameText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        activityId = R.layout.activity_signup;
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
    }

    @Override
    void fieldsInitialization() {
        emailText = (EditText) findViewById(R.id.signup_input_email);
        passwordText = (EditText) findViewById(R.id.signup_input_password);
        link = (TextView) findViewById(R.id.signup_link_login);
        button = (Button) findViewById(R.id.signup_button);
        onVerificationFailMessage = "Signing up failed";
    }

    @Override
    void linkClick() {
        setResult(0);
        finish();
    }

    @Override
    void onVerificationSuccess() {
        String email = getStringTextView(emailText);
        String password = getStringTextView(passwordText);
        String nickname = "@" + getStringTextView(nameText);
        AllUsersInformation.addUser(nickname, email, password);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    boolean checkFields() {
        String email = getStringTextView(emailText);
        return AllUsersInformation.containsByEmail(email);
    }

    @Override
    void onCheckFail() {
        Toast.makeText(getBaseContext(), "This email already has account", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onVerificationFail(int result) {
        super.onVerificationFail(result);
        if (result == 3) {
            nameText.setError("from 3 to 15 characters");
            nameText.requestFocus();
        } else if (result == 4) {
            nameText.setError("shouldn't contain spaces or '@' characters");
            nameText.requestFocus();
        }
    }

    @Override
    protected int validate() {
        int isValid = super.validate();
        String nickname = getStringTextView(nameText);
        if (nickname.length() < 3 || nickname.length() > 15) {
            isValid = 3;
        } else if (nickname.contains("@") || nickname.contains(" ")) {
            isValid = 4;
        }
        return isValid;
    }
}
