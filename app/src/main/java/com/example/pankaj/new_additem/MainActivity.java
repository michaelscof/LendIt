package com.example.pankaj.new_additem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
//import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recycleitemlist;
    private DatabaseReference mdatabase,databaseStudents;
    private FirebaseRecyclerAdapter<Item, ItemHolder> mPeopleRVAdapter;
    private Query query;
    private FirebaseAuth auth;
    private String itemkey;
    private int count;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String category_search="default1";
    private String subType="default2";
    private String exactFilterType;
    private String suborCategory;
    private String userList_Items="default3";
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        auth=FirebaseAuth.getInstance();
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser()==null)
                {
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                }
                
            }
        };
        if (getIntent().getExtras() == null && (getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)!=0) {

            startActivity(new Intent(MainActivity.this,MainPageActivity.class));
            finish();
        }
        setContentView(R.layout.activity_main);
        if(getIntent().hasExtra("category")) {
            category_search = getIntent().getStringExtra("category");
        }
        else if(getIntent().hasExtra("From_Search"))
        {
            subType=getIntent().getStringExtra("From_Search");
        }
        else
            userList_Items=getIntent().getStringExtra("From_profile");
        System.out.println(category_search);
        //Log.d("Heloo",category_search);
        mdatabase=FirebaseDatabase.getInstance().getReference().child("Item");
        mdatabase.keepSynced(true);
        databaseStudents=FirebaseDatabase.getInstance().getReference().child("students");
        recycleitemlist = findViewById(R.id.item_lists);
        recycleitemlist.setHasFixedSize(true);
        recycleitemlist.setLayoutManager(new LinearLayoutManager(this));


        FirebaseRecyclerOptions<Item> options;
        if(!category_search.equals("default1")) {

            suborCategory="type1";

            System.out.println();
            if ("All Items".equals(category_search)) {
                query = mdatabase;
                exactFilterType = "All Items";
            }
            else
                query = mdatabase.orderByChild("type").equalTo(category_search);
            options = new FirebaseRecyclerOptions.Builder<Item>()
                    .setQuery(query, Item.class)
                    .build();
            exactFilterType=category_search;
        }
        else if(!subType.equals("default2"))
        {
            suborCategory="type2";
            System.out.println(subType);
            query = mdatabase.orderByChild("subType").equalTo(subType);
            options = new FirebaseRecyclerOptions.Builder<Item>()
                    .setQuery(query, Item.class)
                    .build();
            exactFilterType=subType;
        }
        else
        {
            suborCategory="type3";
            query = mdatabase.orderByChild("user_id").equalTo(userList_Items);
            options = new FirebaseRecyclerOptions.Builder<Item>()
                    .setQuery(query, Item.class)
                    .build();
            exactFilterType=userList_Items;
        }
        mPeopleRVAdapter =new FirebaseRecyclerAdapter<Item, MainActivity.ItemHolder>(options) {
            @Override
            public ItemHolder onCreateViewHolder (ViewGroup parent,int viewType){
            // Create a new instance of the ViewHolder, in this case we are using a custom
            // layout called R.layout.message for each item
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.post_look, parent, false);

            return new ItemHolder(view);
        }

            @Override
            protected void onBindViewHolder (MainActivity.ItemHolder holder, final int position, Item model){
            // Bind the Chat object to the ChatHolder
            // ...
                    itemkey=getRef(position).getKey();
                    System.out.println(itemkey);
                    holder.setCost(model.getPrice());
                    holder.setCaution(model.getCaution());
                    holder.setType(model.getType());
                    holder.setDesc(model.getDesc());
                    holder.setName(model.getUser_name());
                    holder.setSubType(model.getSubType());
                    holder.setPapularity(model.getPopularity());
                    holder.setImage(getBaseContext(),model.getImage());
                    final String userid_of_itemowner=model.getUser_id();
                    holder.mview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                          //  if(auth.getCurrentUser().getUid().toString().equals(userid_of_itemowner)){

                          //       Toast.makeText(MainActivity.this, "can't make request because item is added by you", Toast.LENGTH_LONG).show();
                           // else {
                                count=1;
                                if(!auth.getCurrentUser().getUid().toString().equals(userid_of_itemowner))
                                    getCount(itemkey);
                                Intent intent = new Intent(MainActivity.this, SendActivity.class);
                                intent.putExtra("user_id", userid_of_itemowner);
                                intent.putExtra("post_id",itemkey);
                                if(suborCategory.equals("type1"))
                                {
                                    intent.putExtra("category", exactFilterType);
                                }
                                else if(suborCategory.equals("type2"))
                                    intent.putExtra("From_Search", exactFilterType);
                                else
                                    intent.putExtra("From_profile",exactFilterType);
                                startActivity(intent);
                                finish();
                                //startActivity(new Intent(MainActivity.this,SendActivity.class));
                                //Toast.makeText(MainActivity.this, "view is clicked...", Toast.LENGTH_LONG).show();

                        }
                    });

            }
        };
       recycleitemlist.setAdapter(mPeopleRVAdapter);
        mPeopleRVAdapter.startListening();
}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id)
        {
            case android.R.id.home:

                if(getIntent().hasExtra("From_profile"))
                {
                    Intent intent=new Intent(getApplicationContext(),ProfileActivity.class);
                    intent.putExtra("current_user_id",auth.getUid().toString());
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), MainPageNActivity.class);
                    startActivity(intent);
                    finish();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void getCount(final String itemkey1) {

        final DatabaseReference itemref = FirebaseDatabase.getInstance().getReference();
        itemref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(count==1) {
                    count = dataSnapshot.child("Item").child(itemkey1).child("popularity").getValue(Integer.class);
                    count++;
                    System.out.println("I" + count);
                    System.out.println("O" + count);
                    DatabaseReference itemreference = FirebaseDatabase.getInstance().getReference();
                    itemreference.child("Item").child(itemkey).child("popularity").setValue(count);
                    count=0;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {

        super.onStart();
        auth.addAuthStateListener(authStateListener);
        mPeopleRVAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPeopleRVAdapter.stopListening();
    }


    public static class ItemHolder extends RecyclerView.ViewHolder{

        View mview;
        public ItemHolder(View itemView) {
            super(itemView);

            mview=itemView;
        }
        public void setImage(Context ctx, String image){

          //  System.out.println("Image");
            ImageView post_image = (ImageView) mview.findViewById(R.id.Image);
            Glide.with(ctx).load(image).into(post_image);
           // Picasso.with(ctx).load(image).into(post_image);
        }
        public void setDesc(String size)
        {
           // System.out.println("Size");
            TextView item_Desc;
            item_Desc = (TextView) mview.findViewById(R.id.Desc);
            item_Desc.setText(size);
        }
        public void setCost(String cost)
        {
            //System.out.println("Cost");
            TextView item_Cost=(TextView) mview.findViewById(R.id.Cost);
            item_Cost.setText("₹"+cost + "/day");
        }
        public void setCaution(String caution)
        {
            //System.out.println("Caution");
            TextView item_Caution=(TextView) mview.findViewById(R.id.Caution);
            item_Caution.setText("₹"+caution+" caution money");
        }
        public void setType(String type)
        {
            System.out.println("Type");
            TextView item_Type=(TextView) mview.findViewById(R.id.Type);
            item_Type.setText(type);
        }
        public void setPapularity(int count)
        {
            String p_string=String.valueOf(count);

            TextView popularity=(TextView) mview.findViewById(R.id.Popularity);
            popularity.setText(p_string);

           // item_Type.setText(type);
        }
        public void setName(String Name)
        {
            TextView name=(TextView) mview.findViewById(R.id.Name);
            name.setText(Name);

            // item_Type.setText(type);
        }

        public void setSubType(String subType) {

            TextView typeSub=(TextView) mview.findViewById(R.id.subType);
            typeSub.setText(subType);
        }
    }

    public void onBackPressed() {

        startActivity(new Intent(MainActivity.this,MainPageNActivity.class));
        finish();
    }
}
