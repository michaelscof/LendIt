package com.example.pankaj.new_additem;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    private int timeout=1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {

            // Using handler with postDelayed called runnable run method

            @Override
            public void run() {

                FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
                if(firebaseAuth.getCurrentUser()!=null)
                {
                    startActivity(new Intent(SplashActivity.this,MainPageNActivity.class));
                    finish();
                }
                else {
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    finish();
                }
            }
        }, timeout); // wait for 5 seconds
    }

}
