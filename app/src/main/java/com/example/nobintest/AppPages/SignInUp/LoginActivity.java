package com.example.nobintest.AppPages.SignInUp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nobintest.AppPages.AppActivity;
import com.example.nobintest.DataManegment.AppDataManager;
import com.example.nobintest.DataManegment.DataManager;
import com.example.nobintest.R;

import java.lang.ref.WeakReference;


public class LoginActivity extends AppCompatActivity {

    private ImageView logo, ivSignIn;
    private AutoCompleteTextView email, password;
    private TextView forgotPass, signUp;
    private Button btnSignIn, btnTwitter;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeGUI();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inEmail = email.getText().toString();
                String inPassword = password.getText().toString();

                if (validateInput(inEmail, inPassword)) {
                    InputMethodManager imm = (InputMethodManager) LoginActivity.this.getSystemService(LoginActivity.INPUT_METHOD_SERVICE);
                    //Find the currently focused view, so we can grab the correct window token from it.
                    View mView = LoginActivity.this.getCurrentFocus();
                    //If no view currently has focus, create a new one, just so we can grab a window token from it
                    if (mView == null) {
                        mView = new View(LoginActivity.this);
                    }
                    imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);

                    signUser(inEmail, inPassword);
                }

            }
        });

        btnTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(LoginActivity.this, AppActivity.class));
            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(LoginActivity.this, PwResetActivity.class));
            }
        });

    }


    public void signUser(String email, String password) {
        AppDataManager appDataManager = AppDataManager.getInstance(getApplication());
        appDataManager.setUserName(email);
        appDataManager.setPassWord(password);

        DataManager dataManager = DataManager.getInstance();
        if (!dataManager.isNetworkConnected(getApplication())) {
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
        } else {
            DataManager.SignInHandler signInHandler = DataManager.getSignInHandlerInstance();
            signInHandler.setLoginActivityWeakReference(new WeakReference<LoginActivity>(this));
            dataManager.getUserToken(email, password);
        }
    }


    private void initializeGUI() {

        logo = findViewById(R.id.ivLogLogo);
        ivSignIn = findViewById(R.id.ivSignIn);
        btnTwitter = findViewById(R.id.gust_btn);
        email = findViewById(R.id.atvEmailLog);
        password = findViewById(R.id.atvPasswordLog);
        forgotPass = findViewById(R.id.tvForgotPass);
        signUp = findViewById(R.id.tvSignIn);
        btnSignIn = findViewById(R.id.btnSignIn);
        progressDialog = new ProgressDialog(this);

    }


    public boolean validateInput(String inEmail, String inPassword) {

        if (inEmail.isEmpty()) {
            email.setError("Email field is empty.");
            return false;
        }
        if (inPassword.isEmpty()) {
            password.setError("Password is empty.");
            return false;
        }

        return true;
    }



    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this,R.string.back_again_to_exit, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}
