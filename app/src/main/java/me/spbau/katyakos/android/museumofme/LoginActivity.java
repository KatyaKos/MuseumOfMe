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
    private static final String LOGGED_USER = "logged_user"; //"-1" if no user is in
    private static final String PREF_FILE = "loggedUser";
    private SharedPreferences sPref;
    //private SQLiteDatabase dataBase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        activityId = R.layout.activity_login;
        super.onCreate(savedInstanceState);

        /*UserDataBaseHelper dataBaseHelper = new UserDataBaseHelper(this);
        dataBase = dataBaseHelper.getWritableDatabase();
        AllUsersInformation.loadDataBase(dataBase);*/

        AllUsersInformation.downloadBasicInfo(getApplicationContext());

        sPref = getSharedPreferences(PREF_FILE, MODE_PRIVATE);
        String savedText = sPref.getString(LOGGED_USER, "-1");
        if (!savedText.equals("-1")) {
            loggedUserSuccess(savedText);
        }

        AllUsersInformation.downloadAuthInfo();

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
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.putExtra("userId", idString);
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
        String userId = AllUsersInformation.getIdByEmail(email);
        this.finish();
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
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
            String nickname = data.getStringExtra("nickname");
            AllUsersInformation.addUser(getApplicationContext(), nickname, email, password);
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

