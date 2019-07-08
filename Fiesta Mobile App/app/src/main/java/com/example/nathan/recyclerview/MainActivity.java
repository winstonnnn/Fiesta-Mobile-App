package com.example.nathan.recyclerview;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ExampleAdapter.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener{

    android.support.v7.widget.SearchView searchView;

    public static final String EXTRA_ID = "fest_id";
    public static final String EXTRA_URL = "image";
    public static final String EXTRA_FIESTA = "fest_name";
    public static final String EXTRA_PROVINCE = "province";
    public static final String EXTRA_MUNICIPAL ="municipal";
    public static final String EXTRA_HISTORY ="history";
    public static final String EXTRA_LATITUDE ="latitude";
    public static final String EXTRA_LONGITUDE ="longitude";

    private RecyclerView mRecyclerView;
    private ExampleAdapter mExampleAdapter;
    private ArrayList<ExampleItem> mExampleList;
    private RequestQueue mRequestQueue;

    public static final String url = "http://192.168.8.105/region1-tf/fiesta_reviews.php";
    public static final String url1 = "http://192.168.8.105/region1-tf/fiesta_details.php";
    public static final String url3 = "http://192.168.8.105/region1-tf/fiesta_event.php";


    //drawer
    private DrawerLayout drawer;
    private ActionBarDrawerToggle myToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView =findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mExampleList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(this);
        parseJSON();

        //drawer
        drawer =(DrawerLayout) findViewById(R.id.drawer);
        myToggle = new ActionBarDrawerToggle(this,drawer,R.string.open,R.string.close);

        drawer.addDrawerListener(myToggle);
        myToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //drawer

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        sendDataToHomeActivity();
        sendDataToDetailsActivity();
        sendCommentsToDetailsActivity();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (myToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_home){
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.putStringArrayListExtra("id", festIdlist);
            intent.putStringArrayListExtra("festList", festList);
            intent.putStringArrayListExtra("municipalList", municipalList);
            intent.putStringArrayListExtra("provinceList", provinceList);
            intent.putStringArrayListExtra("historyList", historyList);
            intent.putStringArrayListExtra("imageList", imageList);
            intent.putStringArrayListExtra("latitudeList", latitudeList);
            intent.putStringArrayListExtra("longitudeList", longitudeList);
            startActivity(intent);
        }else if (id == R.id.nav_gallery){
            Intent intent = new Intent(MainActivity.this, Gallery.class);
            startActivity(intent);
        }else if (id == R.id.nav_fiesta){
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_calendar){
            Intent intent = new Intent(MainActivity.this, Calendar.class);
            intent.putStringArrayListExtra("id", idList);
            intent.putStringArrayListExtra("event_date", dateList);
            intent.putStringArrayListExtra("event_name", eventList);
            intent.putStringArrayListExtra("event_sponsors", sponsorList);
            startActivity(intent);
        }else if (id == R.id.nav_profile){
            Intent intent = new Intent(MainActivity.this, Profile.class);
            startActivity(intent);
        }else if (id == R.id.nav_saint){
            Intent intent = new Intent(MainActivity.this, saint.class);
            startActivity(intent);
        }
        return true;
    }

    ArrayList<String> festName = new ArrayList<>();
    private void parseJSON() {

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("fiesta");
                    Log.d("MainActivity", "onResponse: "+ jsonArray);
                    for (int i = 0 ; i < jsonArray.length(); i++)
                    {
                        JSONObject fiesta_info = jsonArray.getJSONObject(i);
                        String fid = fiesta_info.getString("id");
                        String fname = fiesta_info.getString("fest_name");
                        String fprovice = fiesta_info.getString("province");
                        String imageurl = fiesta_info.getString("image");
                        String fmun = fiesta_info.getString("municipal");
                        String fhistory =fiesta_info.getString("fest_history");
                        String fLatitude = fiesta_info.getString("latitude");
                        String fLongitude = fiesta_info.getString("longitude");
                        mExampleList.add(new ExampleItem(fid,fname,fprovice,imageurl,fmun,fhistory, fLatitude, fLongitude));

                    }
                    mExampleAdapter = new ExampleAdapter(MainActivity.this, mExampleList);
                    mRecyclerView.setAdapter(mExampleAdapter);
                    mExampleAdapter.setOnItemClickListener(MainActivity.this);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onErrorResponse: "+ error.getMessage());
            }
        });
        mRequestQueue.add(request);
         }


         //wag gamitin ang onclick na to. nasa exampleadapter yung onclick
    @Override
    public void onItemClick(int position) {

       /* Intent detailIntent = new Intent(this,details.class);
        ExampleItem clickItem = mExampleList.get(position);
        detailIntent.putExtra(EXTRA_FIESTA, clickItem.getTest_name());
        detailIntent.putExtra(EXTRA_PROVINCE, clickItem.getProvince());
        detailIntent.putExtra(EXTRA_URL,clickItem.getImage());
        detailIntent.putExtra(EXTRA_MUNICIPAL,clickItem.getMunicipal());
        detailIntent.putExtra(EXTRA_HISTORY,clickItem.getHistory());
        startActivity(detailIntent);*/
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_item,menu);

        android.app.SearchManager searchManager = (android.app.SearchManager) getSystemService(android.content.Context.SEARCH_SERVICE);
        searchView = (android.support.v7.widget.SearchView)menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint(getString(R.string.search));
        searchView.setQueryHint(getString(R.string.search));
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                newText = newText.toLowerCase();
                ArrayList<ExampleItem> ExampleList = new ArrayList<>();
                for (ExampleItem exampleItem:mExampleList ){
                    String fish_name = exampleItem.getTest_name().toLowerCase();
                    if (fish_name.contains(newText))
                        ExampleList.add(exampleItem);


                }
                mExampleAdapter.setSearchOperation(ExampleList);

                return false;
            }

        });


        return true;

    }

    private long backPressed;
    @Override
    public void onBackPressed() {


        if (backPressed + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Press again to exit.", Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();

    }

    //sending data to saint activity



    //sending data to home activity
    ArrayList<String>  festIdlist;
    ArrayList<String> festList;
    ArrayList<String> municipalList;
    ArrayList<String> provinceList;
    ArrayList<String> historyList;
    ArrayList<String> imageList;
    ArrayList<String> latitudeList;
    ArrayList<String> longitudeList;

    private void sendDataToHomeActivity(){
        festIdlist = new ArrayList<>();
        festList = new ArrayList<>();
        municipalList = new ArrayList<>();
        provinceList = new ArrayList<>();
        historyList = new ArrayList<>();
        imageList = new ArrayList<>();
        latitudeList = new ArrayList<>();
        longitudeList = new ArrayList<>();



        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("reviewed_festivals");
                    for (int i = 0 ; i < jsonArray.length(); i++)
                    {

                        JSONObject fiesta_info = jsonArray.getJSONObject(i);
                        String id = fiesta_info.getString("id");
                        String fname = fiesta_info.getString("fest_name");
                        String fprovice = fiesta_info.getString("province");
                        String imageurl = fiesta_info.getString("image");
                        String fmun = fiesta_info.getString("municipal");
                        String fhistory =fiesta_info.getString("fest_history");
                        String fLatitude = fiesta_info.getString("latitude");
                        String fLongitude = fiesta_info.getString("longitude");

                        festIdlist.add(id);
                        festList.add(fname);
                        municipalList.add(fmun);
                        provinceList.add(fprovice);
                        historyList.add(fhistory);
                        imageList.add(imageurl);
                        latitudeList.add(fLatitude);
                        longitudeList.add(fLongitude);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onErrorResponse: " + error.getMessage());
            }
        });
        requestQueue.add(request);


    }


    //sending data of events to details activity
    public static ArrayList<String> idList;
    public static ArrayList<String> eventList;
    public static ArrayList<String> dateList;
    public static ArrayList<String> sponsorList;


    private void sendDataToDetailsActivity(){
        idList = new ArrayList<>();
        dateList = new ArrayList<>();
        eventList = new ArrayList<>();
        sponsorList = new ArrayList<>();


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url3, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("events");
                    for (int i = 0 ; i < jsonArray.length(); i++)
                    {
                        JSONObject fiesta_info = jsonArray.getJSONObject(i);
                        String fid = fiesta_info.getString("festival_id");
                        String event_name = fiesta_info.getString("event_name");
                        String event_date = fiesta_info.getString("event_date");
                        String event_sponsors = fiesta_info.getString("event_sponsors");


                        idList.add(fid);
                        eventList.add(event_name);
                        dateList.add(event_date);
                        sponsorList.add(event_sponsors);


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);


    }

    //sending data of events to details activity
    public static ArrayList<String> emailList;
    public static ArrayList<String> commentList;

    private void sendCommentsToDetailsActivity(){
        //idList = new ArrayList<>();
        emailList = new ArrayList<>();
        commentList = new ArrayList<>();


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("reviewed_festivals");
                    for (int i = 0 ; i < jsonArray.length(); i++)
                    {
                        JSONObject fiesta_info = jsonArray.getJSONObject(i);
                        String fid = fiesta_info.getString("id");
                        String email = fiesta_info.getString("email");
                        String comment = fiesta_info.getString("comment");


                        //idList.add(fid);
                        emailList.add(email);
                        commentList.add(comment);



                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);


    }
}

