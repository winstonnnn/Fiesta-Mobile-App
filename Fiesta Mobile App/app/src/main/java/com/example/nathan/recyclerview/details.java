package com.example.nathan.recyclerview;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.nathan.recyclerview.MainActivity.EXTRA_URL;

public class details extends AppCompatActivity {

    public final static String insertRating = "http://192.168.8.101/region1-tf/resources/views/ratings.php";
    public static final String url = "http://192.168.8.101/region1-tf/fiesta_reviews.php";

    //

    //for events
    ArrayList<String> idList;
    ArrayList<String> eventList;
    ArrayList<String> dateList;
    ArrayList<String> sponsorList;
    String fest_id;

    //holder for events
    ArrayList<String> eventNameHolder = new ArrayList<>();
    ArrayList<String> dateHolder = new ArrayList<>();
    ArrayList<String> sponsorHolder = new ArrayList<>();


    //for comments
    ArrayList<String> emailList;
    ArrayList<String> commentList;

    //holder for events
    ArrayList<String> emailHolder = new ArrayList<>();
    ArrayList<String> commentHolder = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Intent intent = getIntent();
        String image = intent.getStringExtra(EXTRA_URL);
        final String fiesta = intent.getStringExtra(MainActivity.EXTRA_FIESTA);
        fest_id = intent.getStringExtra("fest_id");
        final String prov = intent.getStringExtra(MainActivity.EXTRA_PROVINCE);
        final String mun = intent.getStringExtra(MainActivity.EXTRA_MUNICIPAL);
        String history =intent.getStringExtra(MainActivity.EXTRA_HISTORY);

        ImageView imageView = findViewById(R.id.image_view_detail);
        TextView textView = findViewById(R.id.text_view_name);
        TextView textView1 = findViewById(R.id.text_view_provice);
        TextView textView2 = findViewById(R.id.text_view_municipal);
        TextView textView3 = findViewById(R.id.text_view_history);

        Picasso.with(this).load(image).fit().centerInside().into(imageView);
        textView.setText(fiesta);
        textView1.setText(prov);
        textView2.setText(mun);
        textView3.setText(history);



        //viewing the location of fiesta
        Button viewLocation = findViewById(R.id.viewLocation);
        viewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(details.this, MapsActivity.class);
                intent1.putExtra("latitude", getIntent().getStringExtra("latitude"));
                intent1.putExtra("longitude", getIntent().getStringExtra("longitude"));
                intent1.putExtra("fest_name", fiesta);
                intent1.putExtra("municipality", mun);
                intent1.putExtra("province", prov);
                startActivity(intent1);
            }
        });

        Button comment = findViewById(R.id.comment);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(details.this);
                dialog.setContentView(R.layout.comment_dialog1);

                final RatingBar ratingBar = dialog.findViewById(R.id.rating);
                final EditText comment_text = dialog.findViewById(R.id.comment_text);
                Button send = dialog.findViewById(R.id.send);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RequestQueue requestQueue = Volley.newRequestQueue(details.this);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, insertRating, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if (response.equals("success")){
                                    Toast.makeText(details.this, "Sent", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(details.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> param = new HashMap<>();
                                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(details.this);
                                param.put("email", account.getEmail());
                                param.put("fest_name", fiesta);
                                param.put("rating", String.valueOf(ratingBar.getRating()));
                                param.put("comment", comment_text.getText().toString());
                                return param;
                            }
                        };
                        requestQueue.add(stringRequest);
                        //Toast.makeText(details.this, comment_text.getText().toString(), Toast.LENGTH_SHORT).show();

                    }
                });
                dialog.show();
            }
        });

        //displaying the total ratings
        displayUserRating(fiesta);


        //for events
        ListView listView = findViewById(R.id.eventList);
        idList = new ArrayList<>();
        dateList = new ArrayList<>();
        eventList = new ArrayList<>();
        sponsorList = new ArrayList<>();

        idList = getIntent().getStringArrayListExtra("id");
        dateList =getIntent().getStringArrayListExtra("event_date");
        eventList = getIntent().getStringArrayListExtra("event_name");
        sponsorList= getIntent().getStringArrayListExtra("event_sponsors");

        for (int i = 0; i <idList.size(); i++){
            if (getIntent().getStringExtra("fest_id").equals(idList.get(i))){

                eventNameHolder.add(eventList.get(i));
                dateHolder.add(dateList.get(i));
                sponsorHolder.add(sponsorList.get(i));
            }
        }


        EventAdapter eventAdapter = new EventAdapter();
        listView.setAdapter(eventAdapter);
        //for event


        //for user comment
        ListView commentListView = findViewById(R.id.commentsList);
        emailList = new ArrayList<>();
        commentList = new ArrayList<>();



        emailList =getIntent().getStringArrayListExtra("email");
        commentList = getIntent().getStringArrayListExtra("comment");


        for (int i = 0; i <emailList.size(); i++){
            if (getIntent().getStringExtra("fest_id").equals(idList.get(i))){

                emailHolder.add(emailList.get(i));
                commentHolder.add(commentList.get(i));

            }
        }


        CommentAdapter commentAdapter = new CommentAdapter();
        commentListView.setAdapter(commentAdapter);
        //for user comment


    }




    class EventAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return eventNameHolder.size();
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
            convertView = getLayoutInflater().inflate(R.layout.event_adapter, null);

            TextView event_name = convertView.findViewById(R.id.event_name);
            TextView event_date = convertView.findViewById(R.id.event_date);
            TextView event_sponsor = convertView.findViewById(R.id.event_sponsor);

            LinearLayout linearLayout = findViewById(R.id.linear);
            ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
            params.height = 120 * eventNameHolder.size();
            linearLayout.setLayoutParams(params);
            linearLayout.requestLayout();

            event_name.setText(eventNameHolder.get(position));
            event_date.setText(dateHolder.get(position));
            event_sponsor.setText("Sponsored by: "+ sponsorHolder.get(position));



            return convertView;
        }
    }

    class CommentAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return emailHolder.size();
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
            convertView = getLayoutInflater().inflate(R.layout.comments_adapter, null);

            TextView email = convertView.findViewById(R.id.comment_email);
            TextView comment = convertView.findViewById(R.id.comment_comment);


            LinearLayout linearLayout = findViewById(R.id.linear1);
            ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
            params.height = 70 * emailHolder.size();
            linearLayout.setLayoutParams(params);
            linearLayout.requestLayout();

            email.setText(emailHolder.get(position));
            comment.setText(commentHolder.get(position));

            return convertView;
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void displayUserRating(final String fiesta){
        final RatingBar ratingBar = findViewById(R.id.ratingBar);
        final TextView averageRating = findViewById(R.id.averageRating);
        final TextView sumRating = findViewById(R.id.sumRating);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("reviewed_festivals");
                    for (int i = 0 ; i < jsonArray.length(); i++)
                    {
                        JSONObject fiesta_info = jsonArray.getJSONObject(i);
                        String fname = fiesta_info.getString("fest_name");
                        String total_ratings = fiesta_info.getString("total_ratings");
                        String count_ratings = fiesta_info.getString("count_ratings");


                        if (fname.equals(fiesta)){
                            ratingBar.setRating((Float.parseFloat(total_ratings)/Float.parseFloat(count_ratings)));
                            averageRating.setText("("+(Double.parseDouble(total_ratings)/Double.parseDouble(count_ratings))+")");
                            sumRating.setText(count_ratings + " reviews");
                        }


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
