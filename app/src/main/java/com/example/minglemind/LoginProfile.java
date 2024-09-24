package com.example.minglemind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.minglemind.Utils.FirebaseUtil;
import com.example.minglemind.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginProfile extends AppCompatActivity {

    String phone;
    UserModel userModel;
    EditText userName;
    AppCompatButton readyToGo;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_profile);
        phone=getIntent().getStringExtra("phone");
        init();
        getUsername();

        readyToGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUserName();
            }
        });


    }

    void setUserName(){
        String username=userName.getText().toString();
        if (username.isEmpty()||username.length()<3){
            userName.setError("Username should be at least 3 characters");
            return;
        }
        if (userModel!=null){
            userModel.setUserName(username);
        }else {
            userModel=new UserModel(username,phone, Timestamp.now(),FirebaseUtil.currentUserId());
        }

        FirebaseUtil.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setInProgress(false);
                if (task.isSuccessful()){
                    Intent intent=new Intent(LoginProfile.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    private void getUsername() {
        setInProgress(true);
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                setInProgress(false);
                if (task.isSuccessful()){
                    userModel=task.getResult().toObject(UserModel.class);
                    if (userModel!=null){
                        userName.setText(userModel.getUserName());
                    }
                }
            }
        });
    }

    void init(){
        userName=findViewById(R.id.login_userName);
        readyToGo=findViewById(R.id.readyToGo);
        progressBar=findViewById(R.id.loginUserProgress);
    }

    void setInProgress(boolean inProgress){
        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
            readyToGo.setVisibility(View.GONE);
        }
        else {
            progressBar.setVisibility(View.GONE);
            readyToGo.setVisibility(View.VISIBLE);
        }
    }
}