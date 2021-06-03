package com.fish84.activitytimer;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.fish84.activitytimer.AppProvider.CONTENT_AUTHORITY;
import static com.fish84.activitytimer.AppProvider.CONTENT_AUTHORITY_URI;

public class ActivitiesContract {

    static final String TABLE_NAME = "Activities";
    //ActivitiesFields
    public static class Columns{
        public static final String _ID = BaseColumns._ID;
        public static final String ACTIVITIES_NAME = "Name";
        public static final String ACTIVITIES_DESCRIPTION = "Description";
        public static final String ACTIVITIES_SORTORDER = "SortOrder";

        private Columns() {
            // private constructor to prevent instantiation
        }


    }

    /**
     * The URI to ACCESS the Activities table
     */

    public static final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TABLE_NAME);

    static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;
    static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;

    static  Uri buildActivityUri(long activityId){
        return ContentUris.withAppendedId(CONTENT_URI, activityId);
    }

    static long getActivityId(Uri uri) {
        return ContentUris.parseId(uri);
    }

}
