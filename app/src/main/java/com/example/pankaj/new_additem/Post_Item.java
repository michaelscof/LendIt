package com.example.pankaj.new_additem;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.ArrayList;
import java.util.Random;

public class Post_Item extends AppCompatActivity {

    private ImageButton addButton;
    private ImageView itemImage;
    private AutoCompleteTextView editTextType;
    private EditText editDesc,editTextCost,editTextCaution;
    private Button submit;
    final static int GALLERY_REQUEST=1;
    private Uri imageuri=null;
    private static int MAX_LENGTH=10;
    private StorageReference storage;
    private DatabaseReference databaseReference;
    private ProgressDialog uploadprogress;
    private DatabaseReference for_userName;
    private FirebaseAuth auth;
    private String subType;
    private String userId;
    private ArrayList<String> items_List;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post__item);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setValue();
        addButton=findViewById(R.id.btnSelectPhoto);
        editTextType = findViewById(R.id.editTextType);
        editDesc=findViewById(R.id.editDesc);
        editTextCost=findViewById(R.id.editTextCost);
        editTextCaution=findViewById(R.id.editTextCaution);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_dropdown_item_1line,items_List);

        editTextType.setThreshold(1);//will start working from first character
        editTextType.setAdapter(adapter);
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

        editTextType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextType.showDropDown();
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

    private void setValue() {

        items_List = new ArrayList<>();
        items_List.add("Men_Clothing shirts");
        items_List.add("Men_Clothing tshirts");
        items_List.add("Men_Clothing partyWears");
        items_List.add("Men_Clothing jeans");
        items_List.add("Men_Clothing trouses");
        items_List.add("Men_Clothing formalPants");
        items_List.add("Men_Clothing formalshirts");
        items_List.add("Men_Clothing jackets");
        items_List.add("Men_Accessories watches");
        items_List.add("Men_Accessories caps");
        items_List.add("Men_Accessories glasses");
        items_List.add("Men_Accessories belts");
        items_List.add("Men_Accessories bags");
        items_List.add("Men_Footwear formals");
        items_List.add("Men_Footwear sneakers");
        items_List.add("Men_Footwear sports");
        items_List.add("Men_Footwear boots");


        items_List.add("Women_Clothing shirts");
        items_List.add("Women_Clothing tshirts");
        items_List.add("Women_Clothing partyWears");
        items_List.add("Women_Clothing jeans");
        items_List.add("Women_Clothing trouses");
        items_List.add("Women_Clothing formalPants");
        items_List.add("Women_Clothing formalshirts");
        items_List.add("Women_Clothing jackets");
        items_List.add("Women_Accessories watches");
        items_List.add("Women_Accessories caps");
        items_List.add("Women_Accessories glasses");
        items_List.add("Women_Accessories belts");
        items_List.add("Women_Accessories bags");
        items_List.add("Women_Footwear formals");
        items_List.add("Women_Footwear sneakers");
        items_List.add("Women_Footwear sports");
        items_List.add("Women_Footwear sandles");

        items_List.add("Stationary books");
        items_List.add("Stationary calculators");
        items_List.add("Stationary musicalInstruments");
        items_List.add("Stationary papersheets");

        items_List.add("Sports basketball");
        items_List.add("Sports volleyball");
        items_List.add("Sports badminton");
        items_List.add("Sports cricketBall");
        items_List.add("Sports cricketBat");
        items_List.add("Sports football");
        items_List.add("Sports cycle");

        items_List.add("DailyUsage Boiler");

        items_List.add("Techs mouse");
        items_List.add("Techs keyboad");
        items_List.add("Techs harddisk");
        items_List.add("Techs pendrive");
        items_List.add("Techs rounter");

        items_List.add("Others no tag");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                Intent intent=new Intent(getApplicationContext(),MainPageNActivity.class);
                startActivity(intent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void Function_For_Post() {



        String type=editTextType.getText().toString().trim();
        final String finalType;
        final String desc=editDesc.getText().toString().trim();
        final String cost=editTextCost.getText().toString().trim();
        final String caution=editTextCaution.getText().toString().trim();
        final String[] to_get_subtype=type.split(" ");
        StringBuffer forSubtype=new StringBuffer("");
        if(to_get_subtype.length>1)
        {
            type=to_get_subtype[0];
            for(int i=1;i<to_get_subtype.length;i++)
            {
                if(i<to_get_subtype.length-1)
                    forSubtype.append(to_get_subtype[i]).append(" ");
                else
                    forSubtype.append(to_get_subtype[i]);
            }

        }
        else
        {
            type=to_get_subtype[0];
        }
        finalType=type;
        subType=forSubtype.toString();

        if( !TextUtils.isEmpty(type) && !TextUtils.isEmpty(desc) && !TextUtils.isEmpty(cost) && !TextUtils.isEmpty(caution) && imageuri!=null){

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
                            newItem.child("type").setValue(finalType);
                            newItem.child("subType").setValue(subType);
                            newItem.child("desc").setValue(desc);
                            newItem.child("price").setValue(cost);
                            newItem.child("caution").setValue(caution);
                            newItem.child("user_id").setValue(userId);
                            newItem.child("popularity").setValue(0);
                            newItem.child("user_name").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){

                                        startActivity(new Intent(Post_Item.this,MainPageNActivity.class));
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
            addButton.setImageURI(imageuri);
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

}


