// This is the main activity of the app. It is the home screen of the app.

// Importing all the required packages
package com.techvidvan.grampanchayat;

// Importing all the required libraries
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.techvidvan.grampanchayat.data.model.Admin;
import com.techvidvan.grampanchayat.databinding.ActivityHomeBinding;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

// HomeActivity class which extends AppCompatActivity
public class HomeActivity extends AppCompatActivity {

    // Declaring all the variables
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;

    StorageReference storageReference;


    // onCreate method which is called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initializing the binding variable
        binding = ActivityHomeBinding.inflate(getLayoutInflater());

        // Setting the content view
        setContentView(binding.getRoot());

        // Setting the toolbar as the action bar
        setSupportActionBar(binding.appBarHome.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,R.id.nav_profile, R.id.nav_edit_profile, R.id.nav_create_news, R.id.nav_file_complaint, R.id.nav_view_your_complaint, R.id.nav_pending_complaints, R.id.nav_view_all_complaints_resolved, R.id.nav_view_all_complaints_rejected)
                .setOpenableLayout(drawer)
                .build();

        // Setting the navigation controller to the navigation view
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    }

    // onCreateOptionsMenu method which is called when the options menu is created
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        // Setting the on click listener for the logout button
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                // Creating an alert dialog box to confirm the logout
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure? You want to logout?");
                builder.setPositiveButton("YES", (dialog, which) -> {

                    // Signing out the user and redirecting to the login activity
                    // if the user clicks on the yes button
                    FirebaseAuth.getInstance().signOut();

                    // Redirecting to the login activity
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    finish();

                    // Dismissing the dialog box
                    dialog.dismiss();
                });

                // If the user clicks on the no button, the dialog box is dismissed
                builder.setNegativeButton("NO", (dialog, which) -> {
                    dialog.dismiss();
                });

                // Creating and showing the alert dialog box
                AlertDialog alert = builder.create();
                alert.show();

                return false;
            }
        } );

        return true;
    }

    // onSupportNavigateUp method which is called when the navigation up button is clicked
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // onStart method which is called when the activity is started
    // We are checking if the user is verified or not in this method
    @Override
    protected void onStart() {
        super.onStart();
        NavigationView navigationView = binding.navView;

        // Getting the current user from the firebase authentication
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // If the user is not verified, the user is redirected to the login activity
        if(!user.isEmailVerified()){
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        }

        //retrieve user profile image from firebase storage "profileImages" node using user id
        View headerView = navigationView.getHeaderView(0);
        ImageView id_profileImage = (ImageView) headerView.findViewById(R.id.profile_image);

        // Initializing the firebase authentication and storage reference
        FirebaseAuth  auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        // Getting the download url of the profile image
        StorageReference profileRef = storageReference.child("profileImages/" + auth.getCurrentUser().getUid());

        // Setting the profile image using the picasso library
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).into(id_profileImage);
        });

        // Retrieving the user name and email from the firebase realtime database
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference db = database.getReference().child("Users").child(user.getUid());
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Getting the user name and email from the firebase realtime database
                String name = snapshot.child("name").getValue().toString();
                String email = snapshot.child("email").getValue().toString();

                // Setting the user name and email in the navigation view header
                View headerView = navigationView.getHeaderView(0);
                TextView navEmail = (TextView) headerView.findViewById(R.id.mail);
                TextView navUsername = (TextView) headerView.findViewById(R.id.name);
                navUsername.setText(name);
                navEmail.setText(email);

            }

            // If the data is not retrieved, the onCancelled method is called
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Checking if the user is an admin or not and displaying the menu items accordingly
        Admin currentUser = new Admin();
        Menu nav_Menu = navigationView.getMenu();

        if(currentUser.isAdminUsingUID(user.getUid())){

            // If the user is an admin, the following menu items are displayed
            nav_Menu.findItem(R.id.nav_pending_complaints).setVisible(true);
            nav_Menu.findItem(R.id.nav_view_all_complaints_resolved).setVisible(true);
            nav_Menu.findItem(R.id.nav_view_all_complaints_rejected).setVisible(true);
            nav_Menu.findItem(R.id.nav_create_news).setVisible(true);

            // If the user is an admin, the following menu items are hidden
            nav_Menu.findItem(R.id.nav_file_complaint).setVisible(false);
            nav_Menu.findItem(R.id.nav_view_your_complaint).setVisible(false);
        } else {

            // If the user is not an admin, the following menu items are displayed
            nav_Menu.findItem(R.id.nav_pending_complaints).setVisible(false);
            nav_Menu.findItem(R.id.nav_view_all_complaints_resolved).setVisible(false);
            nav_Menu.findItem(R.id.nav_view_all_complaints_rejected).setVisible(false);
            nav_Menu.findItem(R.id.nav_create_news).setVisible(false);

            // If the user is not an admin, the following menu items are hidden
            nav_Menu.findItem(R.id.nav_file_complaint).setVisible(true);
            nav_Menu.findItem(R.id.nav_view_your_complaint).setVisible(true);
        }

    }
}