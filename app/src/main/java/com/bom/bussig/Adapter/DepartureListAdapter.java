package com.bom.bussig.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bom.bussig.R;
import com.mattiasbergstrom.resrobot.RouteSegment;

import java.util.ArrayList;

/**
 * Created by Oskar on 2014-09-06.
 */
public class DepartureListAdapter extends ArrayAdapter<RouteSegment>{

    private final Activity context;
    private final ArrayList<RouteSegment> itemsArrayList;

    public DepartureListAdapter(Activity context, ArrayList<RouteSegment> itemsArrayList) {
        super(context, R.layout.departure_list_row, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    static class ViewHolder {
        public TextView title;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.departure_list_row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.departureRowTextView);

        RouteSegment routeSegment = itemsArrayList.get(position);
        textView.setText(routeSegment.getDeparture().getLocation().getName());



        return rowView;
    }
}
