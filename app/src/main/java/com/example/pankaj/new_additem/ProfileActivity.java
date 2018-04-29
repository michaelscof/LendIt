package com.example.pankaj.new_additem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private TextView textViewUsername,textViewEmail;
    private TextView textViewStatus,textViewAddress;
    private TextView textViewAddedItems,textViewIdpics;
    private ImageView circleImageViewProfilepic;
    private ImageView submit;
    private DatabaseReference databaseReferenceUser;
    private FirebaseAuth mAuth;
    private String EMAIL,user_id;
    private String getUser_id;
    private String FixFilterType;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mAuth=FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null){
            EMAIL= mAuth.getCurrentUser().getEmail().toString();
            user_id=mAuth.getCurrentUser().getUid().toString();
        }
        submit=findViewById(R.id.drop_down_option_menu);
        textViewEmail=findViewById(R.id.user_profile_short_bio);
        textViewAddedItems=findViewById(R.id.item_lists);
        if(getIntent().hasExtra("current_user_id"))
        {
            FixFilterType="current_user_id";
            getUser_id=user_id;
            textViewEmail.setText(EMAIL);
        }
        else{

            FixFilterType="user_id";
            getUser_id=getIntent().getStringExtra("user_id");
            submit.setVisibility(View.GONE);
            textViewEmail.setVisibility(View.GONE);
            textViewAddedItems.setVisibility(View.GONE);
        }
        databaseReferenceUser=FirebaseDatabase.getInstance().getReference().child("students").child(getUser_id);
        textViewUsername=findViewById(R.id.user_profile_name);
        textViewEmail=findViewById(R.id.user_profile_short_bio);
        circleImageViewProfilepic=findViewById(R.id.user_profile_photo);
        textViewAddress=findViewById(R.id.addressText);
        textViewStatus=findViewById(R.id.statusText);

        textViewIdpics=findViewById(R.id.Id_view);
        submit=findViewById(R.id.drop_down_option_menu);
        databaseReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("name").getValue()==null || dataSnapshot.child("image").getValue()==null)
                {
                    Toast.makeText(getApplicationContext(),"Name or image is returing null from database",Toast.LENGTH_LONG);
                    startActivity(new Intent(getApplicationContext(),MainPageActivity.class));
                }

                String name= (String) dataSnapshot.child("name").getValue();
                String image= (String) dataSnapshot.child("image").getValue();
                if(name==null || image==null)
                {
                    Toast.makeText(getApplicationContext(),"Name or image is returing null from database",Toast.LENGTH_LONG);
                    startActivity(new Intent(getApplicationContext(),MainPageActivity.class));
                }

                textViewUsername.setText(name);
                //Picasso.with(ProfileActivity.this).load(image).into(circleImageViewProfilepic);
                textViewAddress.setText(dataSnapshot.child("address").getValue().toString());
                textViewStatus.setText(dataSnapshot.child("status").getValue().toString());
                if (!ProfileActivity.this.isFinishing()) {
                    Glide.with(ProfileActivity.this).load(image).into(circleImageViewProfilepic);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),EditProfileActivity.class));

            }
        });
        textViewAddedItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("From_profile",getUser_id);
                startActivity(intent);
                finish();
            }
        });
        textViewIdpics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),IdCardShowActivity.class);
                intent.putExtra(FixFilterType,getUser_id);
                startActivity(intent);
                finish();

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                if(getIntent().hasExtra("current_user_id"))
                {
                    Intent intent=new Intent(getApplicationContext(),MainPageNActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent intent=new Intent(getApplicationContext(),UserListActivity.class);
                    startActivity(intent);
                    finish();
                }

        }
        return super.onOptionsItemSelected(item);
    }
}
