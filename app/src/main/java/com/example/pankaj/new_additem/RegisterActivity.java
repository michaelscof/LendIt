package com.example.pankaj.new_additem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class RegisterActivity extends AppCompatActivity {


    private EditText editTextUsername,editTextEmail,editTextPassword;
    private TextView textViewSignin;
    private Button buttonSignup;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressRegister;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference().child("students");

        //if getCurrentUser does not returns null
        if(firebaseAuth.getCurrentUser() != null){
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            startActivity(new Intent(getApplicationContext(), MainPageActivity.class));
        }

        progressRegister=new ProgressDialog(this);
        editTextUsername=findViewById(R.id.editTextUsername);
        editTextEmail=findViewById(R.id.editTextEmail);
        editTextPassword=findViewById(R.id.editTextPassword);
        buttonSignup=findViewById(R.id.buttonSignup);
        textViewSignin=findViewById(R.id.textViewSignin);

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerUser();
            }
        });

        textViewSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //System.out.println("hello guys");
                finish();
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });





    }

    private void registerUser() {


        //getting email and password from edit texts
        final String name=editTextUsername.getText().toString().trim();
        String email =editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(name)){

            //registerUser();
            Toast.makeText(RegisterActivity.this,"Please enter name",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(email)){

            Toast.makeText(RegisterActivity.this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){

            Toast.makeText(RegisterActivity.this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

            progressRegister.setMessage("Registering Please Wait...");
            progressRegister.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){

                                MyfirebaseInstance abc=new MyfirebaseInstance();
                                abc.onTokenRefresh();
                                String token=abc.gettoken();
                                        final String user_id=firebaseAuth.getCurrentUser().getUid().toString();

                                   // String token_id= FirebaseInstanceId.getInstance().getToken().toString();
                                    DatabaseReference tokenbaseReference=FirebaseDatabase.getInstance().getReference().child("tokens").child(user_id).child("token");
                                    tokenbaseReference.setValue(token).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            DatabaseReference child_reference=databaseReference.child(user_id);
                                            child_reference.child("name").setValue(name);
                                            child_reference.child("image").setValue("default");
                                            startActivity(new Intent(RegisterActivity.this, MainPageActivity.class));
                                            finish();

                                        }
                                    });

                        }else{
                            //display some message here
                            Toast.makeText(RegisterActivity.this,"Registration Error",Toast.LENGTH_LONG).show();
                        }
                        progressRegister.dismiss();

                    }
                });

    }
    @Override
    public void onBackPressed() {

        moveTaskToBack(true);
    }
}