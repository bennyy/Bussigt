package com.bom.bussig.Data.Location;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
            SqlLiteHelper.COLUMN_LOCATION_ID
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

    public boolean createFavorite(int LocationID){
        boolean success = true;
        try {
            ContentValues values = new ContentValues();
            values.put(SqlLiteHelper.COLUMN_LOCATION_ID, LocationID);
            long insertedID = db.insert(SqlLiteHelper.TABLE_FAVORITES, null, values);
            Cursor cursor = db.query(SqlLiteHelper.TABLE_FAVORITES, allColumns, SqlLiteHelper.COLUMN_ID + " = " + insertedID, null, null, null, null);
            cursor.moveToFirst();
            cursor.close();
        }
        catch (Exception ex){
            success = false;
            Log.d("DB ERROR", "Could not add location to favorites");
        }
        return success;
    }

    public void deleteFavorite(int LocationID){
        db.delete(SqlLiteHelper.TABLE_FAVORITES, SqlLiteHelper.COLUMN_LOCATION_ID + " = " + LocationID, null);
    }
    public List<Integer> getAllFavorites(){
        List<Integer> favorites = new ArrayList<Integer>();

        Cursor cursor = db.query(SqlLiteHelper.TABLE_FAVORITES, allColumns, null, null, null, null,null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            favorites.add(cursor.getInt(1));
            cursor.moveToNext();
        }
        return favorites;
    }
}
