package com.example.pankaj.new_additem;

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
import android.widget.ImageView;
import android.widget.Toast;

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

public class Post_Item extends AppCompatActivity {

    private Button addButton;
    private ImageView itemImage;
    private EditText editTextType,editTextSize,editTextCost,editTextCaution;
    private Button submit;
    final static int GALLERY_REQUEST=1;
    private Uri imageuri=null;
    private static int MAX_LENGTH=10;
    private StorageReference storage;
    private DatabaseReference databaseReference;
    private ProgressDialog uploadprogress;
    private DatabaseReference for_userName;
    private FirebaseAuth auth;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post__item);

        addButton=findViewById(R.id.btnSelectPhoto);
        itemImage=findViewById(R.id.itemImage);
        editTextType=findViewById(R.id.editTextType);
        editTextSize=findViewById(R.id.editTextSize);
        editTextCost=findViewById(R.id.editTextCost);
        editTextCaution=findViewById(R.id.editTextCaution);

        auth=FirebaseAuth.getInstance();
        userId=auth.getCurrentUser().getUid().toString();
        for_userName=FirebaseDatabase.getInstance().getReference().child("students").child(userId);
        storage= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Item");
        uploadprogress=new ProgressDialog(this);

        submit=findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Function_For_Post();
            }
        });




        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent GalleryIntend=new Intent(Intent.ACTION_GET_CONTENT);
                GalleryIntend.setType("image/*");
                startActivityForResult(GalleryIntend,GALLERY_REQUEST);

            }
        });
    }

    private void Function_For_Post() {


        final String type=editTextType.getText().toString().trim();
        final String size=editTextSize.getText().toString().trim();
        final String cost=editTextCost.getText().toString().trim();
        final String caution=editTextCaution.getText().toString().trim();

        if( !TextUtils.isEmpty(type) && !TextUtils.isEmpty(size) && !TextUtils.isEmpty(cost) && !TextUtils.isEmpty(caution) && imageuri!=null){

            uploadprogress.setMessage("Post is uploading.....");
            uploadprogress.show();

            String random_name=random();

            StorageReference imagepath=storage.child("Item_images").child(random_name);
            imagepath.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    final Uri downloaduri=taskSnapshot.getDownloadUrl();

                    final DatabaseReference newItem=databaseReference.push();
                   // databaseReference.
                    for_userName.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            newItem.child("image").setValue(downloaduri.toString());
                            newItem.child("type").setValue(type);
                            newItem.child("size").setValue(size);
                            newItem.child("price").setValue(cost);
                            newItem.child("caution").setValue(caution);
                            newItem.child("user_id").setValue(userId);
                            newItem.child("user_name").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){

                                        startActivity(new Intent(Post_Item.this,MainPageActivity.class));
                                    }else
                                    {
                                        Toast.makeText(Post_Item.this,"getting some error",Toast.LENGTH_LONG).show();
                                    }

                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    uploadprogress.dismiss();
                }
            });
        }
        else
        {
            Toast.makeText(Post_Item.this,"Fill all the field",Toast.LENGTH_LONG).show();
        }



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_REQUEST && resultCode==RESULT_OK)
        {
            imageuri=data.getData();
            itemImage.setImageURI(imageuri);
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

}


