package com.example.pankaj.new_additem;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.List;

public class MainPageActivity extends AppCompatActivity {

    private List<com.example.pankaj.new_additem.Product> productList;

    //the recyclerview
    private RecyclerView recyclerView;
    private RecyclerView popularitywiseView;
    private FirebaseAuth auth;
    private DatabaseReference itemfirebaseDatabase;
    private GoogleApiClient mGoogleApiClient;
    private DatabaseReference databaseStudents;
    //private FirebaseRecyclerAdapter<Item,PopularItemAdaptor> popularItemAdaptor;
    private ImageView search_text;
    private FirebaseAuth.AuthStateListener authStateListener;
    private int count=0;
    private String itemkey;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);


        //getting the recyclerview from xml
        auth= FirebaseAuth.getInstance();
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser()==null)
                {
                    startActivity(new Intent(MainPageActivity.this,LoginActivity.class));
                    finish();
                }

            }
        };
        /*itemfirebaseDatabase=FirebaseDatabase.getInstance().getReference().child("Item");
        itemfirebaseDatabase.keepSynced(true);
        itemfirebaseDatabase.keepSynced(true);
        setValue();
        search_text=findViewById(R.id.Search_category);
        search_text.setSelected(false);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView = findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        popularitywiseView=findViewById(R.id.PopularityOrder_view);
        popularitywiseView.setHasFixedSize(true);
        LinearLayoutManager layout_manager1 = new LinearLayoutManager(this);
        layout_manager1.setReverseLayout(true);
        layout_manager1.setStackFromEnd(true);
        popularitywiseView.setLayoutManager(layout_manager1);
        databaseStudents= FirebaseDatabase.getInstance().getReference().child("students");


        FirebaseRecyclerOptions<Item> options;
        /*scoresRef.orderByValue().limitToLast(3).on("value", function(snapshot) {
            snapshot.forEach(function(data) {
                console.log("The " + data.key + " score is " + data.val());
            });
        });*/

        //orderByChild("All Items").equalTo(category_search)
        /*query = itemfirebaseDatabase.orderByChild("popularity").limitToFirst(10);
             options= new FirebaseRecyclerOptions.Builder<Item>()
                    .setQuery(query, Item.class)
                    .build();

        popularItemAdaptor =new FirebaseRecyclerAdapter<Item, PopularItemAdaptor>(options) {
            @Override
            public PopularItemAdaptor onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.post_look, parent, false);

                return new PopularItemAdaptor(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull PopularItemAdaptor holder, int position, @NonNull Item model) {

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
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                            count=1;
                            if(!auth.getCurrentUser().getUid().toString().equals(userid_of_itemowner))
                                getCount(itemkey);
                            Intent intent = new Intent(MainPageActivity.this, SendActivity.class);
                            intent.putExtra("user_id", userid_of_itemowner);
                            intent.putExtra("MainPage", userid_of_itemowner);
                            intent.putExtra("post_id",itemkey);
                            startActivity(intent);
                            finish();
                            //startActivity(new Intent(MainActivity.this,SendActivity.class));
                            //Toast.makeText(MainActivity.this, "view is clicked...", Toast.LENGTH_LONG).show();
                        }

                });

            }
        };

        search_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainPageActivity.this,Search_ItemActivity.class));

            }
        });

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
        //creating recyclerview adapter
        ProductAdapter adapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(adapter);
        popularitywiseView.setAdapter(popularItemAdaptor);
        popularItemAdaptor.startListening();*/
    }
    /*
    @Override
    public void onStart() {

        super.onStart();
        auth.addAuthStateListener(authStateListener);
        popularItemAdaptor.startListening();
        if(auth.getCurrentUser()!=null) {

            checkUserExist();
        }

    }

    private void setValue() {
        productList = new ArrayList<>();

        productList.add(
                new com.example.pankaj.new_additem.Product(

                        "Men_Clothing",
                        R.drawable.ic_mens));

        productList.add(
                new com.example.pankaj.new_additem.Product(
                        "Women_Clothing",
                        R.drawable.ic_women_clothing));

        productList.add(
                new com.example.pankaj.new_additem.Product(

                        "Men_Accessories",
                        R.drawable.ic_men_acc));
        productList.add(
                new com.example.pankaj.new_additem.Product(

                        "Women_Accessories",
                        R.drawable.ic_women_acc));

        productList.add(
                new com.example.pankaj.new_additem.Product(
                        "Men_Footwear",
                        R.drawable.ic_shoes));

        productList.add(
                new com.example.pankaj.new_additem.Product(

                        "Women_Footwear",
                        R.drawable.ic_women_shoes));
        productList.add(
                new com.example.pankaj.new_additem.Product(

                        "Stationary",
                        R.drawable.ic_book));

        productList.add(
                new com.example.pankaj.new_additem.Product(
                        "Sports",
                        R.drawable.ic_sports));

        productList.add(
                new com.example.pankaj.new_additem.Product(

                        "DailyUsage",
                        R.drawable.ic_daily));
        productList.add(
                new com.example.pankaj.new_additem.Product(

                        "Techs",
                        R.drawable.ic_stationary));

        productList.add(
                new com.example.pankaj.new_additem.Product(
                        "Others",
                        R.drawable.others));

        productList.add(
                new com.example.pankaj.new_additem.Product(

                        "All Items",
                        R.drawable.forward));

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
            final String userId=auth.getCurrentUser().getUid();
            Intent intent=new Intent(MainPageActivity.this,ProfileActivity.class);
            intent.putExtra("current_user_id",userId);
            startActivity(intent);
        }
        if(item.getItemId()==R.id.show_Userlist)
        {
            startActivity(new Intent(MainPageActivity.this,UserListActivity.class));
        }
        if(item.getItemId()==R.id.navigation_setting)
        {
            startActivity(new Intent(MainPageActivity.this,MainPageNActivity.class));
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

    class PopularItemAdaptor extends RecyclerView.ViewHolder{


        public PopularItemAdaptor(View itemView) {

            super(itemView);
        }
        public void setImage(Context ctx, String image){

            //  System.out.println("Image");
            ImageView post_image = (ImageView) itemView.findViewById(R.id.Image);
            Glide.with(ctx).load(image).into(post_image);
            // Picasso.with(ctx).load(image).into(post_image);
        }
        public void setDesc(String size)
        {
            // System.out.println("Size");
            TextView item_Desc;
            item_Desc = (TextView) itemView.findViewById(R.id.Desc);
            item_Desc.setText(size);
        }
        public void setCost(String cost)
        {
            //System.out.println("Cost");
            TextView item_Cost=(TextView) itemView.findViewById(R.id.Cost);
            item_Cost.setText("₹"+cost + "/day");
        }
        public void setCaution(String caution)
        {
            //System.out.println("Caution");
            TextView item_Caution=(TextView) itemView.findViewById(R.id.Caution);
            item_Caution.setText("₹"+caution+" caution money");
        }
        public void setType(String type)
        {
            System.out.println("Type");
            TextView item_Type=(TextView) itemView.findViewById(R.id.Type);
            item_Type.setText(type);
        }
        public void setPapularity(int count)
        {
            String p_string=String.valueOf(count);

            TextView popularity=(TextView) itemView.findViewById(R.id.Popularity);
            popularity.setText(p_string);

            // item_Type.setText(type);
        }
        public void setName(String Name)
        {
            TextView name=(TextView) itemView.findViewById(R.id.Name);
            name.setText(Name);

            // item_Type.setText(type);
        }

        public void setSubType(String subType) {

            TextView typeSub=(TextView) itemView.findViewById(R.id.subType);
            typeSub.setText(subType);
        }
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
    public void onBackPressed() {

        moveTaskToBack(true);
    }*/



}
