package com.example.pankaj.new_additem;

import android.graphics.Color;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class Search_ItemActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, android.support.v7.widget.SearchView.OnQueryTextListener {

    private SearchAdaptor customAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private android.support.v7.widget.SearchView searchView;
    // private SearchView searchView;
    private List<String> Clothing;
    private List<String> Sports;
    private List<String> Books;
    private List<String> Daily_use;
    private List<String> Footwear;
    private ArrayList<String> items_List;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__item);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setSubtitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("  Search by Item name");
        setValue();

        recyclerView=findViewById(R.id.recylcerView);
        customAdapter=new SearchAdaptor(Search_ItemActivity.this,items_List);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(customAdapter);
        searchView=findViewById(R.id.search_view);

        /*materialSearchView=findViewById(R.id.search_view);
        //adapter = new CustomAdapter(names);


        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                if(newText!=null && !newText.isEmpty()){

                    ArrayList<String> found=new ArrayList<String>();
                    for(int i=0;i<names.size();i++)
                    {
                        if(names.get(i).toLowerCase().contains(newText.toLowerCase())){

                            found.add(names.get(i));
                        }
                    }
                    customAdapter=new CustomAdapter(MainActivity.this,found );
                    recyclerView.setAdapter(customAdapter);

                }
                else
                {
                    customAdapter=new CustomAdapter(MainActivity.this,names);
                    recyclerView.setAdapter(customAdapter);
                }
                return true;
            }
        });
        materialSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                prepareListData();
                // setting list adapter
                customAdapter=new CustomAdapter(MainActivity.this,names );
                recyclerView.setAdapter(customAdapter);
            }
        });*/

    }

    private void setValue() {

        items_List = new ArrayList<>();
        items_List.add("Men_Clothing shirts");
        items_List.add("Men_Clothing tshirts");
        items_List.add("Men_Clothing partyWears");
        items_List.add("Men_Clothing jeans");
        items_List.add("Men_Clothing trousers");
        items_List.add("Men_Clothing formalPants");
        items_List.add("Men_Clothing formalshirts");
        items_List.add("Men_Clothing jackets");
        items_List.add("Men_Accessories watches");
        items_List.add("Men_Accessories caps");
        items_List.add("Men_Accessories glasses");
        items_List.add("Men_Accessories belts");
        items_List.add("Men_Accessories bags");
        items_List.add("Men_Footwear formals");
        items_List.add("Men_Footwear sneakers");
        items_List.add("Men_Footwear sports");
        items_List.add("Men_Footwear boots");


        items_List.add("Women_Clothing shirts");
        items_List.add("Women_Clothing tshirts");
        items_List.add("Women_Clothing partyWears");
        items_List.add("Women_Clothing jeans");
        items_List.add("Women_Clothing trousers");
        items_List.add("Women_Clothing formalPants");
        items_List.add("Women_Clothing formalshirts");
        items_List.add("Women_Clothing jackets");
        items_List.add("Women_Accessories watches");
        items_List.add("Women_Accessories caps");
        items_List.add("Women_Accessories glasses");
        items_List.add("Women_Accessories belts");
        items_List.add("Women_Accessories bags");
        items_List.add("Women_Footwear formals");
        items_List.add("Women_Footwear sneakers");
        items_List.add("Women_Footwear sports");
        items_List.add("Women_Footwear sandles");

        items_List.add("Stationary books");
        items_List.add("Stationary calculators");
        items_List.add("Stationary musicalInstruments");
        items_List.add("Stationary papersheets");

        items_List.add("Sports basketball");
        items_List.add("Sports volleyball");
        items_List.add("Sports badminton");
        items_List.add("Sports cricketBall");
        items_List.add("Sports cricketBat");
        items_List.add("Sports football");
        items_List.add("Sports cycle");

        items_List.add("DailyUsage Boiler");

        items_List.add("Techs mouse");
        items_List.add("Techs keyboad");
        items_List.add("Techs harddisk");
        items_List.add("Techs pendrive");
        items_List.add("Techs rounter");
        items_List.add("Others no tag");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_item,menu);
        MenuItem item=menu.findItem(R.id.action_search);
        android.support.v7.widget.SearchView searchView1= (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(item);
        searchView1.setSubmitButtonEnabled(true);
        searchView1.setOnQueryTextListener(this);
        return true;
    }
    private void prepareListData() {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if(newText!=null && !newText.isEmpty()){

            ArrayList<String> found=new ArrayList<String>();
            for(int i=0;i<items_List.size();i++)
            {
                if(items_List.get(i).toString().toLowerCase().contains(newText.toLowerCase())){

                    found.add((String) items_List.get(i));
                }
            }
            customAdapter=new SearchAdaptor(Search_ItemActivity.this,found );
            recyclerView.setAdapter(customAdapter);

        }
        else
        {
            customAdapter=new SearchAdaptor(Search_ItemActivity.this,items_List);
            recyclerView.setAdapter(customAdapter);
        }
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

