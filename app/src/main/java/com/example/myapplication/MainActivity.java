package com.example.myapplication;

//import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.content.Intent;
//import android.view.View;
//import android.widget.TextView;
import android.widget.Toast;
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    Button signUpMainButton, logInMainButton, signInWithOTP, signWithGooglebutton, signInWithTwitter;
    //EditText editTextEmail, EditTextPassword;
    FirebaseAuth firebaseAuth;

    //private FirebaseAuth mAuth;
    //private TextView textView;
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 100;
    private static final String TAG = "GOOGLE_SIGN_IN_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creating instance of Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        //editTextEmail = findViewById(R.id.editTextEmail);
        //EditTextPassword = findViewById(R.id.editTextPassword);
        signUpMainButton = findViewById(R.id.signUpButtonMain);
        logInMainButton = findViewById((R.id.logInButtonMain));
        signInWithOTP = findViewById((R.id.signInWithOTP));
        signWithGooglebutton = findViewById((R.id.signWithGoogleMainbutton));
        signInWithTwitter = findViewById((R.id.signInWithTwitter));

        //Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END config_signin]



        //

        signUpMainButton.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(i);
        });

        logInMainButton.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        });

        signInWithOTP.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, OTPActivity.class);
            startActivity(i);
        });

        signWithGooglebutton.setOnClickListener(view -> {
            Log.d(TAG, "onClick: being Google SignIn");
            Intent intent = googleSignInClient.getSignInIntent();
            startActivityForResult(intent, RC_SIGN_IN);
        });

        signInWithTwitter.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, TwitterActivity2.class);
            startActivity(i);
        });


    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null){
            Log.d(TAG, "checkUser: Already looged in");
            startActivity(new Intent(this, Home.class));
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "onActivityResult: Google Signin intent result");
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                firebaseAuthWithGoogleAccount(account);
            }
            catch (Exception e){
                Log.d(TAG, "onActivityResult: "+e.getMessage());
            }
        }
    }


    private void firebaseAuthWithGoogleAccount(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogleAccount: being firebase auth with google account");
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener(authResult -> {
                    Log.d(TAG, "onSuccess: Logged In");

                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                    assert firebaseUser != null;
                    String uid = firebaseUser.getUid();
                    String email = firebaseUser.getEmail();

                    Log.d(TAG, "onSuccess: Email"+email);
                    Log.d(TAG, "onSuccess: UID"+uid);

                    if (Objects.requireNonNull(authResult.getAdditionalUserInfo()).isNewUser()){
                        Log.d(TAG, "onSuccess: Account Created...\n"+email);
                        Toast.makeText(MainActivity.this, "Account Created...\n"+email, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Log.d(TAG, "onSuccess: Existing User...\n"+email);
                        Toast.makeText(MainActivity.this, "Existing User...\n"+email, Toast.LENGTH_SHORT).show();
                    }

                    startActivity(new Intent(MainActivity.this, Home.class));
                    finish();
                })
                .addOnFailureListener(e -> Log.d(TAG, "on Failure: Loggin failed "+e.getMessage()));
    }


}