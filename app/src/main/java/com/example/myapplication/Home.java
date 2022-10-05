package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity {

    FirebaseAuth mAuth2;
    Button logOutButton, verifyButton;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth2 = FirebaseAuth.getInstance();

        //Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END config_signin]

        //////verification mail
        /*verifyButton = findViewById(R.id.verifyButton);

        FirebaseUser user = mAuth2.getCurrentUser();

        if (user.isEmailVerified() ){
            Log.d("Verified?? false"," "+user.isEmailVerified());
            verifyButton.setVisibility(View.INVISIBLE);

        }else{
            Log.d("Verified?? true"," "+user.isEmailVerified());
            verifyButton.setVisibility(View.VISIBLE);
        }*/



        /////////////////////////////////// LOG OUT ////////////////////////////////////////////


        logOutButton = findViewById(R.id.logOutButton);

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth2.signOut();
                /*startActivity(new Intent(Home.this, MainActivity.class));
                finish();*/

                //Closing session with Google
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Intent loginActivity = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(new Intent(Home.this, MainActivity.class));
                            finish();

                        }else{
                            Toast.makeText(getApplicationContext(), "Google session is not closed", Toast.LENGTH_LONG).show();
                        }
                    }
                });




            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////

    }
}