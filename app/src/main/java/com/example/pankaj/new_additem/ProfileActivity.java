package com.example.pankaj.new_additem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private TextView textViewUsername,textViewEmail;
    private CircleImageView circleImageViewProfilepic;
    private Button submit;
    private DatabaseReference databaseReferenceUser;
    private FirebaseAuth mAuth;
    private String EMAIL,user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        mAuth=FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null){
            EMAIL= mAuth.getCurrentUser().getEmail().toString();
            user_id=mAuth.getCurrentUser().getUid().toString();
        }
        databaseReferenceUser=FirebaseDatabase.getInstance().getReference().child("students").child(user_id);
        textViewUsername=findViewById(R.id.textviewUsername);
        textViewEmail=findViewById(R.id.textviewEmail);
        circleImageViewProfilepic=findViewById(R.id.profileimage);
        submit=findViewById(R.id.submitProfile);
        databaseReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name=dataSnapshot.child("name").getValue().toString();
                String image=dataSnapshot.child("image").getValue().toString();

                textViewUsername.setText(name);
                //Picasso.with(ProfileActivity.this).load(image).into(circleImageViewProfilepic);
                textViewEmail.setText(EMAIL);
                Glide.with(ProfileActivity.this).load(image).into(circleImageViewProfilepic);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(ProfileActivity.this,"Not having any action",Toast.LENGTH_LONG).show();

            }
        });

    }
}
