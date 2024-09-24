package com.example.minglemind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hbb20.CountryCodePicker;

public class LoginPhone extends AppCompatActivity {
    CountryCodePicker country;
    EditText number;
    AppCompatButton sendOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone);
        init();
        country.registerCarrierNumberEditText(number);
        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!country.isValidFullNumber()){
                    number.setError("Please enter a valid number");
                    return;
                }
                Intent intent=new Intent(LoginPhone.this, LoginOTPactivity.class);
                intent.putExtra("phone",country.getFullNumberWithPlus());
                startActivity(intent);
            }
        });


    }
    void init(){
        country=findViewById(R.id.login_country);
        number=findViewById(R.id.login_mobile_number);
        sendOtp=findViewById(R.id.send_otp_btn);

    }
}