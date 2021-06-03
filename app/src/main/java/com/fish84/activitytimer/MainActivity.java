package com.fish84.activitytimer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

import com.fish84.activitytimer.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        String[] projection = {
                ActivitiesContract.Columns._ID,
                ActivitiesContract.Columns.ACTIVITIES_NAME,
                ActivitiesContract.Columns.ACTIVITIES_DESCRIPTION,
                ActivitiesContract.Columns.ACTIVITIES_SORTORDER};

        ContentResolver contentResolver = getContentResolver();

        ContentValues values = new ContentValues();
//        values.put(ActivitiesContract.Columns.ACTIVITIES_SORTORDER, "99");
//        values.put(ActivitiesContract.Columns.ACTIVITIES_DESCRIPTION, "Completed");
//        String selection = ActivitiesContract.Columns.ACTIVITIES_SORTORDER + " = " + 2;
//        int count = contentResolver.update(ActivitiesContract.CONTENT_URI, values, selection, null);
//        Log.d(TAG, "onCreate: " + count + "record(s) updated");

//        values.put(ActivitiesContract.Columns.ACTIVITIES_DESCRIPTION, "For deletion");
//        String selection = ActivitiesContract.Columns.ACTIVITIES_SORTORDER + " = ?";
//        String[] args = {"99"};

//        int count = contentResolver.delete(ActivitiesContract.buildActivityUri(3), null, null);
//        Log.d(TAG, "onCreate: " + count + "record(s) deleted");
        String selection = ActivitiesContract.Columns.ACTIVITIES_DESCRIPTION + " = ?";
        String[] args = {"For deletion"};
        int count = contentResolver.delete(ActivitiesContract.CONTENT_URI, selection, args);
        Log.d(TAG, "onCreate: " + count + " record(s) deleted");

//        values.put(ActivitiesContract.Columns.ACTIVITIES_NAME, "Content Provider");
//        values.put(ActivitiesContract.Columns.ACTIVITIES_DESCRIPTION, "Record Content Provider video");
//        int count = contentResolver.update(ActivitiesContract.buildActivityUri(4), values, null, null);
//        Log.d(TAG, "onCreate: " + count + "record(s) updated");
//        values.put(ActivitiesContract.Columns.ACTIVITIES_NAME, "New Task 1");
//        values.put(ActivitiesContract.Columns.ACTIVITIES_DESCRIPTION, "Description 1");
//        values.put(ActivitiesContract.Columns.ACTIVITIES_SORTORDER, 2);
//        Uri uri = contentResolver.insert(ActivitiesContract.CONTENT_URI, values);

        Cursor cursor = contentResolver.query(ActivitiesContract.CONTENT_URI,
//        Cursor cursor = contentResolver.query(ActivitiesContract.buildActivityUri(2),
                projection,
                null,
                null,
                ActivitiesContract.Columns.ACTIVITIES_SORTORDER);

        if(cursor != null) {
            Log.d(TAG, "onCreate: number of rows: " +cursor.getCount());
            while(cursor.moveToNext()) {
                for(int i=0; i<cursor.getColumnCount(); i++) {
                    Log.d(TAG, "onCreate: " + cursor.getColumnName(i) + ": " + cursor.getString(i));
                }
                Log.d(TAG,"onCreate: =================================");

            }
            cursor.close();
        }


//        AppDatabase appDatabase = AppDatabase.getInstance(this);
//        final SQLiteDatabase db = appDatabase.getReadableDatabase();


        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        //appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menumain_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //@Override
    //public boolean onSupportNavigateUp() {
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        //return NavigationUI.navigateUp(navController, appBarConfiguration)
                //|| super.onSupportNavigateUp();
    //}
}