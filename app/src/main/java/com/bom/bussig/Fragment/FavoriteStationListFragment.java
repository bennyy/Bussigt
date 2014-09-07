package com.bom.bussig.Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bom.bussig.Activity.LineListActivity;
import com.bom.bussig.Activity.StationListActivity;
import com.bom.bussig.Adapter.StationListAdapter;
import com.bom.bussig.BussigApplication;
import com.bom.bussig.Data.Location.BussigDAL;
import com.bom.bussig.Data.Location.LocationService;
import com.bom.bussig.Model.Departure;
import com.bom.bussig.Model.Station;
import com.bom.bussig.R;
import com.mattiasbergstrom.resrobot.ResrobotClient;
import com.mattiasbergstrom.resrobot.RouteSegment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mackan on 2014-09-06.
 */
public class FavoriteStationListFragment extends Fragment {
    private ResrobotClient resrobotClient;
    private LocationService mLocationService;
    private ResrobotClient mResrobotClient;
    private String provider;
    private List<Station> mStations;
    private StationListAdapter mListAdapter;
    private OnFragmentInteractionListener mListener;

    public static ClosestStationListFragment newInstance(String param1, String param2) {
        ClosestStationListFragment fragment = new ClosestStationListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public FavoriteStationListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        this.mStations = (new BussigDAL(((StationListActivity)getActivity()).getContext()).getAllFavorites());
        this.mLocationService = new LocationService(BussigApplication.getContext());
        this.mResrobotClient = new ResrobotClient(getString(R.string.ResrobotAPIKey), getString(R.string.ResrobotStolptidsAPIKey));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //This might be needed in the future
        /*try {
            android.location.Location location = this.mLocationService.getLocation();

            if (location != null) {
                Log.d("location", "fick location");

                //this.loadClosestStations(location.getLongitude(), location.getLatitude());

            } else {
                Log.d("location", "fick inte location");
            }
        }
        catch (Exception e) {
            Log.d("Location failade", e.toString());
        }*/
        View view = inflater.inflate(R.layout.fragment_closest_station_list, container, false);

        ListView lv = (ListView)view.findViewById(R.id.closest_station_list_view);
        mListAdapter = new StationListAdapter(getContext(), mStations);
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

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Station station = (Station) adapterView.getItemAtPosition(i);

                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.add_to_favorite)
                        .setMessage(R.string.add_to_favorite)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeFromFavorites(station);
                            }
                        })
                        .setNegativeButton(R.string.no, null)
                        .show();
                return true;
            }
        });
        //Load departures for each station
        for(int i = 0; i < mStations.size(); i++){
            loadDeparturesFromStation(mStations.get(i), mListAdapter );
        };
        mListAdapter.notifyDataSetChanged();

        return view;
    }

    private void removeFromFavorites(Station station){

    }

    public void loadDeparturesFromStation(final Station station, final StationListAdapter listAdapter){
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

    private Context getContext(){
        return ((StationListActivity)getActivity()).getContext();
    }
}
