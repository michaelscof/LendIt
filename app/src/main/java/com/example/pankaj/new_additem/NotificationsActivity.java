package com.example.pankaj.new_additem;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationsActivity extends AppCompatActivity {

    private TextView noticeText,show;
    private DatabaseReference userdatabase;
    private CircleImageView pic_of_requestuser;
    private TextView request_userName;
    private Button acceptrequest;
    private FirebaseAuth mAuth;
    private String userId;
    private ProgressDialog progressDialog;
    private String name;
    private String Current_user_name;
    private String postId;
    private ImageView requestItemImage;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);


        String currentUserID=FirebaseAuth.getInstance().getUid().toString();
        getName(currentUserID);
        userId = getIntent().getStringExtra("fromid");
        userdatabase = FirebaseDatabase.getInstance().getReference().child("students").child(userId);
        mAuth = FirebaseAuth.getInstance();
        request_userName = findViewById(R.id.requesttextviewUsername);
        acceptrequest = findViewById(R.id.accept_request);
        pic_of_requestuser = findViewById(R.id.requestprofileimage);
        show=findViewById(R.id.show);
        requestItemImage=findViewById(R.id.imageItem);

        String getMessage = getIntent().getStringExtra("message");
        postId=getIntent().getStringExtra("postid");
        setImage();
        if(getMessage.equals("request is accepted")){

            show.setText("Reply From");
            acceptrequest.setVisibility(View.GONE);
        }

        noticeText = findViewById(R.id.notify);
        noticeText.setText(getMessage);
        progressDialog = new ProgressDialog(this);

        userdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();

                request_userName.setText(name);
                //Picasso.with(ProfileActivity.this).load(image).into(circleImageViewProfilepic);
                Glide.with(NotificationsActivity.this).load(image).into(pic_of_requestuser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        acceptrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String current_userId = mAuth.getCurrentUser().getUid().toString();
                //Toast.makeText(NotificationsActivity.this,"functionality is not added",Toast.LENGTH_LONG).show();
                //sending Notification of accept

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Notifications");
                progressDialog.setMessage("Notification is sending....");
                progressDialog.show();

                String mssg = "request is accepted";
                HashMap<String, String> notifications = new HashMap<>();
                notifications.put("fromName", Current_user_name);
                notifications.put("message", mssg);
                notifications.put("from", current_userId);
                notifications.put("postid",postId);
                DatabaseReference user_notification = databaseReference.child(userId).push();
                user_notification.setValue(notifications).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(NotificationsActivity.this, "Notification is successfully send", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),MainPageNActivity.class));
                            progressDialog.dismiss();
                        } else {
                            Toast.makeText(NotificationsActivity.this, "getting some error", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
            }
        });

    }

    private void setImage() {

        DatabaseReference singleItem=FirebaseDatabase.getInstance().getReference().child("Item").child(postId);
        singleItem.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("image").getValue()!=null){

                    Glide.with(getApplicationContext()).load(dataSnapshot.child("image").getValue().toString()).into(requestItemImage);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void getName(String user_id1)
    {
        DatabaseReference databaseReferencesingleuser=FirebaseDatabase.getInstance().getReference().child("students").child(user_id1);
        databaseReferencesingleuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!(dataSnapshot.child("name").getValue()==null))
                    Current_user_name=dataSnapshot.child("name").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
