package com.bom.bussig.Data.Location;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bom.bussig.Model.Station;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mackan on 2014-09-06.
 */
public class BussigDAL {
    private SQLiteDatabase db;
    private SqlLiteHelper dbHelper;
    private String[] allColumns = {
            SqlLiteHelper.COLUMN_ID,
            SqlLiteHelper.COLUMN_LOCATION_ID,
            SqlLiteHelper.COLUMN_NAME,
            SqlLiteHelper.COLUMN_LONGITUDE,
            SqlLiteHelper.COLUMN_LATITUDE
    };

    public BussigDAL(Context context){
        dbHelper = new SqlLiteHelper(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public boolean addStationToFavorites(Station station){
        boolean success = true;
        try {
            open();
            ContentValues values = new ContentValues();
            values.put(SqlLiteHelper.COLUMN_LOCATION_ID, station.getLocationID());
            values.put(SqlLiteHelper.COLUMN_NAME, station.getName());
            values.put(SqlLiteHelper.COLUMN_LONGITUDE, station.getLongitude());
            values.put(SqlLiteHelper.COLUMN_LATITUDE, station.getLatitude());
            long insertedID = db.insert(SqlLiteHelper.TABLE_FAVORITES, null, values);
            Cursor cursor = db.query(SqlLiteHelper.TABLE_FAVORITES, allColumns, SqlLiteHelper.COLUMN_ID + " = " + insertedID, null, null, null, null);
            cursor.moveToFirst();
            cursor.close();
            close();
        }
        catch (Exception ex){
            success = false;
            Log.d("DB ERROR", "Could not add location to favorites");
        }
        finally {
            close();
        }
        return success;
    }

    public void removeStationFromFavorite(Station station){
        try {
            open();
            db.delete(SqlLiteHelper.TABLE_FAVORITES, SqlLiteHelper.COLUMN_LOCATION_ID + " = " + station.getLocationID(), null);
        }
        catch(Exception ex){
            Log.d("DB ERROR", "Could not delete favorite with LocationID = " + station.getLocationID() );
        }
        finally {
            close();
        }
    }

    public boolean StationIsFavorite(Station station){
        boolean isFavorite = false;
        try{
            open();
            Cursor cursor = db.query(SqlLiteHelper.TABLE_FAVORITES, allColumns, SqlLiteHelper.COLUMN_LOCATION_ID + " = " + station.getLocationID(), null, null, null, null);
            if(cursor.moveToFirst())
                isFavorite = true;
        }
        catch (Exception ex){
            Log.d("DB ERROR", "There was an error when trying to check if station is added to favorites. Exception: " + ex.toString());
        }
        finally {
            close();
        }
        return isFavorite;
    }

    public List<Station> getAllFavorites(){

        List<Station> stations = new ArrayList<Station>();
        try {
            open();
            Cursor cursor = db.query(SqlLiteHelper.TABLE_FAVORITES, allColumns, null, null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                stations.add(this.CursorToStation(cursor));
                cursor.moveToNext();
            }
        }
        catch (Exception ex){
            Log.d("DB ERROR", "Could not get all favorites");
        }
        finally {
            close();
        }
        return stations;
    }

    public Station CursorToStation(Cursor cursor){
        return new Station(cursor.getInt(1), cursor.getString(2), cursor.getDouble(3), cursor.getDouble(4));
    }
}
