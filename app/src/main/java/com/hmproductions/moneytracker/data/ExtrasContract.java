package com.hmproductions.moneytracker.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Harsh Mahajan on 27/1/2017.
 */

public class ExtrasContract {

    ExtrasContract () {}

    public static final String CONTENT_AUTHORITY = "com.hmproductions.moneytracker";
    public static final String PATH_EXTRAS = "extras";

    public static final Uri BASE_CONTENT_PROVIDER =Uri.parse("content://" + CONTENT_AUTHORITY);

    public static class ExtrasEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_PROVIDER,PATH_EXTRAS);

        public final static String TABLE_NAME = "extras";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_EXTRAS_NAME ="name";
        public final static String COLUMN_EXTRAS_DATE = "date";
        public final static String COLUMN_EXTRAS_COST = "cost";

        public static final int ITEM_FRENCHFRIES = 0;
        public static final int ITEM_GOBIMANCHURIAN = 1;
        public static final int ITEM_MUSHROOMMASALA = 2;
        public static final int ITEM_PANEERBUTTERMASALA = 3;
        public static final int ITEM_PANEERFRIEDRICE = 4;
        public static final int ITEM_KADAIPANEER = 5;
        public static final int ITEM_PALAKPANEER = 6;
        public static final int ITEM_GOBI65 = 7;
        public static final int ITEM_ALOO65 = 8;
        public static final int ITEM_PANEER65 = 9;
        public static final int ITEM_CHICKENFRIEDRICE = 10;
        public static final int ITEM_CHICKENMASALA = 11;
        public static final int ITEM_FISHFRY = 12;
        public static final int ITEM_EGGCURRY = 13;
        public static final int ITEM_OMELET = 14;
        public static final int ITEM_OTHERS = 15;
    }
}
