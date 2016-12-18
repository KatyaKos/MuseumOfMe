package me.spbau.katyakos.android.museumofme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import static android.util.Patterns.EMAIL_ADDRESS;

abstract class AbstractLoginActivity extends AppCompatActivity {
    protected EditText emailText;
    protected EditText passwordText;
    protected TextView link;
    protected Button button;

    protected int activityId;
    protected String onVerificationFailMessage;

    protected static final ArrayList<Pair<String, String>> DUMMY_CREDENTIALS = new ArrayList<>(Arrays.asList(
            new Pair<>("foo@example.com", "hello"), new Pair<>("bar@example.com", "world")
    ));

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activityId);

        fieldsInitialization();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verification();
            }
        });

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkClick();
            }
        });
    }

    abstract void fieldsInitialization();

    abstract void linkClick();

    protected void verification() {
        if (verificationUIStart() == 0) {
            return;
        }
        verificationLogic();
        verificationUIFinish();
    }

    protected int verificationUIStart() {
        int validateResult = validate();
        if (validateResult > 0) {
            Toast.makeText(getBaseContext(), onVerificationFailMessage, Toast.LENGTH_LONG).show();
            button.setEnabled(true);
            onVerificationFail(validateResult);
            return 0;
        }

        button.setEnabled(false);
        return 1;
    }

    protected void verificationUIFinish() {
        button.setEnabled(true);
    }

    protected void verificationLogic() {
        if (checkFields()) {
            onVerificationSuccess();
        } else {
            onCheckFail();
        }
    }

    abstract void onVerificationSuccess();

    protected void onVerificationFail(int result) {
        if (result == 1) {
            emailText.setError("enter a valid email address");
            emailText.requestFocus();
        } else if (result == 2) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            passwordText.requestFocus();
        }
    }

    abstract boolean checkFields();

    abstract void onCheckFail();

    protected int validate() {
        int isValid = 0;
        String email = getStringTextView(emailText);
        String password = getStringTextView(passwordText);

        if (email.isEmpty() || !EMAIL_ADDRESS.matcher(email).matches()) {
            isValid = 1;
        } else if (password.length() < 4 || password.length() > 10) {
            isValid = 2;
        }
        return isValid;
    }

    protected String getStringTextView(TextView textView) {
        return textView.getText().toString();
    }
}
