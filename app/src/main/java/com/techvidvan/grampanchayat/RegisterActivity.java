// Importing the required packages
package com.techvidvan.grampanchayat;

// Importing the required libraries
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.regex.Pattern;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;
import com.techvidvan.grampanchayat.data.model.User;


// Creating the RegisterActivity class and extending it with the AppCompatActivity class
public class RegisterActivity extends AppCompatActivity {

    // Creating the required variables
    private TextInputLayout tEmail, tPassword, tConfirmPassword, tName, tContact, tDateOfBirth, tAddress, tAadhar, tPincode;
    private TextInputEditText tDOB;
    private ProgressBar pb;
    private Button register;
    private TextView signIn;
    private RadioGroup tgender;
    private RadioButton tMale, tFemale, tOther;
    private FirebaseDatabase database;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setting the content view to the activity_register layout
        setContentView(R.layout.activity_register);

        // Initializing the FirebaseAuth instance and the FirebaseDatabase instance
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // Initializing the variables with the required views
        tEmail = (TextInputLayout) findViewById(R.id.id_emailRegister);
        tPassword = (TextInputLayout) findViewById(R.id.id_pwdRegister);
        tConfirmPassword = (TextInputLayout) findViewById(R.id.id_confirmpwdRegister);
        tName = (TextInputLayout) findViewById(R.id.id_userName);
        tContact = (TextInputLayout) findViewById(R.id.id_contactNo);
        tDateOfBirth = (TextInputLayout) findViewById(R.id.id_dob);
        tDOB = (TextInputEditText) findViewById(R.id.id_dob_edittext);
        tAddress = (TextInputLayout) findViewById(R.id.id_address);
        tAadhar = (TextInputLayout) findViewById(R.id.id_aadharno);
        tPincode = (TextInputLayout) findViewById(R.id.id_pincode);
        tgender = (RadioGroup) findViewById(R.id.radioGroup);
        tMale = (RadioButton) findViewById(R.id.id_male);
        tFemale = (RadioButton) findViewById(R.id.id_female);
        tOther = (RadioButton) findViewById(R.id.id_other);
        pb = (ProgressBar) findViewById(R.id.progressBar2);
        register = (Button) findViewById(R.id.id_register);
        signIn = (TextView) findViewById(R.id.id_signInRegister);


        // Setting the onClickListener for the signIn TextView to open the LoginActivity
        // when the user clicks on the signIn TextView
        signIn.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });

        // Setting the onClickListener for the tDOB TextInputEditText to open the DatePickerDialog
        // when the user clicks on the tDOB TextInputEditText (Date of Birth)
        tDOB.setOnClickListener(v -> {

            // Creating the Calendar instance and getting the current date
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Creating the DatePickerDialog instance and setting the date to the DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, (view, year1, monthOfYear, dayOfMonth) -> {
                // Setting the date to the tDOB TextInputEditText
                tDateOfBirth.getEditText().setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1);
            }, year, month, day);

            // Showing the DatePickerDialog
            datePickerDialog.show();

        });

        // Setting the onClickListener for the register Button to register the user
        // when the user clicks on the register Button
        register.setOnClickListener(v -> {

            // Initializing the required variables
            String email = tEmail.getEditText().getText().toString();
            String password = tPassword.getEditText().getText().toString();
            String confirmPassword = tConfirmPassword.getEditText().getText().toString();
            String name = tName.getEditText().getText().toString();
            String contact = tContact.getEditText().getText().toString();
            String address = tAddress.getEditText().getText().toString();
            String aadhar = tAadhar.getEditText().getText().toString();
            String pincode = tPincode.getEditText().getText().toString();
            int genderID = tgender.getCheckedRadioButtonId();
            String dateOfBirth = tDateOfBirth.getEditText().getText().toString();
            String gender = "";


            // Registration Validation
            if (name.isEmpty()) {
                tName.setError("Name is required");
                tName.requestFocus();
                return;
            } else {
                tName.setError(null);
            }
            if (contact.isEmpty()) {
                tContact.setError("Phone number is required");
                tContact.requestFocus();
                return;
            } else {
                tContact.setError(null);
            }
            if (contact.length() != 10) {
                tContact.setError("Enter a valid phone number");
                tContact.requestFocus();
                return;
            } else {
                tContact.setError(null);
            }
            if (dateOfBirth.isEmpty()) {
                tDateOfBirth.setError("Date of Birth is required");
                tDateOfBirth.requestFocus();
                return;
            } else {
                tDateOfBirth.setError(null);
            }
            if (aadhar.isEmpty()) {
                tAadhar.setError("Aadhar is required");
                tAadhar.requestFocus();
                return;
            } else {
                tAadhar.setError(null);
            }
            if (aadhar.length() != 12) {
                tAadhar.setError("Enter a valid Aadhar number");
                tAadhar.requestFocus();
                return;
            } else {
                tAadhar.setError(null);
            }
            if (genderID == -1) {
                tgender.requestFocus();
                return;
            }
            if (tMale.isChecked()) {
                gender = "Male";
            } else if (tFemale.isChecked()) {
                gender = "Female";
            } else if (tOther.isChecked()) {
                gender = "Other";
            }
            if (address.isEmpty()) {
                tAddress.setError("Address is required");
                tAddress.requestFocus();
                return;
            }
            if (pincode.isEmpty()) {
                tPincode.setError("Pincode is required");
                tPincode.requestFocus();
                return;
            }
            if (pincode.length() != 6) {
                tPincode.setError("Enter a valid Pincode");
                tPincode.requestFocus();
                return;
            }
            if (email.isEmpty()) {
                tEmail.setError("Email is required");
                tEmail.requestFocus();
                return;
            }
            Pattern pattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                tEmail.setError("Enter a valid email");
                tEmail.requestFocus();
                return;
            }
            if (password.isEmpty()) {
                tPassword.setError("Password is required");
                tPassword.requestFocus();
                return;
            }
            if (confirmPassword.isEmpty()) {
                tConfirmPassword.setError("Confirm Password is required");
                tConfirmPassword.requestFocus();
                return;
            }
            if (password.length() < 8) {
                tPassword.setError("Password should be at least 8 characters");
                tPassword.requestFocus();
                return;
            }
            if (!password.matches(".*[0-9].*")) {
                tPassword.setError("Password should contain at least one number");
                tPassword.requestFocus();
                return;
            }
            if (!password.matches(".*[a-zA-Z].*")) {
                tPassword.setError("Password should contain at least one alphabet");
                tPassword.requestFocus();
                return;
            }

            // Checking if the password and confirm password are same
            if (password.equals(confirmPassword)) {

                // Calling the registerUser method to register the user
                registerUser(email, password, name, aadhar, address, contact, pincode, dateOfBirth, gender);
            } else {

                // Showing the error message if the password and confirm password are not same
                Toast.makeText(RegisterActivity.this, "Password and Confirm Password should be same", Toast.LENGTH_SHORT).show();
            }


        });

    }

    // Method to register the user in the Firebase Authentication
    private void registerUser(String email, String password, String name, String aadhar, String address, String contact, String pincode, String dateOfBirth, String gender) {
        pb.setVisibility(View.VISIBLE);

        // Creating the user in the Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                // Creating the user in the Firebase Realtime Database if the user is created in the Firebase Authentication
                User user = new User(email, name, aadhar, address, contact, pincode, dateOfBirth, gender);
                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {

                        // Showing the success message and redirecting the user to the login page
                        pb.setVisibility(View.GONE);
                        Toast.makeText(RegisterActivity.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }

                });
            }

        }).addOnFailureListener(e -> {

            // Showing the error message if the user is not created in the Firebase Authentication
            pb.setVisibility(View.GONE);
            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}