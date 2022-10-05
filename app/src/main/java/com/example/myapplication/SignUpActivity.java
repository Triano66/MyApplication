package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    Button registerButton, returnSignUp;
    EditText registerEmail, registerPassword;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Creating instance of Firebase
        firebaseAuth = FirebaseAuth.getInstance();

        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        registerButton = findViewById(R.id.registerButton);
        returnSignUp = findViewById(R.id.returnSigUp);

        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String email = registerEmail.getText().toString().trim();
                String password = registerPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Enter the whole information", Toast.LENGTH_SHORT).show();
                    Log.d("Empty","There are empty values");
                }else{
                    //Toast.makeText(SignUpActivity.this, "There is a problem with the information", Toast.LENGTH_SHORT).show();
                    //Log.d("no vacio","no esta vacio"+ email+ " "+ password);
                    createUser(email,password);
                }

            }
        });

        returnSignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

    }

    /////////////////////////////////// EMAIL/PASSWORD sign up /////////////////////////////////////
    private void createUser(String emailUser, String passUser) {
        firebaseAuth.createUserWithEmailAndPassword(emailUser, passUser).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d("on Firebase","exito!!!");
                    //finish();
                    startActivity(new Intent(SignUpActivity.this, Home.class));
                    Toast.makeText(SignUpActivity.this, "Welcome "+emailUser+"!!!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, "error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    ////////////////////////////////////////////////////////////////////////////////////////////
}