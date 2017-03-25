package com.hmproductions.moneytracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.hmproductions.moneytracker.data.ExtrasContract.ExtrasEntry;

/**
 * Created by Harsh Mahajan on 27/1/2017.
 */

class ExtrasDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="Money.db";

    private String SQL_CREATE_EXTRAS_TABLE =  "CREATE TABLE " + ExtrasEntry.TABLE_NAME + " ("
            + ExtrasEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ExtrasEntry.COLUMN_EXTRAS_NAME + " TEXT NOT NULL, "
            + ExtrasEntry.COLUMN_EXTRAS_DATE + " TEXT, "
            + ExtrasEntry.COLUMN_EXTRAS_COST + " INTEGER NOT NULL DEFAULT 0);";

    private String SQL_DELETE_EXTRAS_TABLE = "DROP TABLE IF EXISTS " + ExtrasEntry.TABLE_NAME;

    static String SQL_SELECT_COST_SUM = "SELECT SUM(cost) from " + ExtrasEntry.TABLE_NAME;

    ExtrasDbHelper(Context context) {
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_EXTRAS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_EXTRAS_TABLE);
        onCreate(db);
    }
}
