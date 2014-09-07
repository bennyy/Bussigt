package com.bom.bussig.Data.Location;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Mackan on 2014-09-06.
 */
public class SqlLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_FAVORITES = "Favorites";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_LOCATION_ID = "Location_ID";

    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_FAVORITES
            + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_LOCATION_ID + " INTEGER NOT NULL);";

    public SqlLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newVersion) {
        Log.w(SqlLiteHelper.class.getName(), "Upgrading database from version " + oldversion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }
}
