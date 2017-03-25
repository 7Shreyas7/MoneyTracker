package com.hmproductions.moneytracker.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.widget.Toast;

import com.hmproductions.moneytracker.data.ExtrasContract.ExtrasEntry;

/**
 * Created by Harsh Mahajan on 28/1/2017.
 */

public class ExtrasProvider extends ContentProvider {

    static ExtrasDbHelper mDbHelper;
    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mUriMatcher.addURI(ExtrasContract.CONTENT_AUTHORITY,ExtrasContract.PATH_EXTRAS,100);
        mUriMatcher.addURI(ExtrasContract.CONTENT_AUTHORITY,ExtrasContract.PATH_EXTRAS + "/#",101);
    }

    @Override
    public boolean onCreate() {

        mDbHelper = new ExtrasDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = mUriMatcher.match(uri);

        switch(match)
        {
            case 100:
                cursor = db.query(ExtrasEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;

            case 101:
                selection = ExtrasEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(ExtrasEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Error while serving URI request");
        }
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = mUriMatcher.match(uri);

        switch(match)
        {
            case 100:
                long success = db.insert(ExtrasEntry.TABLE_NAME, null, contentValues);

                if(success == -1)
                    Toast.makeText(getContext(),"Failed to insert.",Toast.LENGTH_SHORT).show();
                else
                {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return ContentUris.withAppendedId(uri,success);

            default: throw new IllegalArgumentException("Failed to insert.");
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = mUriMatcher.match(uri);

        switch(match)
        {
            case 100:
                int success = db.delete(ExtrasEntry.TABLE_NAME,null,null);
                getContext().getContentResolver().notifyChange(uri,null);
                return success;

            case 101:
                selection = ExtrasEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                success = db.delete(ExtrasEntry.TABLE_NAME, selection, selectionArgs);
                return success;

            default: throw new IllegalArgumentException("Failed to delete.");

        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = mUriMatcher.match(uri);

        switch(match)
        {
            case 101:
                selection = ExtrasEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                int success = db.update(ExtrasEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                if(success>0)
                {
                    getContext().getContentResolver().notifyChange(uri, null);
                    return success;
                }

            default: throw new IllegalArgumentException("Failed to update");
        }
    }

    public static int getCostSum()
    {
        int total;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(ExtrasDbHelper.SQL_SELECT_COST_SUM, null);
        if(cursor.moveToFirst())
            total = cursor.getInt(0);
        else
            total=0;
        cursor.close();
        return total;
    }

    @Override
    public String getType(Uri uri) {
        // YOLO
        return null;
    }
}
