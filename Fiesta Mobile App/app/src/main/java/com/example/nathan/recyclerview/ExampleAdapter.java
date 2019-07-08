package com.example.nathan.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.nathan.recyclerview.MainActivity.EXTRA_FIESTA;
import static com.example.nathan.recyclerview.MainActivity.EXTRA_HISTORY;
import static com.example.nathan.recyclerview.MainActivity.EXTRA_ID;
import static com.example.nathan.recyclerview.MainActivity.EXTRA_LATITUDE;
import static com.example.nathan.recyclerview.MainActivity.EXTRA_LONGITUDE;
import static com.example.nathan.recyclerview.MainActivity.EXTRA_MUNICIPAL;
import static com.example.nathan.recyclerview.MainActivity.EXTRA_PROVINCE;
import static com.example.nathan.recyclerview.MainActivity.EXTRA_URL;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private Context Mcontext;
    private ArrayList<ExampleItem> ExampleList;
    private OnItemClickListener mListener;
    public interface OnItemClickListener
    {
        void onItemClick (int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener = listener;

    }

    //sample lang
    private ExampleItem currentPosition;


    public ExampleAdapter (Context context, ArrayList<ExampleItem> exampleList)
    {
        Mcontext = context;
        ExampleList = exampleList;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(Mcontext).inflate(R.layout.example_item, parent, false);

        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int i) {
        ExampleItem currentItem = ExampleList.get(i);
        String image = currentItem.getImage();
        String Fname =currentItem.getTest_name();
        String province = currentItem.getProvince();
        String Fmunicipal = currentItem.getMunicipal();
        String Fhistory =currentItem.getHistory();

        holder.mMunicipal.setText(Fmunicipal);
        holder.mFestname.setText(Fname);
        holder.mProvince.setText(province);
        holder.mHistory.setText(Fhistory);
        Picasso.with(Mcontext).load(image).fit().centerInside().into(holder.mImageView);

        //sample lang
        currentPosition = currentItem;

    }

    @Override
    public int getItemCount() {
        return ExampleList.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder
    {


        public ImageView mImageView;
        public TextView mFestname;
        public TextView mProvince;
        public TextView mMunicipal;
        public TextView mHistory;


        public ExampleViewHolder(@NonNull final View itemView) {
            super(itemView);

            mMunicipal = itemView.findViewById(R.id.text_view_municipal);
            mImageView = itemView.findViewById(R.id.image_view);
            mFestname = itemView.findViewById(R.id.text_view_name);
            mProvince = itemView.findViewById(R.id.text_view_provice);
            mHistory =itemView.findViewById(R.id.text_view_history);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    if (mListener!=null)
                    {
                        int position = getLayoutPosition(); //getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {
                            mListener.onItemClick(position);
                            Intent detailIntent = new Intent(Mcontext, details.class);
                            ExampleItem clickItem = ExampleList.get(position);
                            detailIntent.putExtra(EXTRA_FIESTA, clickItem.getTest_name());
                            detailIntent.putExtra(EXTRA_PROVINCE, clickItem.getProvince());
                            detailIntent.putExtra(EXTRA_URL,clickItem.getImage());
                            detailIntent.putExtra(EXTRA_MUNICIPAL,clickItem.getMunicipal());
                            detailIntent.putExtra(EXTRA_HISTORY,clickItem.getHistory());
                            detailIntent.putExtra(EXTRA_LATITUDE, clickItem.getLatitude());
                            detailIntent.putExtra(EXTRA_LONGITUDE, clickItem.getLongitude());
                            detailIntent.putExtra(EXTRA_ID, clickItem.getFid());
                            //for events
                            detailIntent.putStringArrayListExtra("id", MainActivity.idList);
                            detailIntent.putStringArrayListExtra("event_date", MainActivity.dateList);
                            detailIntent.putStringArrayListExtra("event_name", MainActivity.eventList);
                            detailIntent.putStringArrayListExtra("event_sponsors", MainActivity.sponsorList);
                            //for events
                            //for comments
                            detailIntent.putStringArrayListExtra("email", MainActivity.emailList);
                            detailIntent.putStringArrayListExtra("comment", MainActivity.commentList);
                            //for comments
                            Mcontext.startActivity(detailIntent);

                        }
                    }
                }

            });


        }
    }

    public void setSearchOperation (ArrayList<ExampleItem> newList){
        ExampleList = new ArrayList<>();
        ExampleList.addAll(newList);
        notifyDataSetChanged();


    }

}
