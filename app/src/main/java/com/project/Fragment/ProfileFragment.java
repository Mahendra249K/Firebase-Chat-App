package com.project.Fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.project.Model.User;
import com.project.practical.R;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    CircleImageView userImage;
    TextView userName;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    private StorageReference storageReference;
    private Uri imageUri;
    User userModel;
    ProgressBar progressBar;
    private StorageTask uploadTask;

    private static final int CHOOSE_IMAGE_REQUEST_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for getContext() fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        userImage =view. findViewById(R.id.userImage);
        progressBar =view. findViewById(R.id.progressBar);
        userName =view. findViewById(R.id.userName);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());
        storageReference = FirebaseStorage.getInstance().getReference("Uploads");


        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, CHOOSE_IMAGE_REQUEST_CODE);

            }
        });

//        updateProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (userName.getText().toString().trim().isEmpty()) {
//                    userName.setError("Required");
//                    return;
//                }
//                uploadCategoty();
//            }
//        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userModel = snapshot.getValue(User.class);

                userName.setText(userModel.getUserName());

                if (userModel.getImageUrl().equals("deafault")) {
                    userImage.setImageResource(R.drawable.user);
                } else {
                    Glide.with(getContext()).load(userModel.getImageUrl()).into(userImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            imageUri = data.getData();
            if (uploadTask!=null && uploadTask.isInProgress()){
                Toast.makeText(getContext(), "Wait", Toast.LENGTH_SHORT).show();
            }
            else {
                UpdateProfile();
            }

        }
    }

    private void UpdateProfile() {
        if (imageUri != null) {

            progressBar.setVisibility(View.VISIBLE);
            final StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + getFileExtention(imageUri));

            uploadTask=storageReference2.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return storageReference2.getDownloadUrl();
                }

            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri dowloadUri=task.getResult();
                        String mUri=dowloadUri.toString();

                        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());

                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("imageUrl",mUri);
                        reference.updateChildren(hashMap);

                        progressBar.setVisibility(View.GONE);
                    }
                    else {
                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
        else {
            Toast.makeText(getContext(), "Please Select Image", Toast.LENGTH_SHORT).show();
        }


    }

    private String getFileExtention(Uri imageUri) {

        // for uploading time set Extention of image
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));

    }
}