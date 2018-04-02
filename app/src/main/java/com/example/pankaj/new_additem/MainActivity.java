package com.example.pankaj.new_additem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recycleitemlist;
    private DatabaseReference mdatabase,databaseStudents;
    private FirebaseRecyclerAdapter<Item, ItemHolder> mPeopleRVAdapter;
    private Query query;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String category_search;
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

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
        category_search=getIntent().getStringExtra("category");
        System.out.println(category_search);
        //Log.d("Heloo",category_search);
        mdatabase=FirebaseDatabase.getInstance().getReference().child("Item");
        databaseStudents=FirebaseDatabase.getInstance().getReference().child("students");
        databaseStudents.keepSynced(true);
        recycleitemlist = findViewById(R.id.item_lists);
        recycleitemlist.setHasFixedSize(true);
        recycleitemlist.setLayoutManager(new LinearLayoutManager(this));


        FirebaseRecyclerOptions<Item> options;
            if("All Items".equals(category_search))
                query = mdatabase;
            else
                query = mdatabase.orderByChild("type").equalTo(category_search);
            options =new FirebaseRecyclerOptions.Builder<Item>()
                            .setQuery(query, Item.class)
                            .build();
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
            protected void onBindViewHolder (MainActivity.ItemHolder holder,int position, Item model){
            // Bind the Chat object to the ChatHolder
            // ...
                    holder.setCost(model.getPrice());
                    holder.setCaution(model.getCaution());
                    holder.setType(model.getType());
                    holder.setSize(model.getSize());
                    holder.setImage(getBaseContext(),model.getImage());
                    final String userid_of_itemowner=model.getUser_id();
                    holder.mview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(auth.getCurrentUser().getUid().toString().equals(userid_of_itemowner)){

                                Toast.makeText(MainActivity.this, "can't make request because item is added by you", Toast.LENGTH_LONG).show();

                            }else {

                                Intent intent = new Intent(MainActivity.this, SendActivity.class);
                                intent.putExtra("user_id", userid_of_itemowner);
                                startActivity(intent);

                                //startActivity(new Intent(MainActivity.this,SendActivity.class));
                                //Toast.makeText(MainActivity.this, "view is clicked...", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

            }
        };
       recycleitemlist.setAdapter(mPeopleRVAdapter);
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
            Picasso.with(ctx).load(image).into(post_image);
        }
        public void setSize(String size)
        {
           // System.out.println("Size");
            TextView item_Size;
            item_Size = (TextView) mview.findViewById(R.id.Size);
            item_Size.setText(size);
        }
        public void setCost(String cost)
        {
            //System.out.println("Cost");
            TextView item_Cost=(TextView) mview.findViewById(R.id.Cost);
            item_Cost.setText(cost);
        }
        public void setCaution(String caution)
        {
            //System.out.println("Caution");
            TextView item_Caution=(TextView) mview.findViewById(R.id.Caution);
            item_Caution.setText(caution);
        }
        public void setType(String type)
        {
            System.out.println("Type");
            TextView item_Type=(TextView) mview.findViewById(R.id.Type);
            item_Type.setText(type);
        }
    }

    public void onBackPressed() {

        startActivity(new Intent(MainActivity.this,MainPageActivity.class));
        finish();
    }
}
