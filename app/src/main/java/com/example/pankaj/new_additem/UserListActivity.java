package com.example.pankaj.new_additem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<User> userlist;
    private UserRecyclerviewAdapter userRecyclerviewAdapter;
    private DatabaseReference databaseReferenceUser;
    private FirebaseAuth mAuth;
    private String Current_userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        databaseReferenceUser= FirebaseDatabase.getInstance().getReference().child("students");
        mAuth=FirebaseAuth.getInstance();
        Current_userId=mAuth.getCurrentUser().getUid();
        userlist=new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerview);
        userRecyclerviewAdapter=new UserRecyclerviewAdapter(UserListActivity.this,userlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(userRecyclerviewAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        userlist.clear();
        databaseReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot Snapshot) {

                for(DataSnapshot dataSnapshot: Snapshot.getChildren()) {


                    String user_id = dataSnapshot.getKey().toString();

                        if (!user_id.equals(Current_userId)) {

                            String name = (String) dataSnapshot.child("name").getValue();

                            String image = dataSnapshot.child("image").getValue().toString();

                            User user = new User(image, name, user_id);

                            userlist.add(user);

                            userRecyclerviewAdapter.notifyDataSetChanged();
                        }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
