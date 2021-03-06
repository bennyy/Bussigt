package com.bom.bussig.Activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.bom.bussig.Adapter.DepartureListAdapter;
import com.bom.bussig.BussigApplication;
import com.bom.bussig.Helpers.AlarmManagerBroadcastReceiver;
import com.bom.bussig.Helpers.StationTranslator;
import com.bom.bussig.R;
import com.mattiasbergstrom.resrobot.ResrobotClient;
import com.mattiasbergstrom.resrobot.RouteSegment;

import java.util.ArrayList;

public class DepartureListActivity extends ListActivity implements SetAlarmDialogActivity.SetAlarmListener {
    DepartureListAdapter departureListAdapter;
    ResrobotClient resrobotClient;
    ArrayList<RouteSegment> currentDepartures;
    ArrayList<String> directions;
    private String mCurrentDirection;
    private int mBusNumber;
    private int mLocationID;
    private RouteSegment mRouteSegment;
    private AlarmManagerBroadcastReceiver alarm;
    private StationTranslator stationTranslator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departure_list);
        stationTranslator = new StationTranslator();
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final RouteSegment item = (RouteSegment) adapterView.getItemAtPosition(i);
                SetAlarmDialogActivity setAlarmDialog = new SetAlarmDialogActivity();
                Bundle bundle = new Bundle();
                bundle.putInt("Departure", (int) item.getDeparture().getMinutesToDeparture());
                setAlarmDialog.setArguments(bundle);
                setAlarmDialog.show(getFragmentManager(), "Test");
                return true;
            }
        });
        this.alarm = new AlarmManagerBroadcastReceiver();
        resrobotClient = new ResrobotClient(BussigApplication.getContext().getString(R.string.ResrobotAPIKey), BussigApplication.getContext().getString(R.string.ResrobotStolptidsAPIKey));
        Intent intent = getIntent();
        this.mRouteSegment = (RouteSegment)intent.getParcelableExtra(getString(R.string.ROUTE_SEGMENT));
        this.mLocationID = this.mRouteSegment.getDeparture().getLocation().getId();
        this.mBusNumber = this.mRouteSegment.getSegmentId().getCarrier().getNumber();
        this.mCurrentDirection = this.mRouteSegment.getDirection();
        setTitle(this.mRouteSegment.getSegmentId().getCarrier().getNumber() + " " + stationTranslator.translateStation(this.mCurrentDirection));
        directions = new ArrayList<String>();
        getDepartues(this.mLocationID, 120);

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
            case R.id.action_switch_direction:
                int directionIndex = directions.indexOf(mCurrentDirection);
                directionIndex++;
                mCurrentDirection = directions.get(directionIndex%directions.size());
                updateListView(filterDepartures(mCurrentDirection));
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
                currentDepartures = result;
                for (RouteSegment routeSegment : result) {
                    //add direction to directions list if not already exists
                    if (routeSegment.getSegmentId().getCarrier().getNumber() == mBusNumber
                            && !directions.contains(routeSegment.getDirection())){
                        directions.add(routeSegment.getDirection());
                    }
                }
                setListViewAdapter(filterDepartures(mCurrentDirection));
            }
        });

    }

    private ArrayList<RouteSegment> filterDepartures(String direction){
        setTitle(this.mRouteSegment.getSegmentId().getCarrier().getNumber() + " " + stationTranslator.translateStation(this.mCurrentDirection));
        ArrayList<RouteSegment> retain = new ArrayList<RouteSegment>(currentDepartures.size());
        for (RouteSegment routeSegment : currentDepartures) {
            //filter out bus number
            if (routeSegment.getSegmentId().getCarrier().getNumber() == mBusNumber
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

    @Override
    public void onSetAlarm(android.app.DialogFragment dialog) {

    }

    @Override
    public void onCancelSelectAlarm(android.app.DialogFragment dialog) {

    }
}
