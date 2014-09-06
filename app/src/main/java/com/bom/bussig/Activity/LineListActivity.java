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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bom.bussig.Adapter.LineListAdapter;
import com.bom.bussig.Helpers.AlphaForegroundColorSpan;
import com.bom.bussig.Helpers.Coordinate;
import com.bom.bussig.Helpers.StaticMap;
import com.bom.bussig.R;
import com.mattiasbergstrom.resrobot.ResrobotClient;
import com.mattiasbergstrom.resrobot.RouteSegment;
import com.squareup.picasso.Picasso;

import org.apache.http.impl.client.RoutedRequest;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

    private TypedValue mTypedValue = new TypedValue();
    private int actionBarTitleColor;

    private HashMap<Integer, ArrayList<RouteSegment>> groupedView = new HashMap<Integer, ArrayList<RouteSegment>>();
    ArrayList<RouteSegment> heraderp = new ArrayList<RouteSegment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                takeEverythingAndDoItMoreBetterThanShit(result);



                // Woah!
                Iterator it = groupedView.entrySet().iterator();
                while(it.hasNext()) {
                    Map.Entry item = (Map.Entry)it.next();
                    heraderp.add( ((ArrayList<RouteSegment>) item.getValue()).get(0));
                    //it.remove();
                }

                //setListAdapter(lineListAdapter);
                LineListAdapter lla = new LineListAdapter(getApplicationContext(), R.layout.activity_line_list, heraderp);
                lineListView.setAdapter(lla);
            }
        });

        lineListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                RouteSegment rs = (RouteSegment)lineListView.getItemAtPosition(position);
                Log.d("Bus", rs.getDirection());

                // Ta in linjenumret här och do magic!
                int line = rs.getSegmentId().getCarrier().getNumber();
                String direction = rs.getDirection();

                int changedIndex = 0;
                int arrayIndex = 0;

                int numberOfLines = groupedView.get(line).size();
                if(numberOfLines > 1) {
                    for(int i = 0; i < numberOfLines; i++) {
                        RouteSegment routeSegment = groupedView.get(line).get(i);
                        if(routeSegment.getDirection().equals(direction)) {

                            if(i == numberOfLines-1) {
                                // om det slår "över"
                                changedIndex = 0;
                            }
                            else {
                                // Annars är de ju bara ta nästa
                                changedIndex = ++i;
                            }
                            arrayIndex = i;
                            break;
                        }
                    }
                }
                // Hitta vilket index skiten ligger på.. detta börjar bli sjukt invecklat :(
                int thisToBeChanged = 0;
                for(int i = 0; i < heraderp.size(); i++) {
                    if(line == heraderp.get(i).getSegmentId().getCarrier().getNumber()) {
                        thisToBeChanged = i;
                        break;
                    }
                }

                Iterator it = groupedView.entrySet().iterator();
                while(it.hasNext()) {
                    Map.Entry item = (Map.Entry)it.next();
                    if(line == (Integer)item.getKey()) {
                        heraderp.set(thisToBeChanged, ((ArrayList<RouteSegment>) item.getValue()).get(changedIndex));
                    }
                    //it.remove();
                }

                LineListAdapter lla = new LineListAdapter(getApplicationContext(), R.layout.activity_line_list, heraderp);
                lineListView.setAdapter(lla);
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
    private ArrayList<RouteSegment> mycketNajs = new ArrayList<RouteSegment>();

    private void takeEverythingAndDoItMoreBetterThanShit(ArrayList<RouteSegment> result) {
        for (RouteSegment routeSegment : result) {
            // FÖR VARJE JÄVLA ROUTE SEGMENT VI FÅR!!! :((
            int line = routeSegment.getSegmentId().getCarrier().getNumber();
            String direction = routeSegment.getDirection();

            // Om nyckeln redan finns, då ska ju mer data läggas till!
            // OCH OM NYCKELN REDAN FINNS
            if(groupedView.containsKey(line)) {

                boolean foundIt = false;
                for(int i = 0; i < groupedView.get(line).size(); i++) {
                    if(direction.equals(groupedView.get(line).get(i).getDirection())) {
                        foundIt = true;
                        break;
                    }

                }
                if(!foundIt) {
                    groupedView.get(line).add(routeSegment);
                }
                //groupedView.get(line).add(routeSegment);
                /*
                //Men vi vill inte ha samma direction på varje, då man vill ändra håll enkelt.
                ArrayList<RouteSegment> temp = groupedView.get(line);
                for(RouteSegment rs : temp) {


                    // Kollar efter dubletter, vi vill inte ha flera med en destionation
                    boolean foundIt = false;
                    if(routeSegment.getDirection().equals(rs.getDirection())) {
                        foundIt = true;
                    }
                    else if(!foundIt) {
                        groupedView.get(line).add(routeSegment);
                    }

                }*/

            }
            else {
                groupedView.put(line, new ArrayList<RouteSegment>());
                groupedView.get(line).add(routeSegment);
            }
        }
    }

    private void setHeaderImage() {
        ImageView headerImageView = (ImageView)findViewById(R.id.header_picture);
        StaticMap headerMap = new StaticMap(new Coordinate(15.560494, 58.394281), new Coordinate(15.560580, 58.394281));

        Picasso.with(this).load("http://maps.googleapis.com/maps/api/staticmap?center=58.394281,15.560494&zoom=18&size=800x400&markers=color:blue%7Clabel:S%7C58.394281,15.560580&key=AIzaSyDvjJbCT-MFD3y_Wie5i7JTLZ8H5thSf8Y").into(headerImageView);
        //headerImageView.setImageBitmap(headerMap.getImage());

        Log.d("wtf", "hej");



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
