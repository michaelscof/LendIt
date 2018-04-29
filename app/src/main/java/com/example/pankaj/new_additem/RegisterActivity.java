package com.example.pankaj.new_additem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Random;

public class RegisterActivity extends AppCompatActivity {


    private EditText editTextUsername,editTextEmail,editTextPassword;
    private ImageView profileImage,idImage;
    private TextView textViewSignin;
    private Button buttonSignup;
    final static int GALLERY_REQUEST=1;
    private Uri imageuri;
    private static int MAX_LENGTH=10;
    private StorageReference storageprofile_pics;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressRegister;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference().child("students");
        storageprofile_pics= FirebaseStorage.getInstance().getReference();

        //if getCurrentUser does not returns null
        if(firebaseAuth.getCurrentUser() != null){
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            startActivity(new Intent(getApplicationContext(), MainPageActivity.class));
        }

        idImage=findViewById(R.id.addIdImage);
        //idImage=findViewById(R.id.addIdImage);
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

        idImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent GalleryIntend=new Intent(Intent.ACTION_GET_CONTENT);
                GalleryIntend.setType("image/*");
                startActivityForResult(GalleryIntend,GALLERY_REQUEST);
            }
        });






    }

    private void registerUser() {


        //getting email and password from edit texts
        final String name=editTextUsername.getText().toString().trim();
        String email =editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();

        //checking if email and passwords are empty

        if(imageuri==null){

            //registerUser();
            Toast.makeText(RegisterActivity.this,"Add Id image",Toast.LENGTH_LONG).show();
            return;
        }
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

                                            String random_name=random();

                                            StorageReference imagepath=storageprofile_pics.child("id_pics").child(random_name);
                                            imagepath.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                    Uri downloaduri=taskSnapshot.getDownloadUrl();

                                                    DatabaseReference child_reference=databaseReference.child(user_id);
                                                    child_reference.child("name").setValue(name);
                                                    child_reference.child("imageId").setValue(downloaduri.toString());
                                                    child_reference.child("image").setValue("default");
                                                    child_reference.child("status").setValue("hey there!! I'am using LendIt App.Its cool");
                                                    child_reference.child("address").setValue("Mnnit Allahabad");
                                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                    finish();
                                                }
                                            });
                                        }
                                    });

                        }else{
                            //display some message here
                            Toast.makeText(RegisterActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_LONG).show();
                        }
                        progressRegister.dismiss();

                    }
                });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_REQUEST && resultCode==RESULT_OK)
        {
            imageuri=data.getData();
            idImage.setImageURI(imageuri);
            //itemImage.setImageURI(imageuri);
        }
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    @Override
    public void onBackPressed() {

        moveTaskToBack(true);
    }
}