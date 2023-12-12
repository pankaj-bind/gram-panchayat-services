
// Importing the required packages
package com.techvidvan.grampanchayat;

// Importing the required libraries
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.material.textfield.TextInputLayout;

// Creating the AdminLoginActivity class and extending it with the AppCompatActivity class
public class AdminLoginActivity extends AppCompatActivity {

    // Creating the required variables
    private TextInputLayout adminID, adminPassword;
    private Button id_login;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    // Overriding the onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setting the content view to the activity_admin_login layout
        setContentView(R.layout.activity_admin_login);

        // Initializing the variables with the required views
        id_login = findViewById(R.id.id_login);
        adminID = findViewById(R.id.adminID);
        adminPassword = findViewById(R.id.adminPassword);
        progressBar = findViewById(R.id.progressBar);

        // Initializing the FirebaseAuth instance
        auth = FirebaseAuth.getInstance();

        // Setting the onClickListener to the id_login button and performing the required operations
        id_login.setOnClickListener(v -> {
            String ID = adminID.getEditText().getText().toString();
            String Password = adminPassword.getEditText().getText().toString();

            // Validating the input fields
            if (ID.isEmpty()) {
                adminID.setError("Email is required");
                adminID.requestFocus();
                return;
            } else {
                adminID.setError(null);
            }
            if (Password.isEmpty()) {
                adminPassword.setError("Password is required");
                adminPassword.requestFocus();
                return;
            } else {
                adminPassword.setError(null);
            }

            // Setting the visibility of the progress bar to visible
            progressBar.setVisibility(View.VISIBLE);

            // Checking if the entered credentials are correct or not for the admin
            if (ID.equals("techvidvan@gmail.com") && Password.equals("hello@1234")) {

                // Signing in with the entered credentials
                auth.signInWithEmailAndPassword("techvidvan@gmail.com", "hello@1234")
                        .addOnCompleteListener(task -> {

                            // Checking if the task is successful or not
                            if(task.isSuccessful()){

                                // Setting the visibility of the progress bar to gone and starting the HomeActivity
                                // Toast message is displayed if the entered credentials are correct
                                progressBar.setVisibility(View.GONE);
                                Intent intent = new Intent(AdminLoginActivity.this, HomeActivity.class);

                                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                            }
                        });
            } else {

                // Setting the visibility of the progress bar to gone and displaying the error message
                // if the entered credentials are incorrect
                Toast.makeText(AdminLoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }

        });

    }
}