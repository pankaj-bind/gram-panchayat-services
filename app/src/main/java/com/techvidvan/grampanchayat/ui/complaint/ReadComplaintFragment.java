package com.techvidvan.grampanchayat.ui.complaint;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.techvidvan.grampanchayat.R;
import com.techvidvan.grampanchayat.data.model.Admin;
import com.techvidvan.grampanchayat.ui.home.HomeFragment;

import java.util.Objects;

public class ReadComplaintFragment extends Fragment {

    String subject, description, image, date, status, name, mobile, email, aadhar, feedback, complaintId;

    Admin currentUser;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ReadComplaintFragment() {
        // Required empty public constructor
    }

    public ReadComplaintFragment(String complaintId, String subject, String description, String image, String date, String status, String name, String mobile, String email, String aadhar, String feedback){
        this.complaintId = complaintId;
        this.subject = subject;
        this.description = description;
        this.image = image;
        this.date = date;
        this.status = status;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.aadhar = aadhar;
        this.feedback = feedback;

    }

    public static ReadComplaintFragment newInstance(String param1, String param2) {
        ReadComplaintFragment fragment = new ReadComplaintFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_read_complaint, container, false);


        TextView subject = (TextView)view.findViewById(R.id.subject);
        TextView date = (TextView)view.findViewById(R.id.date);
        TextView description = (TextView)view.findViewById(R.id.description);
        TextView name = (TextView)view.findViewById(R.id.name);
        TextView email = (TextView)view.findViewById(R.id.email);
        TextView phone = (TextView)view.findViewById(R.id.phone);
        TextView aadhar = (TextView)view.findViewById(R.id.aadhar);
        TextView status = (TextView)view.findViewById(R.id.status);
        TextView feedback = (TextView)view.findViewById(R.id.feedback);
        ImageView image = (ImageView)view.findViewById(R.id.image);

        TextView selectStatus = (TextView)view.findViewById(R.id.selectStatus);
        RadioGroup radioGroup = (RadioGroup)view.findViewById(R.id.radioGroup);
        RadioButton inProgress = (RadioButton)view.findViewById(R.id.inProgress);
        RadioButton rejected = (RadioButton)view.findViewById(R.id.rejected);
        RadioButton resolved = (RadioButton)view.findViewById(R.id.resolved);
        EditText feedbackText = (EditText)view.findViewById(R.id.feedbackText);
        AppCompatButton giveFeedback = (AppCompatButton)view.findViewById(R.id.giveFeedback);

        subject.setText("Subject: " + this.subject);
        date.setText(this.date);
        description.setText(this.description);
        name.setText("Name: " + this.name);
        email.setText("Email: " + this.email);
        phone.setText("Phone: " + this.mobile);
        aadhar.setText("Aadhar: " + this.aadhar);
        status.setText("Status: " + this.status);

        if(this.feedback.equals("null")){
            this.feedback = "No feedback yet";
        }
        feedback.setText("Feedback: " + this.feedback);


        Picasso.get().load(this.image).into(image);

        currentUser = new Admin();
        firebaseAuth = FirebaseAuth.getInstance();

        if (currentUser.isAdminUsingMail(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail())){
            giveFeedback.setVisibility(View.VISIBLE);
            selectStatus.setVisibility(View.VISIBLE);

            inProgress.setVisibility(View.VISIBLE);
            resolved.setVisibility(View.VISIBLE);
            rejected.setVisibility(View.VISIBLE);
            feedbackText.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.VISIBLE);
        }


        giveFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Update the status and feedback of the complaint in the database
                //and then replace the current fragment with ReadComplaintFragment
                String updatedStatus = "";

                if(inProgress.isChecked()){
                    updatedStatus = "In Progress";
                }
                else if(resolved.isChecked()){
                    updatedStatus = "Resolved";
                }
                else if(rejected.isChecked()){
                    updatedStatus = "Rejected";
                }

                databaseReference = FirebaseDatabase.getInstance().getReference("Complaints").child(complaintId);
                databaseReference.child("status").setValue(updatedStatus);
                databaseReference.child("feedback").setValue(feedbackText.getText().toString());

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, new HomeFragment()).commit();
            }
        });

        return view;

    }

    public void onBackPress() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, new UsersComplaintFragment()).commit();
    }

    //if user selects another fragment without pressing back button, then this method will be called
    //and it will replace the current fragment with ReadNewsFragment
    @Override
    public void onPause() {
        super.onPause();

    }
}