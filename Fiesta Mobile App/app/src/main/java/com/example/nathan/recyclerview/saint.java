package com.example.nathan.recyclerview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
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

public class saint extends AppCompatActivity {

    ListView listView;
    ArrayList<String> Saintidlist;
    ArrayList<String> Saintnamelist;
    ArrayList<String> Saintimagelist;
    ArrayList<String> Sainthistorylist;
    ArrayList<String> provincelist;
    ArrayList<String> municipallist;

    public static final String url = "http://192.168.8.105/region1-tf/patron_details.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saint);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Saintidlist = new ArrayList<>();
        Saintnamelist = new ArrayList<>();
        Saintimagelist = new ArrayList<>();
        Sainthistorylist =new ArrayList<>();
        provincelist = new ArrayList<>();
        municipallist = new ArrayList<>();

        listView = findViewById(R.id.saintList);


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("patron");
                    for (int i = 0 ; i < jsonArray.length(); i++)
                    {

                        JSONObject fiesta_info = jsonArray.getJSONObject(i);
                        String Saintid = fiesta_info.getString("id");
                        String Saintname = fiesta_info.getString("patron_name");
                        String Saintimage = fiesta_info.getString("patron_img");
                        String Sainthistory = fiesta_info.getString("patron_description");
                        String Saintprovince = fiesta_info.getString("province");
                        String Saintminucipal =fiesta_info.getString("municipal");

                        Toast.makeText(saint.this, Saintname, Toast.LENGTH_SHORT).show();

                        Saintidlist.add(Saintid);
                        Saintnamelist.add(Saintname);
                        Saintimagelist.add(Saintimage);
                        Sainthistorylist.add(Sainthistory);
                        provincelist.add(Saintprovince);
                        municipallist.add(Saintminucipal);


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(saint.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);

        SaintAdapter saintAdapter = new SaintAdapter();
        listView.setAdapter(saintAdapter);
    }

    class SaintAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return Saintnamelist.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.saintadapter,null);

            TextView saintname = convertView.findViewById(R.id.saintname);
            TextView saintHistory = convertView.findViewById(R.id.saintHistory);
            TextView saintimage = convertView.findViewById(R.id.saintimage);
            TextView saintprovince = convertView.findViewById(R.id.saintprovince);
            TextView saintmunicipal = convertView.findViewById(R.id.saintmunicipal);


            saintname.setText(Saintnamelist.get(position));
            saintHistory.setText(Sainthistorylist.get(position));
            saintprovince.setText(provincelist.get(position));
            saintmunicipal.setText(municipallist.get(position));

            return convertView;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
