package com.bom.bussig.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bom.bussig.Helpers.StationTranslator;
import com.bom.bussig.R;
import com.mattiasbergstrom.resrobot.RouteSegment;

import java.util.List;

/**
 * Created by Benjamin on 2014-09-06.
 */
public class LineListAdapter extends ArrayAdapter<RouteSegment> {

    private Context context;
    private StationTranslator stationTranslator;
    public LineListAdapter(Context context, int resource, List<RouteSegment> stopList) {
        super(context, resource, stopList);

        this.context = context;
        stationTranslator = new StationTranslator();
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

            int lineNumber = routeSegment.getSegmentId().getCarrier().getNumber();
            RelativeLayout relativeLayout = (RelativeLayout)view.findViewById(R.id.front);

            int color = R.color.abc_search_url_text_holo;
            int shadowColor = context.getResources().getColor(R.color.text_shadow);

            if(lineNumber == 1) {
                color = context.getResources().getColor(R.color.holoBlue);
                shadowColor = context.getResources().getColor(R.color.holodBlue);
            } else if(lineNumber == 2) {
                color = context.getResources().getColor(R.color.holoGreen);
                shadowColor = context.getResources().getColor(R.color.holodGreen);
            } else if(lineNumber == 3) {
                color = context.getResources().getColor(R.color.holoRed);
                shadowColor = context.getResources().getColor(R.color.holodRed);
            } else if(lineNumber == 4) {
                color = context.getResources().getColor(R.color.holoPurple);
                shadowColor = context.getResources().getColor(R.color.holoPurple);
            } else if(lineNumber == 10) {
                color = context.getResources().getColor(R.color.holoOrange);
                shadowColor = context.getResources().getColor(R.color.holodOrange);
            } else if(lineNumber == 11) {
                color = context.getResources().getColor(R.color.holoPurple);
            } else if(lineNumber == 12) {
                color = context.getResources().getColor(R.color.holodBlue);
                shadowColor = context.getResources().getColor(R.color.holoBlue);
            } else if(lineNumber == 13) {
                color = context.getResources().getColor(R.color.holoGreen);
            } else if(lineNumber == 14) {
                color = context.getResources().getColor(R.color.holoRed);
            } else if(lineNumber >= 15 && lineNumber <= 99) {
                color = context.getResources().getColor(R.color.holoOrange);
            } else if(lineNumber >= 100 && lineNumber <= 999) {
                color = context.getResources().getColor(R.color.holoGrey);
            } else {
                color = context.getResources().getColor(R.color.holodGrey);
            }

           relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.greygrey));
            txtLineNumber.setTextColor(color);//.setBackgroundColor(color);
            //txtLineNumber.setShadowLayer(txtLineNumber.getShadowRadius(),txtLineNumber.getShadowDx(), txtLineNumber.getShadowDy(), shadowColor );
            // Set line number
            txtLineNumber.setText(Integer.toString(routeSegment.getSegmentId().getCarrier().getNumber()));

            // Set direction, translate also
            txtDirection.setText(stationTranslator.translateStation(routeSegment.getDirection()));

            // Set time left (minutes)
            txtTimeLeft.setText(Long.toString(routeSegment.getDeparture().getMinutesToDeparture()));
        }

        return view;
    }
}
