package com.example.chatappfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.chatappfinal.databinding.ActivityOtpGenerationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class otpGeneration extends AppCompatActivity {
ActivityOtpGenerationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOtpGenerationBinding.inflate(getLayoutInflater());
   binding.cpp.registerCarrierNumberEditText(binding.editTextTextPersonName);
        setContentView(binding.getRoot());
        binding.generateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(otpGeneration.this,VarifyOtp.class);
                intent.putExtra("mobile",binding.cpp.getFullNumberWithPlus().replace(" ",""));
                startActivity(intent);
            }
        });
    }
}