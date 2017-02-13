package com.project3w.wharrynathan_tripjournal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {


    //Setup XML variables
    EditText emailEditText;
    EditText passwordEditText;
    EditText confirmPasswordEditText;
    Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Link variables to XML
        emailEditText = (EditText) findViewById(R.id.signupEmailEditText);
        passwordEditText = (EditText) findViewById(R.id.signupPasswordEditText);
        confirmPasswordEditText = (EditText) findViewById(R.id.signupConfirmPasswordEditText);
        signupButton = (Button) findViewById(R.id.signupCompleteButton);

        //Setup signup button listener
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if input email is a valid email and password and confirm password fields have 7 or more characters and match, return to login activity
                if (isValidEmail(emailEditText.getText().toString()) &&
                        passwordEditText.getText().length() > 6 &&
                        Objects.equals(passwordEditText.getText().toString(), confirmPasswordEditText.getText().toString())) {
                    //Setup logic for user creation (send results back to Main Activity).
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(LoginActivity.EXTRA_USER_EMAIL, emailEditText.getText().toString());
                    returnIntent.putExtra(LoginActivity.EXTRA_USER_PASSWORD, passwordEditText.getText().toString());
                    setResult(RESULT_OK, returnIntent);
                    finish();


                } else {
                    //Setup logic for user creation error.
                    if (!isValidEmail(emailEditText.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "You must enter a valid email in order to create an account.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (passwordEditText.getText().length() <= 6) {
                        Toast.makeText(getApplicationContext(), "Password must be at least 7 characters in length.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!Objects.equals(passwordEditText.getText().toString(), confirmPasswordEditText.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Password fields do not match.", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}
