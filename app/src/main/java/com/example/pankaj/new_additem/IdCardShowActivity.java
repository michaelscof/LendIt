package com.example.pankaj.new_additem;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class IdCardShowActivity extends AppCompatActivity {

    private ImageView imageViewIdcard;
    private String userId;
    private DatabaseReference  databaseReferenceStudends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_card_show);

        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        imageViewIdcard=findViewById(R.id.idphoto);
        if(getIntent().hasExtra("current_user_id"))
        {
            userId=getIntent().getStringExtra("current_user_id");
        }
        else
        {
            userId=getIntent().getStringExtra("user_id");
        }



        databaseReferenceStudends = FirebaseDatabase.getInstance().getReference().child("students");
        getPhotoOfPhotoid();

    }

    public void getPhotoOfPhotoid() {

        DatabaseReference findIDphoto=databaseReferenceStudends.child(userId);
        findIDphoto.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String imageiduri=dataSnapshot.child("imageId").getValue().toString();
                Glide.with(IdCardShowActivity.this).load(imageiduri).into(imageViewIdcard);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
                    Intent intent=new Intent(getApplicationContext(),ProfileActivity.class);
                    intent.putExtra("current_user_id",userId);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent intent=new Intent(getApplicationContext(),ProfileActivity.class);
                    intent.putExtra("user_id",userId);
                    startActivity(intent);
                    finish();
                }
        }
        return super.onOptionsItemSelected(item);
    }
}
