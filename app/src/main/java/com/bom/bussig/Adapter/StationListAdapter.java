package com.bom.bussig.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bom.bussig.Helpers.StationTranslator;
import com.bom.bussig.Model.Departure;
import com.bom.bussig.Model.Station;
import com.bom.bussig.R;

import java.util.ArrayList;

/**
 * Created by Mackan on 2014-09-06.
 */
public class StationListAdapter extends ArrayAdapter<Station> {
    private final Context context;
    private final ArrayList<Station> mStations;
    private final StationTranslator stationTranslator;

    public StationListAdapter(Context context, ArrayList<Station> stations){
        super(context, R.layout.station_list_adapter, stations);
        this.context = context;
        this.stationTranslator = new StationTranslator();
        this.mStations = stations;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.station_list_adapter, parent, false);
        Station station = mStations.get(position);

        setTextOnTextView(view, R.id.station_name, station.getName());
        int departureNr = 0;
        if(station.getDepartures().size() > departureNr){
            Departure departure = station.getDepartures().get(departureNr);
            setTextOnTextView(view, R.id.BusNr1, Integer.toString(departure.getNumber()));
            setTextOnTextView(view, R.id.BusDirection1, stationTranslator.translateStation(departure.getmDirection()));
            setTextOnTextView(view, R.id.BusMinutes1, Long.toString(departure.getmMinutesToNextBus()));
        }
        departureNr++;
        if(station.getDepartures().size() > departureNr){
            Departure departure = station.getDepartures().get(departureNr);
            setTextOnTextView(view, R.id.BusNr2, Integer.toString(departure.getNumber()));
            setTextOnTextView(view, R.id.BusDirection2, stationTranslator.translateStation(departure.getmDirection()));
            setTextOnTextView(view, R.id.BusMinutes2, Long.toString(departure.getmMinutesToNextBus()));
        }
        departureNr++;
        if(station.getDepartures().size() > departureNr){
            Departure departure = station.getDepartures().get(departureNr);
            setTextOnTextView(view, R.id.BusNr3, Integer.toString(departure.getNumber()));
            setTextOnTextView(view, R.id.BusDirection3, stationTranslator.translateStation(departure.getmDirection()));
            setTextOnTextView(view, R.id.BusMinutes3, Long.toString(departure.getmMinutesToNextBus()));
        }
        return view;
    }

    private void setTextOnTextView(View view, int textViewID, String text){
        TextView textView = (TextView) view.findViewById(textViewID);
        textView.setText(text);
    }

}
