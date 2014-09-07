package com.bom.bussig.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bom.bussig.Adapter.LineListAdapter;
import com.bom.bussig.BussigApplication;
import com.bom.bussig.Data.Location.BussigDAL;
import com.bom.bussig.Data.Location.LocationService;
import com.bom.bussig.Helpers.AlphaForegroundColorSpan;
import com.bom.bussig.Helpers.Coordinate;
import com.bom.bussig.Helpers.StaticMap;
import com.bom.bussig.Model.Station;
import com.bom.bussig.R;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.mattiasbergstrom.resrobot.ResrobotClient;
import com.mattiasbergstrom.resrobot.RouteSegment;
import com.squareup.picasso.Picasso;

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

    private SwipeListView lineListView;
    private View placeHolderView;
    private View header;
    private ImageView headerLogo;
    private int mHeaderHeight;
    private int mMinHeaderTranslation;
    private int mActionBarHeight;
    private AlphaForegroundColorSpan alphaForegroundColorSpan;
    private SpannableString mSpannableString;
    private Station mStation;
    private LocationService locationService;

    private TypedValue mTypedValue = new TypedValue();
    private int actionBarTitleColor;
    private Context mContext;
    private HashMap<Integer, ArrayList<RouteSegment>> groupedView = new HashMap<Integer, ArrayList<RouteSegment>>();
    ArrayList<RouteSegment> heraderp = new ArrayList<RouteSegment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        locationService = new LocationService(this);
        this.mContext = this;
        mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
        mMinHeaderTranslation = -mHeaderHeight + getActionBarHeight();
        //Eeeh...?
        //setContentView(R.layout.activity_noboringactionbar);

        setContentView(R.layout.line_list_fancy_header);

        this.mStation = (Station)getIntent().getSerializableExtra(getString(R.string.STATION));

        lineListView = (SwipeListView) findViewById(R.id.listview);
        lineListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final RouteSegment item = (RouteSegment) parent.getItemAtPosition(position);
                Intent intent = new Intent(BussigApplication.getContext(), DepartureListActivity.class);

                intent.putExtra(BussigApplication.getContext().getString(R.string.ROUTE_SEGMENT), item);

                startActivity(intent);
            }
        });

        lineListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                //((TextView) view.findViewById((R.id.bussnr))).getText();

                return true;
            }
        });

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

        client.departures(this.mStation.getLocationID(), 120, new ResrobotClient.DeparturesCallback() {

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
                // TODO: mAdapter.notifyDataSetChanged(); här kanske?
                LineListAdapter lla = new LineListAdapter(getApplicationContext(), R.layout.activity_line_list, heraderp);
                lineListView.setAdapter(lla);
            }
        });

        lineListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                RouteSegment routesegment = (RouteSegment)lineListView.getItemAtPosition(position);
                // Ta in linjenumret här och do magic!
                int line = routesegment.getSegmentId().getCarrier().getNumber();
                String direction = routesegment.getDirection();
                int indexToBeChoosen = 0;
                int numberOfLines = groupedView.get(line).size();

                if(numberOfLines > 1) {
                    // Hitta vart den ligger i listan, av den linjen
                    for(int i = 0; i < numberOfLines; i++) {
                        RouteSegment routeSegment = groupedView.get(line).get(i);
                        if(routeSegment.getDirection().equals(direction)) {
                            if(i == numberOfLines - 1) {
                                // om det slår "över"
                                indexToBeChoosen = 0;
                            }
                            else {
                                // Annars är de ju bara ta nästa
                                indexToBeChoosen = ++i;
                            }
                            break;
                        }
                    }
                }
                else {
                    Log.d("LineListActivity", "Det är bara en resa från ett håll inom närmaste 120 min");
                }


                // Hitta vilket index i AdapterListan som den ligger på
                // som ska ändras. Vi vill ju bevara de förra ändringarna som fanns i listan också
                int itemToBeChanged = 0;
                for(int i = 0; i < heraderp.size(); i++) {
                    if(line == heraderp.get(i).getSegmentId().getCarrier().getNumber()) {
                        itemToBeChanged = i;
                        break;
                    }
                }

                Iterator it = groupedView.entrySet().iterator();
                while(it.hasNext()) {
                    Map.Entry item = (Map.Entry)it.next();
                    if(line == (Integer)item.getKey()) {
                        heraderp.set(itemToBeChanged, ((ArrayList<RouteSegment>) item.getValue()).get(indexToBeChoosen));
                    }
                    //it.remove();
                }

                LineListAdapter lla = new LineListAdapter(getApplicationContext(), R.layout.activity_line_list, heraderp);
                lineListView.setAdapter(lla);
            }
        });

        lineListView.setSwipeListViewListener(new BaseSwipeListViewListener(){
            @Override
            public void onOpened(int position, boolean toRight) {
            }

            @Override
            public void onClosed(int position, boolean fromRight) {
            }

            @Override
            public void onListChanged() {
            }

            @Override
            public void onMove(int position, float x) {
            }

            @Override
            public void onStartOpen(int position, int action, boolean right) {
                Log.d("swipe", String.format("onStartOpen %d - action %d", position, action));
            }

            @Override
            public void onStartClose(int position, boolean right) {
                Log.d("swipe", String.format("onStartClose %d", position));
            }

            @Override
            public void onClickFrontView(int position) {
                Log.d("swipe", String.format("onClickFrontView %d", position));
            }


            @Override
            public void onChoiceChanged(int position, boolean selected) {
                RouteSegment routesegment = (RouteSegment)lineListView.getItemAtPosition(position);
                // Ta in linjenumret här och do magic!
                int line = routesegment.getSegmentId().getCarrier().getNumber();
                String direction = routesegment.getDirection();
                int indexToBeChoosen = 0;
                int numberOfLines = groupedView.get(line).size();

                if(numberOfLines > 1) {
                    // Hitta vart den ligger i listan, av den linjen
                    for(int i = 0; i < numberOfLines; i++) {
                        RouteSegment routeSegment = groupedView.get(line).get(i);
                        if(routeSegment.getDirection().equals(direction)) {
                            if(i == numberOfLines - 1) {
                                // om det slår "över"
                                indexToBeChoosen = 0;
                            }
                            else {
                                // Annars är de ju bara ta nästa
                                indexToBeChoosen = ++i;
                            }
                            break;
                        }
                    }
                }
                else {
                    Log.d("LineListActivity", "Det är bara en resa från ett håll inom närmaste 120 min");
                }


                // Hitta vilket index i AdapterListan som den ligger på
                // som ska ändras. Vi vill ju bevara de förra ändringarna som fanns i listan också
                int itemToBeChanged = 0;
                for(int i = 0; i < heraderp.size(); i++) {
                    if(line == heraderp.get(i).getSegmentId().getCarrier().getNumber()) {
                        itemToBeChanged = i;
                        break;
                    }
                }

                Iterator it = groupedView.entrySet().iterator();
                while(it.hasNext()) {
                    Map.Entry item = (Map.Entry)it.next();
                    if(line == (Integer)item.getKey()) {
                        heraderp.set(itemToBeChanged, ((ArrayList<RouteSegment>) item.getValue()).get(indexToBeChoosen));
                    }
                    //it.remove();
                }

                LineListAdapter lla = new LineListAdapter(getApplicationContext(), R.layout.activity_line_list, heraderp);
                lineListView.setAdapter(lla);
            }

            @Override
            public void onClickBackView(int position) {
                Log.d("swipe", String.format("onClickBackView %d", position));
            }

            @Override
            public void onDismiss(int[] reverseSortedPositions) {
                Log.d("Swajp", "Dismiss");
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
            }
            else {
                groupedView.put(line, new ArrayList<RouteSegment>());
                groupedView.get(line).add(routeSegment);
            }
        }
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
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        View headerView = getLayoutInflater().inflate(R.layout.line_list_action_bar_layout, null);
        ToggleButton favoriteToggle = (ToggleButton) headerView.findViewById(R.id.line_list_action_bar_favorite);

        favoriteToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    new BussigDAL(mContext).addStationToFavorites(mStation);
                    CharSequence text = mStation.toString() + " add to favorites";
                    Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
                } else {
                    new BussigDAL(mContext).addStationToFavorites(mStation);
                    CharSequence text = mStation.toString() + " removed from favorites";
                    Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
                }
            }
        });
        actionBar.setCustomView(headerView);
        //actionBar.setIcon(R.drawable.logo1);

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
