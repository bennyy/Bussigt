package com.bom.bussig.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bom.bussig.R;
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
        View view = convertView;

        if(view == null) {
            LayoutInflater vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.line_list_item, null);
        }
        RouteSegment routeSegment = getItem(position);

        if(routeSegment != null) {
            TextView txtLineNumber = (TextView) view.findViewById(R.id.lineNumber);
            TextView txtDirection = (TextView) view.findViewById(R.id.direction);
            TextView txtTimeLeft = (TextView) view.findViewById(R.id.timeLeft);

            // Set line number
            txtLineNumber.setText(Integer.toString(routeSegment.getSegmentId().getCarrier().getNumber()));

            // Set direction
            txtDirection.setText(routeSegment.getDirection());

            // Set time left (minutes)
            txtTimeLeft.setText(Long.toString(routeSegment.getDeparture().getMinutesToDeparture()));
        }

        return view;
    }
}
