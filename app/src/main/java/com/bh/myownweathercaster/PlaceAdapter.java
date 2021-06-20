package com.bh.myownweathercaster;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class PlaceAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<PlaceItem> data;
    private int layout;

    public PlaceAdapter(Context context, int layout, ArrayList<PlaceItem> data) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position).getPlace();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(layout, parent, false);
        }
        PlaceItem placeItem = data.get(position);

        TextView place = convertView.findViewById(R.id.tv_item_place);
        place.setText(placeItem.getPlace());

        TextView lat = convertView.findViewById(R.id.tv_item_lat);
        lat.setText(placeItem.getLat());

        TextView lon = convertView.findViewById(R.id.tv_item_lon);
        lon.setText(placeItem.getLon());

        return convertView;
    }
}
