package com.example.pankaj.new_additem;

import android.content.Intent;
import android.os.Build;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationMessageActivity extends AppCompatActivity {

    private DatabaseReference databaseReferenceNotifications;
    private MessageNotificationRecyclerviewAdapter messageNotificationRecyclerviewAdapter;
    private List<NotificationMessage> Messagelist;
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private String Current_userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_message);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mAuth= FirebaseAuth.getInstance();
        Current_userId=mAuth.getCurrentUser().getUid();
        databaseReferenceNotifications= FirebaseDatabase.getInstance().getReference().child("Notifications").child(Current_userId);
        Messagelist=new ArrayList<>();
        recyclerView=findViewById(R.id.NotificationRecylerView);
        messageNotificationRecyclerviewAdapter= new MessageNotificationRecyclerviewAdapter(getApplicationContext(),Messagelist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageNotificationRecyclerviewAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Messagelist.clear();
        databaseReferenceNotifications.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot Snapshot) {

                for(DataSnapshot dataSnapshot: Snapshot.getChildren()) {

                    String message=dataSnapshot.child("message").getValue().toString();
                    String fromName=dataSnapshot.child("fromName").getValue().toString();
                    NotificationMessage notificationMessage=new NotificationMessage(fromName,message);
                    Messagelist.add(notificationMessage);
                    messageNotificationRecyclerviewAdapter.notifyDataSetChanged();
                }
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
                Intent intent=new Intent(getApplicationContext(),MainPageNActivity.class);
                startActivity(intent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

