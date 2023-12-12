package com.techvidvan.grampanchayat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout id_email, id_password;

    private TextView id_registerHere;
    private Button id_login;

    private ProgressBar progressBar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id_registerHere = findViewById(R.id.id_registerHere);
        id_login = findViewById(R.id.id_login);
        id_email = findViewById(R.id.id_emailLogin);
        id_password = findViewById(R.id.id_pwdLogin);
        progressBar = findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();



        id_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = id_email.getEditText().getText().toString();
                String password = id_password.getEditText().getText().toString();

                if(email.isEmpty()){
                    id_email.setError("Email is required");
                    id_email.requestFocus();
                    return;
                } else {
                    id_email.setError(null);
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    id_email.setError("Please provide valid email");
                    id_email.requestFocus();
                    return;
                } else {
                    id_email.setError(null);
                }
                if(password.isEmpty()){
                    id_password.setError("Password is required");
                    id_password.requestFocus();
                    return;
                } else {
                    id_password.setError(null);
                }
                if(password.length() < 8){
                    id_password.setError("Password must be 8 characters long");
                    id_password.requestFocus();
                    return;
                } else {
                    id_password.setError(null);
                }

                loginUser(email, password);
            }
        });


        id_registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser(String email, String password) {



        progressBar.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        progressBar.setVisibility(View.VISIBLE);

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if(user.isEmailVerified()){
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            // Show a Toast message
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            user.sendEmailVerification();
                            Toast.makeText(LoginActivity.this, "You must verify your email address", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        progressBar.setVisibility(View.GONE);
                        id_email.setError("Invalid email or password");
                        id_email.requestFocus();
                    }
                });
    }
}