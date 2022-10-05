package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {

    Button returnOTP, getOTPButton, verifyOTPButton;
    EditText phoneOTP, otpNumber;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBarOTP;
    String verificationID;

    //private static final String TAG = "PhoneAuthActivity";

    // [START declare_auth]
    //private FirebaseAuth mAuth;
    // [END declare_auth]

    //private String mVerificationId;
    //private PhoneAuthProvider.ForceResendingToken mResendToken;
    //private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        //Creating instance of Firebase
        firebaseAuth = FirebaseAuth.getInstance();

        phoneOTP = findViewById(R.id.phoneOTP);
        otpNumber = findViewById(R.id.otpNumber);
        getOTPButton = findViewById(R.id.getOTPButton);
        verifyOTPButton = findViewById(R.id.verifyOTPButton);
        progressBarOTP = findViewById(R.id.progressBarOTP);
        returnOTP = findViewById(R.id.returnOTP);
        progressBarOTP.setVisibility(View.INVISIBLE);


        returnOTP.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(OTPActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        getOTPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(phoneOTP.getText().toString())){
                    Toast.makeText(OTPActivity.this, "Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
                }else{
                    String number = phoneOTP.getText().toString();
                    progressBarOTP.setVisibility(View.VISIBLE);
                    sendverificationcode(number);
                }
            }
        });

        verifyOTPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(otpNumber.getText().toString())){
                    Toast.makeText(OTPActivity.this, "Wrong OTP entered", Toast.LENGTH_SHORT).show();
                }else{
                    verifycode(otpNumber.getText().toString());
                }

            }
        });

    }

    /////////////////////////////////// OTP SIGN IN ////////////////////////////////////////////////

    private void verifycode(String Code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID,Code);
        signinbyCredentials(credential);
    }

    private void signinbyCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(OTPActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(OTPActivity.this, Home.class));
                        }
                    }
                });
    }

    private void sendverificationcode(String phoneNumber) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            final String code = credential.getSmsCode();
            if(code!=null){
                verifycode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(OTPActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(s, token);
            verificationID = s;
            Toast.makeText(OTPActivity.this, "Code sent", Toast.LENGTH_SHORT).show();
            verifyOTPButton.setEnabled(true);
            progressBarOTP.setVisibility(View.INVISIBLE);
        }
    };


    ////////////////////////////////////////////////////////////////////////////////////////////////
}