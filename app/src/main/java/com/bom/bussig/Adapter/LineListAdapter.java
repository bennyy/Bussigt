package com.bom.bussig.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.mattiasbergstrom.resrobot.RouteSegment;

import java.util.List;

/**
 * Created by Benjamin on 2014-09-06.
 */
public class LineListAdapter extends ArrayAdapter<RouteSegment> {

    private Context context;
    public LineListAdapter(Context context, int resource, List<RouteSegment> stopList) {
        super(context, resource, stopList);

        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
