package com.techvidvan.grampanchayat.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.techvidvan.grampanchayat.R;

import com.techvidvan.grampanchayat.databinding.FragmentProfileBinding;


import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class ProfileFragment extends Fragment {

    private TextView id_userName;
    private TextView id_userEmail;
    private TextView id_contactNo;
    private TextView id_address;
    private TextView id_pinCode;
    private TextView id_aadharNo;
    private TextView id_birthDate;
    private TextView id_gender;
    private ImageView user_profile_photo;
    private FragmentProfileBinding binding;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        id_userName = root.findViewById(R.id.id_userName);
        id_userEmail = root.findViewById(R.id.id_userEmail);
        id_contactNo = root.findViewById(R.id.id_contactNo);
        id_address = root.findViewById(R.id.id_address);
        id_pinCode = root.findViewById(R.id.id_pinCode);
        id_aadharNo = root.findViewById(R.id.id_aadharNo);
        id_birthDate = root.findViewById(R.id.id_birthDate);
        id_gender = root.findViewById(R.id.id_gender);
        user_profile_photo = root.findViewById(R.id.user_profile_photo);



        StorageReference profileRef = FirebaseStorage.getInstance().getReference("profileImages/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).into(user_profile_photo);
        });

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users").child(auth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue().toString();
                String email = snapshot.child("email").getValue().toString();
                String contactNo = snapshot.child("contact").getValue().toString();
                String address = snapshot.child("address").getValue().toString();
                String pincode = snapshot.child("pincode").getValue().toString();
                String aadharNo = snapshot.child("aadhar").getValue().toString();
                String birthDate = snapshot.child("dateOfBirth").getValue().toString();
                String gender = snapshot.child("gender").getValue().toString();

                id_userName.setText(name);
                id_userEmail.setText(email);
                id_contactNo.setText(contactNo);
                id_address.setText(address);
                id_pinCode.setText(pincode);
                id_aadharNo.setText(aadharNo);
                id_gender.setText(gender);

                String[] date = birthDate.split("-");
                int day = Integer.parseInt(date[0]);
                int month = Integer.parseInt(date[1]);
                int year = Integer.parseInt(date[2]);

                String monthName = "";
                switch (month){
                    case 1:
                        monthName = "January";
                        break;
                    case 2:
                        monthName = "February";
                        break;
                    case 3:
                        monthName = "March";
                        break;
                    case 4:
                        monthName = "April";
                        break;
                    case 5:
                        monthName = "May";
                        break;
                    case 6:
                        monthName = "June";
                        break;
                    case 7:
                        monthName = "July";
                        break;
                    case 8:
                        monthName = "August";
                        break;
                    case 9:
                        monthName = "September";
                        break;
                    case 10:
                        monthName = "October";
                        break;
                    case 11:
                        monthName = "November";
                        break;
                    case 12:
                        monthName = "December";
                        break;
                }
                String dateOfBirth = day + " " + monthName + ", " + year;

                long millis = System.currentTimeMillis();
                java.sql.Date currentDate = new java.sql.Date(millis);
                String currentDateStr = currentDate.toString();
                String[] newDate = currentDateStr.split("-");
                int currentYear = Integer.parseInt(newDate[0]);
                int currentMonth = Integer.parseInt(newDate[1]);
                int currentDay = Integer.parseInt(newDate[2]);
                //Calculate age
                System.out.println(currentYear + " " + currentMonth + " " + currentDay);
                System.out.println(year + " " + month + " " + day);
                int age = currentYear - year;
                if(currentMonth < month){
                    age--;
                }else if(currentMonth == month){
                    if(currentDay < day){
                        age--;
                    }
                }
                id_birthDate.setText(String.valueOf(dateOfBirth + " (" + age + " years old)"));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return root;
    }





}