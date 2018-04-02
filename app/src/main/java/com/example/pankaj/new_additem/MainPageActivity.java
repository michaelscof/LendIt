package com.example.pankaj.new_additem;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainPageActivity extends AppCompatActivity {

    private List<com.example.pankaj.new_additem.Product> productList;

    //the recyclerview
    private RecyclerView recyclerView;
    private FirebaseAuth auth;
    private FirebaseAuth authStateListener;
    private GoogleApiClient mGoogleApiClient;
    private DatabaseReference databaseStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        //getting the recyclerview from xml
        auth= FirebaseAuth.getInstance();
        if(auth.getCurrentUser()==null)
        {
            startActivity(new Intent(MainPageActivity.this,LoginActivity.class));
        }
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView = findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        databaseStudents= FirebaseDatabase.getInstance().getReference().child("students");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //initializing the productlist
        productList = new ArrayList<>();


        //adding some items to our list
        productList.add(
                new com.example.pankaj.new_additem.Product(

                        "books",
                        R.drawable.macbook));

        productList.add(
                new com.example.pankaj.new_additem.Product(
                        "shoes",
                        R.drawable.dellinspiron));

        productList.add(
                new com.example.pankaj.new_additem.Product(

                        "clothes",
                        R.drawable.surface));
        productList.add(
                new com.example.pankaj.new_additem.Product(

                        "money",
                        R.drawable.macbook));

        productList.add(
                new com.example.pankaj.new_additem.Product(
                        "shoes",
                        R.drawable.dellinspiron));

        productList.add(
                new com.example.pankaj.new_additem.Product(

                        "shirts",
                        R.drawable.surface));
        productList.add(
                new com.example.pankaj.new_additem.Product(

                        "jeans",
                        R.drawable.macbook));

        productList.add(
                new com.example.pankaj.new_additem.Product(
                        "shoes",
                        R.drawable.dellinspiron));

        productList.add(
                new com.example.pankaj.new_additem.Product(

                        "coats",
                        R.drawable.surface));
        productList.add(
                new com.example.pankaj.new_additem.Product(

                        "money",
                        R.drawable.macbook));

        productList.add(
                new com.example.pankaj.new_additem.Product(
                        "tshirts",
                        R.drawable.dellinspiron));

        productList.add(
                new com.example.pankaj.new_additem.Product(

                        "All Items",
                        R.drawable.forward));

        //creating recyclerview adapter
        ProductAdapter adapter = new ProductAdapter(this, productList);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onStart() {

        super.onStart();

        if(auth.getCurrentUser()!=null) {

            checkUserExist();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_AddItem){

            startActivity(new Intent(MainPageActivity.this,Post_Item.class));
        }
        if(item.getItemId()==R.id.action_logout)
        {
            logout();
        }
        if(item.getItemId()==R.id.show_Profile)
        {
            startActivity(new Intent(MainPageActivity.this,ProfileActivity.class));
        }
        if(item.getItemId()==R.id.show_Userlist)
        {
            startActivity(new Intent(MainPageActivity.this,UserListActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
    private void logout() {

        // String userId=auth.getCurrentUser().getUid().toString();
        // DatabaseReference tokenbaseReference=FirebaseDatabase.getInstance().getReference().child("tokens").child(userId);
        //tokenbaseReference.removeValue();

        auth.signOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        startActivity(new Intent(MainPageActivity.this,LoginActivity.class));


        //Intent out_login=new Intent(MainActivity.this,LoginActivity.class);
        //out_login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //startActivity(out_login);
        //startActivity(new Intent(MainActivity.this,Post_Item.class));
    }
    public void checkUserExist()
    {
        final String userId=auth.getCurrentUser().getUid();

        databaseStudents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(userId))
                {

                }
                else
                {
                    startActivity(new Intent(MainPageActivity.this, SetupAccountActivity.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}