package com.bom.bussig.Adapter;

import android.content.Context;
<<<<<<< HEAD
import android.location.Location;
=======
import android.util.Log;
>>>>>>> 2954f485b90b4ce265943333235a2647e14eeaca
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

<<<<<<< HEAD
import com.bom.bussig.Data.Location.LocationService;
import com.bom.bussig.Helpers.StationTranslator;
=======
import com.bom.bussig.BussigApplication;
>>>>>>> 2954f485b90b4ce265943333235a2647e14eeaca
import com.bom.bussig.Model.Departure;
import com.bom.bussig.Model.Station;
import com.bom.bussig.R;

import java.util.List;

/**
 * Created by Mackan on 2014-09-06.
 */
public class StationListAdapter extends ArrayAdapter<Station> {
    private final Context context;
    private final StationTranslator stationTranslator;
    private final List<Station> mStations;
    private LocationService locationService;
    private Location currentLocation;

    public StationListAdapter(Context context, List<Station> stations){
        super(context, R.layout.station_list_adapter, stations);
        this.context = context;
        this.stationTranslator = new StationTranslator();
        this.mStations = stations;
        locationService = new LocationService(context);
        currentLocation = locationService.getLocation();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.station_list_adapter, parent, false);
        Station station = mStations.get(position);

        Location stationLocation = new Location("gps");
        stationLocation.setLatitude(station.getLatitude());
        stationLocation.setLongitude(station.getLongitude());

        int distance = (int)Math.round(currentLocation.distanceTo(stationLocation));

        setTextOnTextView(view, R.id.station_name, station.getName() + " " + distance + "m");
        int departureNr = 0;

        Log.d("STA",Integer.toString(station.getDepartures().size()));
        if(station.getDepartures().size() > 0) {
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
            else {
                setTextOnTextView(view, R.id.BusDirection2, ""); // Clear if there is only one
            }
            departureNr++;
            if(station.getDepartures().size() > departureNr){
                Departure departure = station.getDepartures().get(departureNr);
                setTextOnTextView(view, R.id.BusNr3, Integer.toString(departure.getNumber()));
                setTextOnTextView(view, R.id.BusDirection3, stationTranslator.translateStation(departure.getmDirection()));
                setTextOnTextView(view, R.id.BusMinutes3, Long.toString(departure.getmMinutesToNextBus()));
            }
        }
        else {
            setTextOnTextView(view, R.id.BusDirection2, BussigApplication.getContext().getString(R.string.nothingOnTwoHours));
        }

        return view;
    }

    private void setTextOnTextView(View view, int textViewID, String text){
        TextView textView = (TextView) view.findViewById(textViewID);
        textView.setText(text);
    }

}
