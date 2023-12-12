package com.techvidvan.grampanchayat.ui.home;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.techvidvan.grampanchayat.HomeActivity;
import com.techvidvan.grampanchayat.LoginActivity;
import com.techvidvan.grampanchayat.R;
import com.techvidvan.grampanchayat.data.model.Admin;

public class ReadNewsFragment extends Fragment {

    String newsId, title, description, image, date;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ReadNewsFragment() {
        // Required empty public constructor
    }

    public ReadNewsFragment(String newsId, String title, String description, String image, String date) {
        this.newsId = newsId;
        this.title = title;
        this.description = description;
        this.image = image;
        this.date = date;
    }

    public static ReadNewsFragment newInstance(String param1, String param2) {
        ReadNewsFragment fragment = new ReadNewsFragment();
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


        View view = inflater.inflate(R.layout.fragment_read_news, container, false);

        TextView news_title = view.findViewById(R.id.subject);
        TextView news_description = view.findViewById(R.id.description);
        TextView news_date = view.findViewById(R.id.date);
        ImageView news_image = view.findViewById(R.id.image);
        AppCompatButton delete_news = view.findViewById(R.id.delete_news);

        news_title.setText(title);
        news_description.setText(description);
        news_date.setText(date);

        Picasso.get().load(image).into(news_image);


        Admin currentUser = new Admin();
        if(currentUser.isAdminUsingMail(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
            delete_news.setVisibility(View.VISIBLE);
        }

        delete_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create a prompt to confirm deletion
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure you want to delete this news?");
                builder.setPositiveButton("YES", (dialog, which) -> {

                    // If the user clicks on the yes button, the news is deleted
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("News").child(newsId);
                    databaseReference.removeValue();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, new HomeFragment()).commit();


                    // Redirecting to the login activity
                    startActivity(new Intent(getContext(), HomeActivity.class));


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

            }
        });

        return view;

    }

    public void onBackPress() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, new ReadNewsFragment()).commit();
    }

    //if user selects another fragment without pressing back button, then this method will be called
    //and it will replace the current fragment with ReadNewsFragment
    @Override
    public void onPause() {
        super.onPause();

    }
}