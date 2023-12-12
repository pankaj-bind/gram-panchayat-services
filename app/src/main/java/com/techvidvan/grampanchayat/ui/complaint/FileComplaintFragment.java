package com.techvidvan.grampanchayat.ui.complaint;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.techvidvan.grampanchayat.HomeActivity;
import com.techvidvan.grampanchayat.data.model.Complaint;
import com.techvidvan.grampanchayat.data.model.News;
import com.techvidvan.grampanchayat.databinding.FragmentFileComplaintBinding;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FileComplaintFragment extends Fragment {

    private Uri imageUri;
    private ImageView complaintImage;
    private TextView subject;
    private TextView description;
    private AppCompatButton addImage;
    private AppCompatButton submitComplaint;



    private FragmentFileComplaintBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentFileComplaintBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        complaintImage = binding.complaintImage;
        subject = binding.subject;
        description = binding.description;
        addImage = binding.addImage;
        submitComplaint = binding.submitComplaint;

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setAspectRatio(3, 2)
                        .start(getContext(), FileComplaintFragment.this);
            }
        });

        submitComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String subjectText = subject.getText().toString();
                String descriptionText = description.getText().toString();

                if (subjectText.isEmpty()) {
                    subject.setError("Subject is required");
                    subject.requestFocus();
                    return;
                }else {
                    subject.setError(null);
                }
                if (descriptionText.isEmpty()) {
                    description.setError("Description is required");
                    description.requestFocus();
                    return;
                }else {
                    description.setError(null);
                }

                uploadComplaint();

            }
        });


        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            complaintImage.setImageURI(imageUri);
        } else {
            Toast.makeText(getContext(), "Error, Try Again.", Toast.LENGTH_SHORT).show();

        }

    }

    private void uploadComplaint() {
        ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Filing Complaint");
        pd.show();

        if (imageUri == null) {
            imageUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/gram-panchayat-services.appspot.com/o/News%2FPlaceholder_view.jpg?alt=media&token=5ca6caac-8be3-457d-8837-107b551a7875");
        } else if (imageUri != null ){
            StorageReference filePath = FirebaseStorage.getInstance().getReference("Complaints").child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));

            StorageTask uploadTask = filePath.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filePath.getDownloadUrl();
                }
            });

        }

        DatabaseReference refComplaint = FirebaseDatabase.getInstance().getReference("Complaints");

            String complaintId = refComplaint.push().getKey();

            //Getting current date
            SimpleDateFormat SDFormat
                    = new SimpleDateFormat();
            Calendar cal = Calendar.getInstance();
            String curr_date = SDFormat.format(cal.getTime());


            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String name = snapshot.child("name").getValue().toString();
                    String email = snapshot.child("email").getValue().toString();
                    String phone = snapshot.child("contact").getValue().toString();
                    String aadhar = snapshot.child("aadhar").getValue().toString();

                    Complaint complaint = new Complaint(complaintId, curr_date, subject.getText().toString(),
                            description.getText().toString(), "In Progress", email, name, aadhar, phone, "null", imageUri.toString());

                    refComplaint.child(complaintId).setValue(complaint);

                    pd.dismiss();
                    Toast.makeText(getContext(), "Complaint Filed Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext(), HomeActivity.class));
                    getActivity().finish();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {


                }
            });


    }

    private String getFileExtension(Uri uri) {
        String[] uriParts = uri.toString().split("\\.");
        return uriParts[uriParts.length - 1];
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}