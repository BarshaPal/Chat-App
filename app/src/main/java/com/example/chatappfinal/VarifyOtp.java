package com.example.chatappfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.chatappfinal.databinding.ActivityOtpGenerationBinding;
import com.example.chatappfinal.databinding.ActivityVarifyOtpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VarifyOtp extends AppCompatActivity {
    ActivityVarifyOtpBinding binding;
    FirebaseAuth mAuth;
    String otp;
    ProgressDialog mprogressDialog;
    FirebaseDatabase mfirebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        binding = ActivityVarifyOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String phone = getIntent().getStringExtra("mobile").toString();
        Toast.makeText(getApplicationContext(), "Wait For the messege to arrive for verification", Toast.LENGTH_SHORT).show();
        mprogressDialog = new ProgressDialog(VarifyOtp.this);
        mprogressDialog.setTitle("Logging In");
        mprogressDialog.setMessage("Wait for Verification");

        sendVerificationCode(phone);

        binding.resend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                sendVerificationCode(phone);

            }
        });
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mprogressDialog.show();
                String code=binding.editTextPhone.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Enter code", Toast.LENGTH_SHORT).show();
                }
                verifyCode(code);
            }
        });
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otp, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)

                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                          mprogressDialog.hide();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

//get reference
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

//build child
                            ref.child(user.getUid()).setValue(user);
                            Intent intent = new Intent(VarifyOtp.this, MainActivity.class);
                            startActivity(intent);

                        } else {
                            mprogressDialog.hide();
                            Toast.makeText(VarifyOtp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)
                .setTimeout(60L,TimeUnit.MILLISECONDS)
                .setActivity(VarifyOtp.this)
                .setCallbacks( new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            @Override
                            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                otp = s;
                            }

                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                                String code = phoneAuthCredential.getSmsCode();
                                if (code != null) {
                                    binding.editTextPhone.setText(code);
                                    Toast.makeText(VarifyOtp.this, code, Toast.LENGTH_LONG).show();
                                    signInWithCredential(phoneAuthCredential);
                                }
                            }

                            @Override
                            public void onVerificationFailed(FirebaseException e) {
                                Toast.makeText(VarifyOtp.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }) .build();

        PhoneAuthProvider.verifyPhoneNumber(options);

    }


    }

