package com.example.pankaj.new_additem;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    private TextView noticeText;
    private DatabaseReference userdatabase;
    private CircleImageView pic_of_requestuser;
    private TextView request_userName;
    private Button acceptrequest;
    private FirebaseAuth mAuth;
    private String userId;
    private ProgressDialog progressDialog;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);


        userId = getIntent().getStringExtra("fromid");
        userdatabase = FirebaseDatabase.getInstance().getReference().child("students").child(userId);
        mAuth = FirebaseAuth.getInstance();
        request_userName = findViewById(R.id.requesttextviewUsername);
        acceptrequest = findViewById(R.id.accept_request);
        pic_of_requestuser = findViewById(R.id.requestprofileimage);

        String getMessage = getIntent().getStringExtra("message");
        if(getMessage.equals("request is accepted")){

            acceptrequest.setVisibility(View.GONE);
        }

        noticeText = findViewById(R.id.notify);
        noticeText.setText(getMessage);
        progressDialog = new ProgressDialog(this);

        userdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
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
                notifications.put("message", mssg);
                notifications.put("from", current_userId);
                DatabaseReference user_notification = databaseReference.child(userId).push();
                user_notification.setValue(notifications).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(NotificationsActivity.this, "Notification is successfully send", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(NotificationsActivity.this, "getting some error", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
            }
        });

    }
}
