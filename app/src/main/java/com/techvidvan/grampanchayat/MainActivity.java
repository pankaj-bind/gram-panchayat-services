// Importing the required packages
package com.techvidvan.grampanchayat;

// Importing the required libraries
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

// Creating the MainActivity class and extending it with the AppCompatActivity class
public class MainActivity extends AppCompatActivity {

    // Creating the required variables
    private Button register;
    private Button login;
    private Button admin;

    // Overriding the onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setting the content view to the activity_main layout
        setContentView(R.layout.activity_main);

        // Initializing the variables with the required views
        register = findViewById(R.id.registerButton);
        login = findViewById(R.id.loginButton);
        admin = findViewById(R.id.adminLoginButton);


        // Setting the onClickListener to the register button and moving to the RegisterActivity
        // If user clicks on the register button, the app will move to the RegisterActivity
        register.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));

        });

        // Setting the onClickListener to the login button and moving to the LoginActivity
        // If user clicks on the login button, the app will move to the LoginActivity
        login.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });

        // Setting the onClickListener to the admin button and moving to the AdminLoginActivity
        // If user clicks on the admin button, the app will move to the AdminLoginActivity
        admin.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AdminLoginActivity.class));
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        // Checking if the user is already logged in
        // If the user is already logged in, the app will move to the HomeActivity
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}