package com.example.pankaj.new_additem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail,editTextPassword;
    private TextView textViewSignUp;
    private Button buttonSignIn;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private SignInButton googlebtn;
    private final static int RC_SIGN_IN=1;
    private GoogleApiClient mGoogleApiClient;
    private final String TAG="LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail=findViewById(R.id.editTextEmail);
        editTextPassword=findViewById(R.id.editTextPassword);
        textViewSignUp=findViewById(R.id.textViewSignUp);
        buttonSignIn=findViewById(R.id.buttonSignin);
        googlebtn=findViewById(R.id.GooglesignInButton);
        progressDialog=new ProgressDialog(this);

        //getting firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("students");
        databaseReference.keepSynced(true);

        //if the objects getcurrentuser method is not null
        //means user is already logged in
        if(firebaseAuth.getCurrentUser() != null){
            //close this activity
            finish();
            //opening profile activity
            System.out.println("hello guysll");
            startActivity(new Intent(LoginActivity.this, MainPageActivity.class));
        }

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userLogin();
            }
        });
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // finish();
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        googlebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
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
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        progressDialog.setMessage("Starting Sign in...");
        progressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            MyfirebaseInstance abc=new MyfirebaseInstance();
                            abc.onTokenRefresh();
                            String token=abc.gettoken();

                                    //String token_id= FirebaseInstanceId.getInstance().getToken().toString();
                                    String user_id=firebaseAuth.getCurrentUser().getUid().toString();
                                    DatabaseReference tokenbaseReference=FirebaseDatabase.getInstance().getReference().child("tokens").child(user_id).child("token");
                                    tokenbaseReference.setValue(token).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Log.d(TAG, "signInWithCredential:success");
                                            startActivity(new Intent(LoginActivity.this,MainPageActivity.class));
                                            FirebaseUser user = firebaseAuth.getCurrentUser();
                                        }
                                        // startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                                    });
                           // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_LONG).show();
                           // updateUI(null);
                        }
                        progressDialog.dismiss();

                        // ...
                    }
                });
    }

    private void userLogin() {

        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();


        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Logining Please Wait...");
        progressDialog.show();

        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        //if the task is successfull
                        if(task.isSuccessful()){
                            //start the profile activity

                            MyfirebaseInstance abc=new MyfirebaseInstance();
                            abc.onTokenRefresh();
                            String token=abc.gettoken();

                                   // String token_id= FirebaseInstanceId.getInstance().getToken().toString();
                                    String user_id=firebaseAuth.getCurrentUser().getUid().toString();
                                    DatabaseReference tokenbaseReference=FirebaseDatabase.getInstance().getReference().child("tokens").child(user_id).child("token");
                                    tokenbaseReference.setValue(token).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Toast.makeText(LoginActivity.this, "successfully login", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(LoginActivity.this, MainPageActivity.class));
                                            // checkUserExist();
                                        }
                            // startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                                });
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this,"First Register yourself then try",Toast.LENGTH_LONG).show();
                           // startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                            finish();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    public void checkUserExist()
    {
        final String userId=firebaseAuth.getCurrentUser().getUid().toString();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(userId))
                {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }else{

                    Toast.makeText(LoginActivity.this,"First Register yourself then try",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void onBackPressed() {

        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
        finish();
    }
}
