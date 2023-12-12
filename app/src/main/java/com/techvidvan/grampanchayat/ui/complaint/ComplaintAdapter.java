package com.techvidvan.grampanchayat.ui.complaint;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.techvidvan.grampanchayat.R;
import com.techvidvan.grampanchayat.data.model.Complaint;
import com.techvidvan.grampanchayat.data.model.News;
import com.techvidvan.grampanchayat.ui.home.ReadNewsFragment;

import java.util.List;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ComplaintViewHolder> {

    private Context context;
    private List<Complaint> complaintList;



    FirebaseUser firebaseUser;

    public ComplaintAdapter(Context context, List<Complaint> complaintList) {
        this.context = context;
        this.complaintList = complaintList;
    }

    @NonNull
    @Override
    public ComplaintViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.complaint, parent, false);
        return new ComplaintAdapter.ComplaintViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Complaint complaint = complaintList.get(position);
        holder.subject.setText(complaint.getSubject());
        holder.date.setText(complaint.getDate());


        if(complaint.getSubject().length() > 40) {
            String subject = complaint.getSubject().substring(0, 40);
            holder.subject.setText(subject + "...");
        }else{
            holder.subject.setText(complaint.getSubject());
        }


        if(complaint.getFeedback().equals("null")){
            holder.message.setText("No Feedback");
        }else{
            //check length of feedback
            if(complaint.getFeedback().length() > 35) {
                String feedback = complaint.getFeedback().substring(0, 35);
                holder.message.setText(feedback + "...");
            }else{
                holder.message.setText(complaint.getFeedback());
            }

        }


        if(complaint.getStatus().equals("In Progress")){
            holder.status.setImageResource(R.drawable.pending);
            //change tint color
            holder.status.setColorFilter(Color.parseColor("#F9A825"));
        }
        else if(complaint.getStatus().equals("Resolved")){
            holder.status.setImageResource(R.drawable.resolved);
            //change tint color
            holder.status.setColorFilter(Color.parseColor("#4CAF50"));
        }
        else if(complaint.getStatus().equals("Rejected")){
            holder.status.setImageResource(R.drawable.rejected);
            //change tint color
            holder.status.setColorFilter(Color.parseColor("#F44336"));
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, new ReadComplaintFragment(complaint.getComplaintId(), complaint.getSubject(), complaint.getDescription(), complaint.getImage(), complaint.getDate(), complaint.getStatus(), complaint.getName(), complaint.getContact(), complaint.getEmail(), complaint.getAadhar(), complaint.getFeedback() )).addToBackStack(null).commit();

            }
        });



    }

    @Override
    public int getItemCount() {
        return complaintList.size();
    }

    public class ComplaintViewHolder extends RecyclerView.ViewHolder {
        TextView subject, date, message;
        ImageView status;
//        ImageView image;

        public ComplaintViewHolder(@NonNull View itemView) {
            super(itemView);
            subject = itemView.findViewById(R.id.subject);
            date = itemView.findViewById(R.id.date);
            message = itemView.findViewById(R.id.message);
            status = itemView.findViewById(R.id.status);
        }
    }
}