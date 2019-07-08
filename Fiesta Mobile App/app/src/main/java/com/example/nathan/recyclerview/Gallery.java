package com.example.nathan.recyclerview;

        import android.content.Context;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.BaseAdapter;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.ListView;
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
        import com.squareup.picasso.Target;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.Map;
public class Gallery extends AppCompatActivity {

    ArrayList<String> imageList = new ArrayList<>();
    ImageView imageView;
    public static final String url = "http://192.168.8.101/region1-tf/fiesta_images.php";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("fiesta_images");
                    for (int i = 0 ; i < jsonArray.length(); i++)
                    {
                        JSONObject fiesta_info = jsonArray.getJSONObject(i);
                        String image = fiesta_info.getString("images");

                        imageList.add(image);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Gallery.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);

        ListView listView =findViewById(R.id.imageListView);
        ImageAdapter imageAdapter = new ImageAdapter();
        listView.setAdapter(imageAdapter);




    }

    class ImageAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return imageList.size();
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
            convertView = getLayoutInflater().inflate(R.layout.image_adapter, null);
            ImageView imageView = convertView.findViewById(R.id.loadImages);
            Toast.makeText(Gallery.this, imageList.get(position), Toast.LENGTH_SHORT).show();
            Picasso.with(Gallery.this).load(imageList.get(position)).fit().centerInside().into(imageView);

            return convertView;

        }
    }
}
