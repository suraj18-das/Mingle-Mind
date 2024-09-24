package com.example.minglemind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class LoginOTPactivity extends AppCompatActivity {

    String phone;
    Long timeOutSec=60L;
    String verificationCode;
    PhoneAuthProvider.ForceResendingToken ResendingToken;
    EditText login_otp;
    AppCompatButton next_btn;
    TextView resend_otp;
    ProgressBar progressBar;
    FirebaseAuth auth=FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otpactivity);
        phone=getIntent().getStringExtra("phone");
        init();
        sendOTP(phone,false);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredOtp=login_otp.getText().toString();
                PhoneAuthCredential credential= PhoneAuthProvider.getCredential(verificationCode,enteredOtp);
                signIn(credential);
                setInProgress(true);
            }
        });

        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOTP(phone,true);
            }
        });

    }

    void sendOTP(String phone,boolean isResend){
        startResendTimer();
        setInProgress(true);
        PhoneAuthOptions.Builder builder=PhoneAuthOptions.newBuilder(auth).setPhoneNumber(phone).setTimeout(timeOutSec, TimeUnit.SECONDS).setActivity(this).setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signIn(phoneAuthCredential);
                setInProgress(false);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(LoginOTPactivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                setInProgress(false);
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode=s;
                ResendingToken=forceResendingToken;
                Toast.makeText(LoginOTPactivity.this, "OTP sent successfully", Toast.LENGTH_SHORT).show();
                setInProgress(false);
            }
        });

        if (isResend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(ResendingToken).build());
        }
        else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }

    private void startResendTimer() {
        resend_otp.setEnabled(false);
        Timer timer=new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeOutSec--;
                resend_otp.setText("Resend OTP in "+timeOutSec+" seconds");
                if (timeOutSec<0){
                    timeOutSec=60l;
                    timer.cancel();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resend_otp.setEnabled(true);
                        }
                    });
                }
            }
        },0,1000);
    }

    void setInProgress(boolean inProgress){
        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
            next_btn.setVisibility(View.GONE);
        }
        else {
            progressBar.setVisibility(View.GONE);
            next_btn.setVisibility(View.VISIBLE);
        }
    }
    void init(){
        login_otp=findViewById(R.id.login_otp);
        next_btn=findViewById(R.id.next_btn);
        resend_otp=findViewById(R.id.resend_otp);
        progressBar=findViewById(R.id.progressbar_loginOtp);
    }
    void signIn(PhoneAuthCredential phoneAuthCredential){
        setInProgress(true);
        auth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                setInProgress(false);
                if (task.isSuccessful()){
                    Intent intent=new Intent(LoginOTPactivity.this, LoginProfile.class);
                    intent.putExtra("phone",phone);
                    startActivity(intent);
                }else {
                    Toast.makeText(LoginOTPactivity.this, "OTP verification failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}