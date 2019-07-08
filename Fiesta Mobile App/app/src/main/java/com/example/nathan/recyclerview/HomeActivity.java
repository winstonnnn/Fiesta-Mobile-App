package com.example.nathan.recyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

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

import static com.example.nathan.recyclerview.MainActivity.EXTRA_FIESTA;
import static com.example.nathan.recyclerview.MainActivity.EXTRA_HISTORY;
import static com.example.nathan.recyclerview.MainActivity.EXTRA_LATITUDE;
import static com.example.nathan.recyclerview.MainActivity.EXTRA_LONGITUDE;
import static com.example.nathan.recyclerview.MainActivity.EXTRA_MUNICIPAL;
import static com.example.nathan.recyclerview.MainActivity.EXTRA_PROVINCE;
import static com.example.nathan.recyclerview.MainActivity.EXTRA_URL;

public class HomeActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> festIdList;
    ArrayList<String> festList;
    ArrayList<String> municipalList;
    ArrayList<String> provinceList;
    ArrayList<String> historyList;
    ArrayList<String> imageList;
    ArrayList<String> latitudeList;
    ArrayList<String> longitudeList;

    ViewFlipper viewFlipper;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewFlipper = findViewById(R.id.viewFlipper);
        viewFlipper.setAutoStart(true);
        viewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        viewFlipper.setFlipInterval(1800);

        festIdList = new ArrayList<>();
        festList = new ArrayList<>();
        municipalList = new ArrayList<>();
        provinceList = new ArrayList<>();
        historyList = new ArrayList<>();
        imageList = new ArrayList<>();
        latitudeList = new ArrayList<>();
        longitudeList = new ArrayList<>();

        listView = findViewById(R.id.reviewedList);

        festIdList = getIntent().getStringArrayListExtra("id");
        festList = getIntent().getStringArrayListExtra("festList");
        municipalList = getIntent().getStringArrayListExtra("municipalList");
        provinceList = getIntent().getStringArrayListExtra("provinceList");
        historyList = getIntent().getStringArrayListExtra("historyList");
        imageList = getIntent().getStringArrayListExtra("imageList");
        latitudeList = getIntent().getStringArrayListExtra("latitudeList");
        longitudeList = getIntent().getStringArrayListExtra("longitudeList");

        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);

    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return festList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.example_item, null);

            final TextView fest_name = convertView.findViewById(R.id.text_view_name);
            TextView province = convertView.findViewById(R.id.text_view_provice);


            fest_name.setText(festList.get(position));
            province.setText(municipalList.get(position) +", "+ provinceList.get(position));



            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detailIntent = new Intent(HomeActivity.this, details.class);
                    detailIntent.putExtra(EXTRA_FIESTA, festList.get(position));
                    detailIntent.putExtra(EXTRA_PROVINCE, provinceList.get(position));
                    detailIntent.putExtra(EXTRA_URL, imageList.get(position));
                    detailIntent.putExtra(EXTRA_MUNICIPAL, municipalList.get(position));
                    detailIntent.putExtra(EXTRA_HISTORY, historyList.get(position));
                    detailIntent.putExtra(EXTRA_LATITUDE, latitudeList.get(position));
                    detailIntent.putExtra(EXTRA_LONGITUDE, longitudeList.get(position));
                    detailIntent.putStringArrayListExtra("id", festIdList);
                    detailIntent.putExtra("fest_id", festIdList.get(position));
                    detailIntent.putStringArrayListExtra("event_date", MainActivity.dateList);
                    detailIntent.putStringArrayListExtra("event_name", MainActivity.eventList);
                    detailIntent.putStringArrayListExtra("event_sponsors", MainActivity.sponsorList);
                    detailIntent.putStringArrayListExtra("email", MainActivity.emailList);
                    detailIntent.putStringArrayListExtra("comment", MainActivity.commentList);

                    startActivity(detailIntent);
                }
            });
            return convertView;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
