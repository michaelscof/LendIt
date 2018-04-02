package com.example.pankaj.new_additem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SendActivity extends AppCompatActivity {

    private TextView abcdUser;
    private EditText sendText;
    private Button sendButton;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        final String user_id=getIntent().getStringExtra("user_id").toString();
       // final String user_name=getIntent().getStringExtra("user_name").toString();
        //abcdUser=findViewById(R.id.abcdUser);
        sendText=findViewById(R.id.sendText);
        sendButton=findViewById(R.id.sendButton);
        //abcdUser.setText(user_name);
        mAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        final String current_user_id=mAuth.getCurrentUser().getUid().toString();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Notifications");
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setMessage("Notification is sending....");
                progressDialog.show();

                String mssg=sendText.getText().toString();
                HashMap<String,String> notifications=new HashMap<>();
                notifications.put("message",mssg);
                notifications.put("from",current_user_id);
                DatabaseReference user_notification=databaseReference.child(user_id).push();
                user_notification.setValue(notifications).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){

                            sendText.setText("");
                            Toast.makeText(SendActivity.this,"Notification is successfully send",Toast.LENGTH_LONG).show();
                        }else
                        {
                            Toast.makeText(SendActivity.this,"getting some error",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
            }
        });

    }
}
