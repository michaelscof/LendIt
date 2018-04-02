package com.example.pankaj.new_additem;

import android.app.Application;
import android.widget.ImageButton;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by pankaj on 22/3/18.
 */

public class ForUserOffline extends Application{

   @Override
    public void onCreate(){
       super.onCreate();
       if(!FirebaseApp.getApps(this).isEmpty()) {
           FirebaseDatabase.getInstance().setPersistenceEnabled(true);
       }
   }
}
