package pl.jwojcik.gascompanion.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import pl.jwojcik.gascompanion.R;
import pl.jwojcik.gascompanion.models.CurrentUserService;
import pl.jwojcik.gascompanion.models.User;
import pl.jwojcik.gascompanion.services.ObjectResultListener;
import pl.jwojcik.gascompanion.services.firebase.UserService;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText textEmail;
    private EditText textPassword;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private UserService userService;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        userService = UserService.getInstance();

        if (userService.isLoggedIn()) {
            showProgressDialog("");
            userService.getUser(auth.getCurrentUser().getUid(), new ObjectResultListener() {
                @Override
                public void onResult(boolean isSuccess, String error, Object object) {
                    hideProgressDialog();
                    if (isSuccess) {
                        CurrentUserService.login((User) object);
                        startMainActivity();
                    } else {
                        Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        textEmail = findViewById(R.id.email);
        textPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        Button btnLogin = findViewById(R.id.btn_login);
        Button btnSignup = findViewById(R.id.btn_signup);
        Button btnForgot = findViewById(R.id.btn_forgot);

        btnLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
        btnForgot.setOnClickListener(this);

    }

    @Override
    public void onPause() {
        hideProgressDialog();
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                hideSoftKeyboard();
                login();
                break;
            case R.id.btn_forgot:
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
                break;
            case R.id.btn_signup:
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                break;
        }
    }

    private void login() {

        String email = textEmail.getText().toString().trim();
        String password = textPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), getString(R.string.empty_email), Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), getString(R.string.empty_password), Toast.LENGTH_SHORT).show();
            return;
        } else if (password.length() < 6) {
            textPassword.setError(getString(R.string.minimum_password));
            return;
        }

        showProgressDialog("");
        userService.login(email, password, new ObjectResultListener() {
            @Override
            public void onResult(boolean isSuccess, String error, Object object) {
                hideProgressDialog();
                if (!isSuccess) {
                    Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
                } else {
                    CurrentUserService.login((User) object);
                    startMainActivity();
                }
            }
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showProgressDialog(String title) {
        progressDialog = ProgressDialog.show(this, title, "");
    }

    private void hideProgressDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog = null;
    }

}
