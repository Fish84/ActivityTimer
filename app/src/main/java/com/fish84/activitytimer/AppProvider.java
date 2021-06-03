package com.fish84.activitytimer;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Fish84
 *
 * Provider for the ActivityTimer app. This is the only that knows about {@Link AppDatabase}
 */
public class AppProvider extends ContentProvider {
    public static final String TAG = "AppProvider";

    private AppDatabase mOpenHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    static final String CONTENT_AUTHORITY = "com.fish84.activitytimer.provider";
    public static final Uri CONTENT_AUTHORITY_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final int ACTIVITIES = 100;
    private static final int ACTIVITIES_ID = 101;

    private static final int TIMINGS = 200;
    private static final int TIMINGS_ID = 201;

    //private static final int ACTIVITY_TIMINGS = 300;
    //private static final int ACTIVITY_TIMINGS_ID = 301;

    private static final int ACTIVITY_DURATIONS = 400;
    private static final int ACTIVITY_DURATIONS_ID = 401;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        //  eg. content://com.fish84.activitytimer.provider/Tasks
        matcher.addURI(CONTENT_AUTHORITY, ActivitiesContract.TABLE_NAME, ACTIVITIES);
        // e.g. content://com.tish84.activitytimer.provider/Tasks/8
        matcher.addURI(CONTENT_AUTHORITY, ActivitiesContract.TABLE_NAME + "/#", ACTIVITIES_ID);

//        matcher.addURI(CONTENT_AUTHORITY, TimingsContract.TABLE_NAME, TIMINGS);
//        matcher.addURI(CONTENT_AUTHORITY, TimingsContract.TABLE_NAME + "/#", TIMINGS_ID);
//
//        matcher.addURI(CONTENT_AUTHORITY, DurationsContract.TABLE_NAME, ACTIVITY_DURATIONS);
//        matcher.addURI(CONTENT_AUTHORITY, DurationsContract.TABLE_NAME + "/#", ACTIVITY_DURATIONS_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = AppDatabase.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d(TAG, "query: called with URI " + uri);
        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "query: match is " + match);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (match){
            case ACTIVITIES:
                queryBuilder.setTables(ActivitiesContract.TABLE_NAME);
                break;

            case ACTIVITIES_ID:
                queryBuilder.setTables(ActivitiesContract.TABLE_NAME);
                long activityId = ActivitiesContract.getActivityId(uri);
                queryBuilder.appendWhere(ActivitiesContract.Columns._ID + " = " + activityId);
                break;

//            case TIMINGS:
//                queryBuilder.setTables(TimingsContract.TABLE_NAME);
//                break;

//            case TIMINGS_ID:
//                queryBuilder.setTables(TimingsContract.TABLE_NAME);
//                long timingId = TimingsContract.getTimingId(uri);
//                queryBuilder.appendWhere(TimingsContract.Columns._ID + " = " + timingId);
//                break;
//
//            case ACTIVITY_DURATIONS:
//                queryBuilder.setTables(DurationsContract.TABLE_NAME);
//                break;

//            case ACTIVITY_DURATIONS_ID:
//                queryBuilder.setTables(DurationsContract.TABLE_NAME);
//                long durationId = DurationsContract.getDurationId(uri);
//                queryBuilder.appendWhere(DurationsContract.Columns._ID + " = " + durationId);
//                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ACTIVITIES:
                return ActivitiesContract.CONTENT_TYPE;

            case ACTIVITIES_ID:
                return ActivitiesContract.CONTENT_ITEM_TYPE;

//            case TIMINGS:
//                return TimingsContract.Timings.CONTENT_TYPE;
//
//            case TIMINGS_ID:
//                return TimingsContract.Timings.CONTENT_ITEM_TYPE;
//
//            case TASK_DURATIONS:
//                return DurationsContract.TaskDurations.CONTENT_TYPE;
//
//            case TASK_DURATIONS_ID:
//                return DurationsContract.TaskDurations.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("unknown Uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "Entering insert, called with uri:" + uri);
        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "match is " + match);

        final SQLiteDatabase db;

        Uri returnUri;
        long recordId;

        switch (match) {
            case ACTIVITIES:
                db = mOpenHelper.getWritableDatabase();
                recordId = db.insert(ActivitiesContract.TABLE_NAME, null, values);
                if (recordId >= 0) {
                    returnUri = ActivitiesContract.buildActivityUri(recordId);
                } else {
                    throw new android.database.SQLException("Failed to insert into " + uri.toString());
                }
                break;

            case TIMINGS:
//                db = mOpenHelper.getWritableDatabase();
//                recordId = db.insert(TimingsContract.Timings.buildTimingUri(recordId));
//                if(recordId >=0) {
//                    returnUri = TimingsContract.Timings.buildTimingUri(recordId);
//                } else {
//                    throw new android.database.SQLException("Failed to insert into " + uri.toString());
//                }
//                break;

            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
        Log.d(TAG, "Exiting insert, returning " + returnUri);
        return returnUri;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "delete called with uri " + uri);
        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "match is " + match);

        final SQLiteDatabase db;
        int count;

        String selectionCriteria;

        switch(match) {
            case ACTIVITIES:
                db = mOpenHelper.getWritableDatabase();
                count = db.delete(ActivitiesContract.TABLE_NAME, selection, selectionArgs);
                break;

            case ACTIVITIES_ID:
                db = mOpenHelper.getWritableDatabase();
                long activityId = ActivitiesContract.getActivityId(uri);
                selectionCriteria = ActivitiesContract.Columns._ID + " = " + activityId;

                if((selection != null) && (selection.length()>0)) {
                    selectionCriteria += " AND (" + selection + ")";
                }
                count = db.delete(ActivitiesContract.TABLE_NAME, selectionCriteria, selectionArgs);
                break;

//            case TIMINGS:
//                db = mOpenHelper.getWritableDatabase();
//                count = db.delete(TimingsContract.TABLE_NAME, selection, selectionArgs);
//                break;
//
//            case TIMINGS_ID:
//                db = mOpenHelper.getWritableDatabase();
//                long timingsId = TimingsContract.getActivityId(uri);
//                selectionCriteria = TimingsContract.Columns._ID + " = " + timingsId;
//
//                if((selection != null) && (selection.length()>0)) {
//                    selectionCriteria += " AND (" + selection + ")";
//                }
//                count = db.delete(TimingsContract.TABLE_NAME, selectionCriteria, selectionArgs);
//                break;

            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
        Log.d(TAG, "Exiting update, returning " + count);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(TAG, "update called with uri " + uri);
        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "match is " + match);

        final SQLiteDatabase db;
        int count;

        String selectionCriteria;

        switch(match) {
            case ACTIVITIES:
                db = mOpenHelper.getWritableDatabase();
                count = db.update(ActivitiesContract.TABLE_NAME, values, selection, selectionArgs);
                break;

            case ACTIVITIES_ID:
                db = mOpenHelper.getWritableDatabase();
                long activityId = ActivitiesContract.getActivityId(uri);
                selectionCriteria = ActivitiesContract.Columns._ID + " = " + activityId;

                if((selection != null) && (selection.length()>0)) {
                    selectionCriteria += " AND (" + selection + ")";
                }
                count = db.update(ActivitiesContract.TABLE_NAME, values, selectionCriteria, selectionArgs);
                break;

//            case TIMINGS:
//                db = mOpenHelper.getWritableDatabase();
//                count = db.update(TimingsContract.TABLE_NAME, values, selection, selectionArgs);
//                break;
//
//            case TIMINGS_ID:
//                db = mOpenHelper.getWritableDatabase();
//                long timingsId = TimingsContract.getActivityId(uri);
//                selectionCriteria = TimingsContract.Columns._ID + " = " + timingsId;
//
//                if((selection != null) && (selection.length()>0)) {
//                    selectionCriteria += " AND (" + selection + ")";
//                }
//                count = db.update(TimingsContract.TABLE_NAME, values, selectionCriteria, selectionArgs);
//                break;

            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
        Log.d(TAG, "Exiting update, returning " + count);
        return count;
    }

}
