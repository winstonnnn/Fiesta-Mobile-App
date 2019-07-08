package com.example.nathan.recyclerview;

import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Calendar extends AppCompatActivity {

    List<EventDay> events;
    java.util.Calendar calendar;
    ArrayList<java.util.Calendar> calendars;
    ArrayList<java.util.Calendar> ca;

    CalendarView calendarView;
    public static final String url3 = "http://192.168.8.105/region1-tf/fiesta_event.php";
    public static final String url1 = "http://192.168.8.105/region1-tf/fiesta_details.php";

    //for events
    ListView listView;
    ArrayList<String> idList;
    ArrayList<String> eventList;
    ArrayList<String> dateList;
    ArrayList<String> sponsorList;
    ArrayList<String> festName;
    //holder for events
    ArrayList<String> eventNameHolder = new ArrayList<>();
    String festIdHolder;
    ArrayList<String> dateHolder = new ArrayList<>();
    ArrayList<String> sponsorHolder = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadDataToCalendar();



        //for events
        listView = findViewById(R.id.eventList);
        idList = new ArrayList<>();
        dateList = new ArrayList<>();
        eventList = new ArrayList<>();
        sponsorList = new ArrayList<>();


        idList = getIntent().getStringArrayListExtra("id");
        dateList =getIntent().getStringArrayListExtra("event_date");
        eventList = getIntent().getStringArrayListExtra("event_name");
        sponsorList= getIntent().getStringArrayListExtra("event_sponsors");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void loadDataToCalendar(){

        events = new ArrayList<>();
        calendar = java.util.Calendar.getInstance();
        calendars = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url3, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("events");
                    for (int i = 0 ; i < jsonArray.length(); i++)
                    {
                        JSONObject fiesta_info = jsonArray.getJSONObject(i);
                        String event_date = fiesta_info.getString("event_date");


                        SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
                        SimpleDateFormat sdf1 = new SimpleDateFormat("MM");

                        String[] nameList = event_date.split(" ");
                        String month = nameList[0];
                        int day = Integer.parseInt(nameList[1]);
                        int year = Integer.parseInt(nameList[2]);
                        Log.d("Calendar", "caledar: "+ String.valueOf(sdf1.format(sdf.parse(month))) + " " + day + " " + year);


                        calendar.set(year, Integer.parseInt(sdf1.format(sdf.parse(month)))-1, day);
                        calendars.add(calendar);
                        calendar = (java.util.Calendar) calendar.clone();
                    }


                } catch (JSONException e) {
                    Toast.makeText(Calendar.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    Toast.makeText(Calendar.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);



        calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {

                for (int a = 0; a < calendars.size(); a++) {
                    events.add(new EventDay(calendars.get(a), R.drawable.calendar_notif));
                }
                calendarView.setEvents(events);


                SimpleDateFormat sdf = new SimpleDateFormat("MMMM d yyyy");
                Date d = eventDay.getCalendar().getTime();
                if (eventNameHolder != null){
                    eventNameHolder.clear();
                    dateHolder.clear();
                    sponsorHolder.clear();
                }
                TextView events = findViewById(R.id.events);
                events.setText("No Events on this Day");
                for (int i = 0; i < eventList.size(); i++){
                    Log.d("TAG", "onDayClick: " + sdf.format(d) + " " +dateList.get(i));
                    if (sdf.format(d).equals(dateList.get(i))){
                        events.setText("List of Events");
                        eventNameHolder.add(eventList.get(i));
                        dateHolder.add(dateList.get(i));
                        sponsorHolder.add(sponsorList.get(i));
                        festIdHolder = idList.get(i);
                    }
                }


                EventAdapter eventAdapter = new EventAdapter();
                listView.setAdapter(eventAdapter);

                final TextView fest_name = findViewById(R.id.fiesta);
                //for setting the fiesta
                RequestQueue requestQueue = Volley.newRequestQueue(Calendar.this);
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

                                if (fid.equals(festIdHolder)){
                                    fest_name.setText(fname);
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Calendar.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                requestQueue.add(request);

            }
        });

        try {
            calendarView.setDate(calendar);
        } catch (OutOfDateRangeException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    class EventAdapter extends BaseAdapter {

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
        public View getView(final int position, View convertView, ViewGroup parent) {
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
}
