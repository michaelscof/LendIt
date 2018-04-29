package com.example.pankaj.new_additem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import java.util.HashMap;

public class SendActivity extends AppCompatActivity {

    private TextView abcdUser;
    private EditText sendText;
    private Button sendButton;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private String postkey;
    private TextView postInfo;
    private Button deletePost;
    private String user_name;
    private String user_id;
    private DatabaseReference Itemsdatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        user_id=getIntent().getStringExtra("user_id").toString();
        postkey=getIntent().getStringExtra("post_id");
        //user_name=getIntent().getStringExtra("user_name").toString();
        //abcdUser=findViewById(R.id.abcdUser);
        sendText=findViewById(R.id.sendText);
        sendButton=findViewById(R.id.sendButton);
        postInfo=findViewById(R.id.Postinfo);
        deletePost=findViewById(R.id.deletePostBotton);
        //abcdUser.setText(user_name);
        mAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        final String current_user_id=mAuth.getCurrentUser().getUid().toString();
        getName(current_user_id);
        if(current_user_id.equals(user_id))
        {
            postInfo.setText("Delete Post");
            sendText.setVisibility(View.GONE);
            sendButton.setVisibility(View.GONE);
        }
        else
        {
            deletePost.setVisibility(View.GONE);
        }
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Notifications");
        Itemsdatabase=FirebaseDatabase.getInstance().getReference().child("Item");
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setMessage("Notification is sending....");
                progressDialog.show();

                String mssg=sendText.getText().toString();
                HashMap<String,String> notifications=new HashMap<>();
                notifications.put("message",mssg);
                notifications.put("from",current_user_id);
                notifications.put("fromName",user_name);
                notifications.put("postid",postkey);
                DatabaseReference user_notification=databaseReference.child(user_id).push();
                user_notification.setValue(notifications).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){

                            sendText.setText("");
                            Toast.makeText(SendActivity.this,"Notification is successfully send",Toast.LENGTH_LONG).show();
                            //startActivity(new Intent(SendActivity.this,MainPageActivity.class));
                        }else
                        {
                            Toast.makeText(SendActivity.this,"getting some error",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
            }
        });

        deletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setMessage("Post is deleting...");
                progressDialog.show();

                Itemsdatabase.child(postkey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {
                            progressDialog.dismiss();
                            startActivity(new Intent(getApplicationContext(),MainPageNActivity.class));
                        }
                        else {

                            Toast.makeText(getApplicationContext(),"Error while deleting post",Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id)
        {
            case android.R.id.home:

                if(getIntent().hasExtra("MainPage"))
                {
                    startActivity(new Intent(getApplicationContext(),MainPageNActivity.class));
                    finish();
                }
                else if(getIntent().hasExtra("category"))
                {
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    intent.putExtra("category", getIntent().getStringExtra("category"));
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    intent.putExtra("From_Search", getIntent().getStringExtra("From_Search"));
                    startActivity(intent);
                    finish();
                }

        }
        return super.onOptionsItemSelected(item);
    }
    public void getName(String user_id1)
    {
        DatabaseReference databaseReferencesingleuser=FirebaseDatabase.getInstance().getReference().child("students").child(user_id1);
        databaseReferencesingleuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!(dataSnapshot.child("name").getValue()==null))
                    user_name=dataSnapshot.child("name").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
