package com.bom.bussig.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bom.bussig.Helpers.StationTranslator;
import com.bom.bussig.R;
import com.mattiasbergstrom.resrobot.RouteSegment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Oskar on 2014-09-06.
 */
public class DepartureListAdapter extends ArrayAdapter<RouteSegment> {

    private Context context;
    private StationTranslator stationTranslator;
    public DepartureListAdapter(Context context, int resource, List<RouteSegment> departureList) {
        super(context, resource, departureList);

        this.context = context;
        this.stationTranslator = new StationTranslator();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            LayoutInflater vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.departure_list_row, null);
        }
        RouteSegment routeSegment = getItem(position);

        if(routeSegment != null) {
            Date leaveDate = routeSegment.getDeparture().getDateTime();
            Date timeNow = new Date();


            //print bus number
            TextView busNumberText = (TextView) view.findViewById(R.id.bus_number);
            busNumberText.setText(Integer.toString(routeSegment.getSegmentId().getCarrier().getNumber()));

            //print avgångstid
            TextView leavingText = (TextView) view.findViewById(R.id.leaving);
            SimpleDateFormat leaveDateFormat = new SimpleDateFormat("HH:mm");
            leavingText.setText(leaveDateFormat.format(leaveDate));

            //printa tid kvar i minuter
            TextView timeLeftText = (TextView) view.findViewById(R.id.timeLeft);
            long timeleft = ((leaveDate.getTime()/60000) - (timeNow.getTime()/60000));
            timeLeftText.setText(Integer.toString((int)timeleft) + " minuter");

            //printa direction
            TextView directionText = (TextView) view.findViewById(R.id.direction);
            directionText.setText(stationTranslator.translateStation(routeSegment.getDirection()));

        }

        return view;
    }
}
