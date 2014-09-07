package com.bom.bussig.Adapter;


import android.app.Fragment;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

import com.bom.bussig.Fragment.ClosestStationListFragment;
import com.bom.bussig.Fragment.FavoriteStationListFragment;
import com.bom.bussig.R;

import java.util.Locale;


/**
 * Created by Mackan on 2014-09-06.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    public SectionsPagerAdapter(Context context, android.app.FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        switch (position) {
            case 0:
                // Top Rated fragment activity
                return new ClosestStationListFragment();
            case 1:
                // Games fragment activity
                return new FavoriteStationListFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return mContext.getString(R.string.closest).toUpperCase(l);
            case 1:
                return mContext.getString(R.string.favorites).toUpperCase(l);
        }
        return null;
    }
}
