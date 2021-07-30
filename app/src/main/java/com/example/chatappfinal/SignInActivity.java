package com.example.chatappfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.chatappfinal.databinding.ActivitySignInBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;


public class SignInActivity extends AppCompatActivity {
    ActivitySignInBinding mbinding;
    ProgressDialog mprogressDialog;
    private FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseDatabase mfirebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        mbinding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(mbinding.getRoot());
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mprogressDialog = new ProgressDialog(SignInActivity.this);
        mprogressDialog.setTitle("LogIn Account");
        mprogressDialog.setMessage("We're logging your Account");
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mbinding.Google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signInGoogle();
            }

        });
        //PHONE NUMBER
        mbinding.SignupPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),otpGeneration.class);
                startActivity(intent);
            }
        });
        if(mAuth.getCurrentUser()!=null)
        {
            Intent intent=new Intent(SignInActivity.this,MainActivity.class);
            startActivity(intent);
        }
        mbinding.Google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
 try {
     signInGoogle();
 }
 catch (Exception e)
 {
     Toast.makeText(getApplicationContext(), "Sorry!!Couldn't find ur accont SIGN UP", Toast.LENGTH_SHORT).show();

 }

            }
        });
        mbinding.haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
        mbinding.SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mprogressDialog.show();
                if( mbinding.Password.getText().toString()!=null) {
                    mAuth.signInWithEmailAndPassword(mbinding.Email.getText().toString(), mbinding.Password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mprogressDialog.dismiss();
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Account Opened Succesfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    mAuth.signInWithEmailAndPassword(mbinding.Email.getText().toString(), mbinding.Password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mprogressDialog.dismiss();
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Account Opened Succesfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

    }
    int RC_SIGN_IN=65;
    private void signInGoogle() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        mprogressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            mprogressDialog.hide();
                            FirebaseUser user1 = mAuth.getCurrentUser();
                            User users = new User();
                            users.setId(user1.getUid());
                            users.setEmail(user1.getEmail());
                            users.setUsername(user1.getDisplayName());
                            mfirebaseDatabase.getReference().child("Users").child(user1.getUid()).setValue(users);
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            //   updateUI(null);

                        }
                    }
                });

    }



}