package me.spbau.katyakos.android.museumofme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.util.Patterns.EMAIL_ADDRESS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    @InjectView(R.id.login_email)
    EditText emailText;
    @InjectView(R.id.login_password)
    EditText passwordText;
    @InjectView(R.id.login_link_signup)
    TextView signupLink;
    @InjectView(R.id.login_button)
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        buttonsListener();

    }

    private void buttonsListener() {
        passwordText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    loginAttempt();
                    return true;
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAttempt();
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the SignupActivity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }


    private void loginAttempt() {
        loginAttemptUI();
        loginAttemptLogic();
    }

    private void loginAttemptUI() {
        int validateResult = validate();
        if (validateResult > 0) {
            Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
            loginButton.setEnabled(true);

            if (validateResult == 1) {
                emailText.setError("enter a valid email address");
                emailText.requestFocus();
            } else {
                passwordText.setError("between 4 and 10 alphanumeric characters");
                passwordText.requestFocus();
            }

            return;
        }

        loginButton.setEnabled(false);
    }

    private void loginAttemptLogic() {
        String email = getStringTextView(emailText);
        String password = getStringTextView(passwordText);

        // TODO: Implement authentication logic here.

        if (checkEmailPassword(email, password)) {
            startMainActivity();
        } else {
            passwordText.setError(getString(R.string.error_incorrect_password));
            passwordText.requestFocus();
        }
    }

    private int validate() {
        int isValid = 0;

        String email = getStringTextView(emailText);
        String password = getStringTextView(passwordText);

        if (email.isEmpty() || !EMAIL_ADDRESS.matcher(email).matches()) {
            isValid = 1;
        }

        if (password.length() < 4 || password.length() > 10) {
            isValid = 2;
        }
        return isValid;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // TODO: Implement successful signup logic here
            startMainActivity();
        }
    }

    private boolean checkEmailPassword(String email, String password) {
        for (String credential : DUMMY_CREDENTIALS) {
            String[] pieces = credential.split(":");
            if (pieces[0].equals(email)) {
                // Account exists, return true if the password matches.
                return pieces[1].equals(password);
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    private void startMainActivity() {
        this.finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private String getStringTextView(TextView textView) {
        return textView.getText().toString();
    }

}

