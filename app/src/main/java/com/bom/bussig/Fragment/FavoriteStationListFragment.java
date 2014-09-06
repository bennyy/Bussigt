package com.bom.bussig.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bom.bussig.BussigApplication;
import com.bom.bussig.Data.Location.LocationService;
import com.bom.bussig.R;
import com.mattiasbergstrom.resrobot.ResrobotClient;

/**
 * Created by Mackan on 2014-09-06.
 */
public class FavoriteStationListFragment extends Fragment {
    private ResrobotClient resrobotClient;
    private LocationService mLocationService;
    private ResrobotClient mResrobotClient;
    private String provider;


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
        this.mLocationService = new LocationService(BussigApplication.getContext());
        this.mResrobotClient = new ResrobotClient(BussigApplication.getContext().getString(R.string.ResrobotAPIKey), BussigApplication.getContext().getString(R.string.ResrobotStolptidsAPIKey));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        try {
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
        }

        return inflater.inflate(R.layout.fragment_closest_station_list, container, false);
    }
}
