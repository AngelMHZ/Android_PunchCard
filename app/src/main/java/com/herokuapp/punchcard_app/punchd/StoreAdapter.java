package com.herokuapp.punchcard_app.punchd;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StoreAdapter extends ArrayAdapter<Stores> {
    ArrayList<Stores> storeList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;

    public StoreAdapter(Context context, int resource, ArrayList<Stores> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        storeList = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // convert view = design
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);
            //holder.imageview = (ImageView) v.findViewById(R.id.ivImage);
            holder.storeName = (TextView) v.findViewById(R.id.storeName);
            holder.storeAddress = (TextView) v.findViewById(R.id.storeAddress);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.storeName.setText("  " + storeList.get(position).getName());
        holder.storeAddress.setText("  " + storeList.get(position).getAddress());
        return v;

    }

    static class ViewHolder {
        public TextView storeName;
        public TextView storeAddress;
    }

}