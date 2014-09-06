package com.bom.bussig.Activity;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.bom.bussig.Adapter.LineListAdapter;
import com.bom.bussig.R;
import com.mattiasbergstrom.resrobot.ResrobotClient;
import com.mattiasbergstrom.resrobot.RouteSegment;

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
public class LineListActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_list);

        ResrobotClient client = new ResrobotClient("tAKhTKVqWF8OmVsJrJQqtlQzPQpBFTNr", "tAKhTKVqWF8OmVsJrJQqtlQzPQpBFTNr");

        client.departures(7400009, 120, new ResrobotClient.DeparturesCallback() {
            @Override
            public void departuresComplete(ArrayList<RouteSegment> result) {
                Log.d("LineListAct", "Hamtade data bra och najs o så!");
                LineListAdapter lineListAdapter = new LineListAdapter(getApplicationContext(), R.layout.activity_line_list, result);
                setListAdapter(lineListAdapter);
            }
        });
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
