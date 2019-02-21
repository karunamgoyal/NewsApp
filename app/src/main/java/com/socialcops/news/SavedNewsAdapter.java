package com.socialcops.news;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;


class SavedNewsAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;

    public SavedNewsAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
    }
    public int getCount() {
        return data.size();
    }
    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) {
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        SavedNewsViewHolder holder = null;
        if (convertView == null) {
            holder = new SavedNewsViewHolder();
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.saved_item, parent, false);

            holder.title = (TextView) convertView.findViewById(R.id.headline);
            //  holder.source = (TextView) convertView.findViewById(R.id.newssource);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        } else {
            holder = (SavedNewsViewHolder) convertView.getTag();
        }

        holder.title.setId(position);

        holder.time.setId(position);
        // holder.source.setId(position);
        HashMap<String, String> song;
        song = data.get(position);

        try{
            String arr[]=song.get(NewsFragment.KEY_PUBLISHEDAT).split("T");
            holder.title.setText(song.get(NewsFragment.KEY_TITLE));
            holder.time.setText(arr[0]);
        }catch(Exception e) {
            System.out.println("Hello   "+song.get(NewsFragment.KEY_URLTOIMAGE));
        }
        return convertView;
    }
}

class SavedNewsViewHolder {
    TextView  title, time,source;

}