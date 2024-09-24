package com.example.minglemind;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.minglemind.Utils.AndroidUtil;
import com.example.minglemind.Utils.FirebaseUtil;


import com.example.minglemind.model.UserModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class ProfileFragment extends Fragment {

    ImageView profilePic;
    EditText usernameInput,phoneInput;
    Button updateProfileBtn;
    TextView logoutBtn;
    ProgressBar progressBar;
    UserModel currentUserModel;
    ActivityResultLauncher<Intent> imagePickLaunchaer;
    Uri selectedImageuri;
    public ProfileFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePickLaunchaer=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
        result->{
            if (result.getResultCode()== ChatActivity.RESULT_OK){
                Intent data=result.getData();
                if (data!=null && data.getData()!=null){
                    selectedImageuri=data.getData();
                    AndroidUtil.setProfilePic(getContext(),selectedImageuri,profilePic);
                }
            }
        }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_profile, container, false);
        profilePic=root.findViewById(R.id.profile_pictur);
        usernameInput=root.findViewById(R.id.profile_userName);
        phoneInput=root.findViewById(R.id.profile_phone);
        updateProfileBtn=root.findViewById(R.id.profile_update_btn);
        logoutBtn=root.findViewById(R.id.logoutBtn);
        progressBar=root.findViewById(R.id.profileProgress);

        getUserDetails();
        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBtnClicked();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUtil.logout();
                Intent intent=new Intent(getContext(), SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(ProfileFragment.this).cropSquare().compress(512).maxResultSize(512,512)
                        .createIntent(new Function1<Intent, Unit>() {
                            @Override
                            public Unit invoke(Intent intent) {
                                imagePickLaunchaer.launch(intent);
                                return null;
                            }
                        });
            }
        });
        return root;
    }

    private void getUserDetails() {
        setInProgress(true);
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                setInProgress(false);
                if (task.isSuccessful()){
                    currentUserModel=task.getResult().toObject(UserModel.class);
                    if (currentUserModel!=null){
                        usernameInput.setText(currentUserModel.getUserName());
                        phoneInput.setText(currentUserModel.getPhone());
                        System.out.println(FirebaseUtil.currentUserId());
                    }
                }
            }
        });

        FirebaseUtil.getCurrentProfilePicStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    Uri uri=task.getResult();
                    AndroidUtil.setProfilePic(getContext(),uri,profilePic);
                }
            }
        });
    }

    void updateBtnClicked(){
        String newUsername=usernameInput.getText().toString();
        if (newUsername.isEmpty()||newUsername.length()<3){
            usernameInput.setError("Username should be at least 3 characters");
            return;
        }
        currentUserModel.setUserName(newUsername);
        setInProgress(true);
        if (selectedImageuri!=null){
            UploadTask uploadTask=FirebaseUtil.getCurrentProfilePicStorage().putFile(selectedImageuri);
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    setInProgress(false); // Hideprogress bar
                    Toast.makeText(getContext(), "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            AndroidUtil.setProfilePic(getContext(),uri,profilePic);
                            updateToFirestore();
                        }
                    });
                }
            });

        }else {
            updateToFirestore();
        }
    }

    void updateToFirestore(){
        FirebaseUtil.currentUserDetails().set(currentUserModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setInProgress(false);
                if (task.isSuccessful()){
                    Toast.makeText(getContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "Profile Updation failed", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    void setInProgress(boolean inProgress){
        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
            updateProfileBtn.setVisibility(View.GONE);
        }
        else {
            progressBar.setVisibility(View.GONE);
            updateProfileBtn.setVisibility(View.VISIBLE);
        }
    }
}