package com.bom.bussig.Fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bom.bussig.Activity.LineListActivity;
import com.bom.bussig.Adapter.StationListAdapter;
import com.bom.bussig.BussigApplication;
import com.bom.bussig.Data.Location.LocationService;
import com.bom.bussig.Model.Departure;
import com.bom.bussig.Model.Station;
import com.bom.bussig.R;
import com.mattiasbergstrom.resrobot.Location;
import com.mattiasbergstrom.resrobot.ResrobotClient;
import com.mattiasbergstrom.resrobot.RouteSegment;

import java.util.ArrayList;

/**
 * Created by Mackan on 2014-09-06.
 */
public class ClosestStationListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public String BUSS_ID = "BussID";

    private ResrobotClient resrobotClient;
    private LocationService mLocationService;
    private ResrobotClient mResrobotClient;
    private SwipeRefreshLayout mSwipeLayout;
    private String provider;
    private View mView;
    private ArrayList<Station> mStations;
    private OnFragmentInteractionListener mListener;
    private StationListAdapter mListAdapter;
    public static ClosestStationListFragment newInstance(String param1, String param2) {
        ClosestStationListFragment fragment = new ClosestStationListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public ClosestStationListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        this.mLocationService = new LocationService(BussigApplication.getContext());
        this.mResrobotClient = new ResrobotClient(BussigApplication.getContext().getString(R.string.ResrobotAPIKey), BussigApplication.getContext().getString(R.string.ResrobotStolptidsAPIKey));
        mStations = new ArrayList<Station>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        this.mView = inflater.inflate(R.layout.fragment_closest_station_list, container, false);
        this.mSwipeLayout = (SwipeRefreshLayout) this.mView.findViewById(R.id.swipe_container);
        this.mSwipeLayout.setOnRefreshListener(this);
        this.mSwipeLayout.setColorScheme(android.R.color.darker_gray, android.R.color.black,android.R.color.darker_gray,android.R.color.black);

        loadClosestStations(getLocation().getLongitude(), getLocation().getLatitude());
        //loadClosestStations(15.623509, 58.414957);

        return this.mView;
    }

    public android.location.Location getLocation(){
        return this.mLocationService.getLocation();
    }

    public void loadClosestStations(double longitude, double latitude){

        try {
            this.mResrobotClient.stationsInZone(longitude, latitude, 2000, new ResrobotClient.StationsInZoneCallback() {
                @Override
                public void stationsInZoneComplete(ArrayList<Location> locations) {
                    //Convert the result to our DataModel;
                    for(int i = 0; i < locations.size() && i < BussigApplication.getContext().getResources().getInteger(R.integer.station_list_max); i++){

                        mStations.add(new Station(locations.get(i).getId(), locations.get(i).getName(), locations.get(i).getLongitude(), locations.get(i).getLatitude()));
                    }

                    ListView lv = (ListView)getView().findViewById(R.id.closest_station_list_view);
                    mListAdapter = new StationListAdapter(BussigApplication.getContext(), mStations);
                    lv.setAdapter(mListAdapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                        @Override
                        public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                            final Station station = (Station) parent.getItemAtPosition(position);
                            Intent intent = new Intent(BussigApplication.getContext(), LineListActivity.class);

                            intent.putExtra(BussigApplication.getContext().getString(R.string.STATION), station);

                            startActivity(intent);
                        }
                    });


                    //Load departures for each station
                    for(int i = 0; i < mStations.size(); i++){
                        loadDeparturesFromStation(mStations.get(i), mListAdapter );
                    };
                    mListAdapter.notifyDataSetChanged();
                }
            });


        }
        catch (Exception e){

            Log.d("resrobot", e.toString());
        }
    }

    public void updateClosestStations(){
        try {
            this.mResrobotClient.stationsInZone(getLocation().getLongitude(), getLocation().getLatitude(), 2000, new ResrobotClient.StationsInZoneCallback() {
                @Override
                public void stationsInZoneComplete(ArrayList<Location> locations) {
                    //Convert the result to our DataModel;
                    for(int i = 0; i < locations.size() && i < BussigApplication.getContext().getResources().getInteger(R.integer.station_list_max); i++){

                        mStations.add(new Station(locations.get(i).getId(), locations.get(i).getName(), locations.get(i).getLongitude(), locations.get(i).getLatitude()));
                    }

                    //Load departures for each station
                    for(int i = 0; i < mStations.size(); i++){
                        loadDeparturesFromStation(mStations.get(i), mListAdapter );
                    };
                    mListAdapter.notifyDataSetChanged();
                }
            });


        }
        catch (Exception e){

            Log.d("resrobot", e.toString());
        }
    }

    public void loadDeparturesFromStation(final Station station, final StationListAdapter  listAdapter){
        mResrobotClient.departures(station.getLocationID(), 120, new ResrobotClient.DeparturesCallback() {
            @Override
            public void departuresComplete(ArrayList<RouteSegment> result) {
                for (RouteSegment route : result) {
                    Departure departure = new Departure(
                            route.getSegmentId().getCarrier().getNumber(),
                            route.getDirection(),
                            route.getDeparture().getMinutesToDeparture()
                    );
                    station.addDeparture(departure);
                }
                listAdapter.notifyDataSetChanged();
            }
        });
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        if (mStations != null)
            mStations.clear();
        if (mListAdapter != null)
            mListAdapter.notifyDataSetChanged();
        this.updateClosestStations();
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                mSwipeLayout.setRefreshing(false);
            }
        }, 1000);
    }


}