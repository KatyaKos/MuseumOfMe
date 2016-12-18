package me.spbau.katyakos.android.museumofme;

import android.os.Bundle;
import android.util.Pair;
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
        setResult(RESULT_OK);
        finish();
    }

    @Override
    boolean checkFields() {
        String email = getStringTextView(emailText);
        for (Pair<String, String> credential : DUMMY_CREDENTIALS) {
            String emailCredential = credential.first;
            if (emailCredential.equals(email)) {
                return false;
            }
        }
        return true;
    }

    @Override
    void onCheckFail() {
        Toast.makeText(getBaseContext(), "This email already has account", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onVerificationFail(int result) {
        super.onVerificationFail(result);
        if (result == 3) {
            nameText.setError("at least 3 characters");
            nameText.requestFocus();
        }
    }

    @Override
    protected int validate() {
        int isValid = super.validate();
        String name = getStringTextView(nameText);
        if (name.length() < 3) {
            isValid = 3;
        }
        return isValid;
    }
}
