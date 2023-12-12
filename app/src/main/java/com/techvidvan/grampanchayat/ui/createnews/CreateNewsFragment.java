package com.techvidvan.grampanchayat.ui.createnews;


import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.techvidvan.grampanchayat.HomeActivity;
import com.techvidvan.grampanchayat.MainActivity;
import com.techvidvan.grampanchayat.data.model.News;
import com.techvidvan.grampanchayat.databinding.FragmentCreateNewsBinding;
import com.theartofdev.edmodo.cropper.CropImage;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;


public class CreateNewsFragment extends Fragment {

    private Uri imageUri;

    private String imageUrl;
    private TextView postNews;
    private EditText newsTitle, newsDescription, newsContent;
    private ImageView newsImage;

    private AppCompatButton addImage;

    private FragmentCreateNewsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentCreateNewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        postNews = binding.postNews;
        newsTitle = binding.newsTitle;
        newsDescription = binding.newsDescription;
        newsImage = binding.newsImage;
        addImage = binding.addImage;




        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity()
                        .start(getContext(), CreateNewsFragment.this);
            }
        });



        postNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = newsTitle.getText().toString();
                String description = newsDescription.getText().toString();

                if (title.isEmpty()) {
                    newsTitle.setError("Title is required");
                    newsTitle.requestFocus();
                    return;
                } else {
                    newsTitle.setError(null);
                }
                if (description.isEmpty()) {
                    newsDescription.setError("Description is required");
                    newsDescription.requestFocus();
                    return;
                } else {
                    newsDescription.setError(null);
                }

                uploadNews();

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
            newsImage.setImageURI(imageUri);
        } else {
            Toast.makeText(getContext(), "Error, Try Again.", Toast.LENGTH_SHORT).show();

        }


    }

    private void uploadNews() {
        ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Uploading");
        pd.show();

        if (imageUri != null) {
            StorageReference filePath = FirebaseStorage.getInstance().getReference("News").child(System.currentTimeMillis()
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
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        imageUrl =  downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("News");

                        String newsId = reference.push().getKey();

                        //Getting current date
                        SimpleDateFormat SDFormat
                                = new SimpleDateFormat();
                        Calendar cal = Calendar.getInstance();
                        String curr_date = SDFormat.format(cal.getTime());
                        System.out.println(curr_date);

                        News news = new News(newsId, newsTitle.getText().toString(), newsDescription.getText().toString(), imageUrl, curr_date);

                        reference.child(newsId).setValue(news);

                        pd.dismiss();
                        Toast.makeText(getContext(), "News Uploaded", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getContext(), HomeActivity.class));
                        getActivity().finish();
                    } else {
                        pd.dismiss();
                        Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            pd.dismiss();
            Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
        }
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