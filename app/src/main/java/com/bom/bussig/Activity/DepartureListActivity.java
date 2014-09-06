package com.bom.bussig.Activity;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bom.bussig.Adapter.DepartureListAdapter;
import com.bom.bussig.R;
import com.mattiasbergstrom.resrobot.ResrobotClient;
import com.mattiasbergstrom.resrobot.RouteSegment;

import java.util.ArrayList;

public class DepartureListActivity extends ListActivity {
    DepartureListAdapter departureListAdapter;
    ResrobotClient resrobotClient;
    ArrayList<RouteSegment> currentDepartues;
    ArrayList<String> directions;
    String currentDirection = "Linköping Centralstation";
    int busNumber = 12;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departure_list);

        resrobotClient = new ResrobotClient("tAKhTKVqWF8OmVsJrJQqtlQzPQpBFTNr", "tAKhTKVqWF8OmVsJrJQqtlQzPQpBFTNr");
        directions = new ArrayList<String>();
        getDepartues(7456608, 120);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.departure_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_switch_direction:
                int directionIndex = directions.indexOf(currentDirection);
                directionIndex++;
                currentDirection = directions.get(directionIndex%directions.size());
                updateListView(filterDepartures(currentDirection));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void getDepartues(int locationID, int timeSpan)
    {
        resrobotClient.departures(locationID, timeSpan, new ResrobotClient.DeparturesCallback() {
            @Override
            public void departuresComplete(ArrayList<RouteSegment> result) {
                currentDepartues = result;
                for (RouteSegment routeSegment : result) {
                    //add direction to directions list if not already exists
                    if (routeSegment.getSegmentId().getCarrier().getNumber() == busNumber
                            && !directions.contains(routeSegment.getDirection())){
                        directions.add(routeSegment.getDirection());
                    }
                }
                setListViewAdapter(filterDepartures(currentDirection));
            }
        });

    }

    private ArrayList<RouteSegment> filterDepartures(String direction){
        ArrayList<RouteSegment> retain = new ArrayList<RouteSegment>(currentDepartues.size());
        for (RouteSegment routeSegment : currentDepartues) {
            //filter out bus number
            if (routeSegment.getSegmentId().getCarrier().getNumber() == busNumber
                    && routeSegment.getDirection().equals(direction)) {
                retain.add(routeSegment);
            }

        }
        return retain;
    }

    void setListViewAdapter(ArrayList<RouteSegment> departuresList){
        departureListAdapter = new DepartureListAdapter(getApplicationContext(), R.layout.activity_departure_list, departuresList);
        setListAdapter(departureListAdapter);
    }

    void updateListView(ArrayList<RouteSegment> departuresList) {
        departureListAdapter.clear();
        departureListAdapter.addAll(departuresList);
        departureListAdapter.notifyDataSetChanged();
    }


}
