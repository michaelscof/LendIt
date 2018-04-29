package com.example.pankaj.new_additem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Random;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editTextProfilename,editTextAddress,editTextStatus;
    private ImageButton editprofilePic;
    private String Userid;
    private static int MAX_LENGTH=10;
    private Uri imageuri=null;
    private StorageReference storage;
    final static int GALLERY_REQUEST=1;
    private DatabaseReference databaseReferenceStudents;
    private Button saveChanges;
    private ProgressDialog uploadProgress;
    private String image;
    private int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        databaseReferenceStudents= FirebaseDatabase.getInstance().getReference().child("students");
        editprofilePic=findViewById(R.id.user_profile_photo);
        editTextProfilename=findViewById(R.id.user_profile_name);
        editTextAddress=findViewById(R.id.editTextAddress);
        editTextStatus=findViewById(R.id.editTextStatus);
        Userid= FirebaseAuth.getInstance().getUid();
        saveChanges=findViewById(R.id.saveChanges);
        storage = FirebaseStorage.getInstance().getReference();
        uploadProgress=new ProgressDialog(this);
        SetValue(Userid);
        editprofilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent GalleryIntend=new Intent(Intent.ACTION_GET_CONTENT);
                GalleryIntend.setType("image/*");
                startActivityForResult(GalleryIntend,GALLERY_REQUEST);

            }
        });
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadChanges(Userid);

            }
        });
    }

    private void uploadChanges(final String userid) {


        uploadProgress.setMessage("Updating the changes");
        uploadProgress.show();
        final String name = editTextProfilename.getText().toString();
        final String address = editTextAddress.getText().toString();
        final String status = editTextStatus.getText().toString();
        if (imageuri == null) {


            final DatabaseReference upload = databaseReferenceStudents.child(userid);
            upload.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    if (flag == 0) {
                        upload.child("name").setValue(name);
                        upload.child("address").setValue(address);
                        upload.child("status").setValue(status);
                        flag = 1;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            Toast.makeText(getApplicationContext(), "successfully upload", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            Toast.makeText(getApplicationContext(), "successfully upload", Toast.LENGTH_LONG).show();
            Intent intent=new Intent(getApplicationContext(),ProfileActivity.class);
            intent.putExtra("current_user_id",userid);
            startActivity(intent);
            uploadProgress.dismiss();
           // finish();


        }
        else{

        final String random_name = random();
        StorageReference imagpath = storage.child("profile_pics").child(random_name);
        imagpath.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                final Uri uri = taskSnapshot.getDownloadUrl();
                System.out.println(random_name);
                final DatabaseReference upload = databaseReferenceStudents.child(userid);
                upload.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        if (flag == 0) {
                            upload.child("name").setValue(name);
                            upload.child("image").setValue(uri.toString());
                            upload.child("address").setValue(address);
                            upload.child("status").setValue(status);
                            flag = 1;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Toast.makeText(getApplicationContext(), "successfully upload", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getApplicationContext(),ProfileActivity.class);
                intent.putExtra("current_user_id",userid);
                startActivity(intent);
                uploadProgress.dismiss();
                //finish();
            }
        });
        }
        /*

       /* StorageReference imagepath=storage.child("profile_pics").child(random_name);
        if(imagepath==null)
            System.out.println("hey man");
        else
            System.out.println("mt ro");*/
        /*/final DatabaseReference upload=databaseReferenceStudents.child(userid);
        upload.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                upload.child("name").setValue(name);
                upload.child("address").setValue(address);
                upload.child("status").setValue(status);
                uploadProgress.dismiss();
                startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        /*imagepath.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                final Uri downloaduri=taskSnapshot.getDownloadUrl();

                final DatabaseReference upload=databaseReferenceStudents.child(userid);
                upload.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        upload.child("name").setValue(name);
                        upload.child("address").setValue(address);
                        upload.child("status").setValue(status);
                        //upload.child("image").setValue(downloaduri.toString());

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        });*/


    }

    private void SetValue(String userid) {

        DatabaseReference getinfo=databaseReferenceStudents.child(userid);
        getinfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                editTextProfilename.setText(dataSnapshot.child("name").getValue().toString());
                editTextAddress.setText(dataSnapshot.child("address").getValue().toString());
                editTextStatus.setText(dataSnapshot.child("status").getValue().toString());
                image=dataSnapshot.child("image").getValue().toString();
                if(image.equals("default"))
                {
                        editprofilePic.setImageResource(R.drawable.user_profile);
                }
                else
                    Glide.with(EditProfileActivity.this).load(image).into(editprofilePic);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_REQUEST && resultCode==RESULT_OK)
        {
            imageuri=data.getData();
            editprofilePic.setImageURI(imageuri);
            if(imageuri==null)
            {
                System.out.println("hello i working fine");
            }
            else
                System.out.println("having some error");
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
        return randomStringBuilder.toString()+"uir";
    }
}
