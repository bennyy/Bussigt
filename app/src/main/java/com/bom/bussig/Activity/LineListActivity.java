package com.bom.bussig.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.RectF;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bom.bussig.Adapter.LineListAdapter;
import com.bom.bussig.Data.Location.LocationService;
import com.bom.bussig.Helpers.AlphaForegroundColorSpan;
import com.bom.bussig.Helpers.Coordinate;
import com.bom.bussig.Helpers.StaticMap;
import com.bom.bussig.R;
import com.mattiasbergstrom.resrobot.ResrobotClient;
import com.mattiasbergstrom.resrobot.RouteSegment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/*
Denna ska ju lista alla LINJER från EN hållplats
1    Skäggetorp    3
3    Ryd           5
12   Mjärdevi      12

om man swishar på en linje blir det typ (säg Skäggetorp)

1    Vidingsjö      12
3    Ryd           5
12   Mjärdevi      12


Yay!
 */
public class LineListActivity extends Activity {

    private ListView lineListView;
    private View placeHolderView;
    private View header;
    private ImageView headerLogo;
    private int mHeaderHeight;
    private int mMinHeaderTranslation;
    private int mActionBarHeight;
    private AlphaForegroundColorSpan alphaForegroundColorSpan;
    private SpannableString mSpannableString;
    private int mLocationID;
    private LocationService locationService;

    private TypedValue mTypedValue = new TypedValue();
    private int actionBarTitleColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationService = new LocationService(this);

        mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
        mMinHeaderTranslation = -mHeaderHeight + getActionBarHeight();
        //Eeeh...?
        //setContentView(R.layout.activity_noboringactionbar);
        Intent intent = getIntent();
        this.mLocationID = intent.getIntExtra(getString(R.string.LOCATION_ID),0);
        setContentView(R.layout.line_list_fancy_header);


        lineListView = (ListView) findViewById(R.id.listview);
        header = findViewById(R.id.header);
        //headerLogo = (ImageView) findViewById(R.id.header_logo);

        //View mFakeHeader = getLayoutInflater().inflate(R.layout.line_list_fancy_fake_header, mListView, false);
        //mListView.addHeaderView(mFakeHeader);

        actionBarTitleColor = getResources().getColor(R.color.actionbar_title_color);

        mSpannableString = new SpannableString(getString(R.string.dontbreak));
        alphaForegroundColorSpan = new AlphaForegroundColorSpan(actionBarTitleColor);

        setupActionBar();
        setupLineList();


        setHeaderImage();



    }

    private void setupLineList() {
        placeHolderView = getLayoutInflater().inflate(R.layout.line_list_fancy_fake_header, lineListView, false);
        lineListView.addHeaderView(placeHolderView);


        // TODO: Felhantera

        ResrobotClient client = new ResrobotClient("tAKhTKVqWF8OmVsJrJQqtlQzPQpBFTNr", "tAKhTKVqWF8OmVsJrJQqtlQzPQpBFTNr");

        client.departures(this.mLocationID, 120, new ResrobotClient.DeparturesCallback() {
            @Override
            public void departuresComplete(ArrayList<RouteSegment> result) {
                Log.d("LineListAct", "Hamtade data bra och najs o så!");
                LineListAdapter lineListAdapter = new LineListAdapter(getApplicationContext(), R.layout.activity_line_list, result);
                //setListAdapter(lineListAdapter);
                lineListView.setAdapter(lineListAdapter);
            }
        });

        lineListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int scrollY = getScrollY();

                // kladdig actionbar
                header.setTranslationY(Math.max(-scrollY, mMinHeaderTranslation));

                float ratio = clamp(header.getTranslationY() / mMinHeaderTranslation, 0.0f, 1.0f);
                //actionbar title alpha
                setTitleAlpha(clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F));

            }
        }

        );


    }

    private void setHeaderImage() {
        ImageView headerImageView = (ImageView)findViewById(R.id.header_picture);
        headerImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        android.location.Location myLocation = locationService.getLocation();
        StaticMap headerMap = new StaticMap(myLocation, new Coordinate(15.569680,58.394281));
        Picasso.with(this).load(headerMap.getUrl()).into(headerImageView);
    }

    private void setTitleAlpha(float alpha) {
        alphaForegroundColorSpan.setAlpha(alpha);
        mSpannableString.setSpan(alphaForegroundColorSpan, 0, mSpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getActionBar().setTitle(mSpannableString);
    }

    private float clamp(float value, float max, float min) {
        return Math.max(Math.min(value, min), max);
    }

    private void setupActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(R.drawable.logo1);

        //getActionBarTitleView().setAlpha(0f);
    }

    private int getScrollY() {
        View c = lineListView.getChildAt(0);
        if (c == null) {
            return 0;
        }

        int firstVisiblePosition = lineListView.getFirstVisiblePosition();
        int top = c.getTop();

        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            headerHeight = lineListView.getHeight();
        }

        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
    }

    public int getActionBarHeight() {
        if (mActionBarHeight != 0) {
            return mActionBarHeight;
        }
        getTheme().resolveAttribute(android.R.attr.actionBarSize, mTypedValue, true);
        mActionBarHeight = TypedValue.complexToDimensionPixelSize(mTypedValue.data, getResources().getDisplayMetrics());
        return mActionBarHeight;
    }

    private TextView getActionBarTitleView() {
        int id = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
        return (TextView) findViewById(id);
    }

    private RectF getOnScreenRect(RectF rect, View view) {
        rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        return rect;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.line_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
