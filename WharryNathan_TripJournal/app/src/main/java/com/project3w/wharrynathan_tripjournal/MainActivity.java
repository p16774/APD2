package com.project3w.wharrynathan_tripjournal;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.project3w.wharrynathan_tripjournal.Fragments.MainTripFragment;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements MainTripFragment.SelectedDateListener {

    // class variables
    String currentSelectedDate;
    public static final String EXTRA_CURRENT_DATE = "com.project3w.wharrynathan_tripjournal.EXTRA_CURRENT_DATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        // add our tripfragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MainTripFragment tFrag = new MainTripFragment();
        fragmentTransaction.add(R.id.trip_container, tFrag);
        fragmentTransaction.commit();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        // add our tripfragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MainTripFragment tFrag = new MainTripFragment();
        fragmentTransaction.replace(R.id.trip_container, tFrag);
        fragmentTransaction.commit();
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
        if (id == R.id.action_addtrip) {
            // create intent to send the user to the AddTripActivity
            Intent createTripIntent = new Intent(this, AddTripActivity.class);
            createTripIntent.putExtra(EXTRA_CURRENT_DATE, currentSelectedDate);
            startActivity(createTripIntent);

        } else if (id == R.id.action_import) {
            Snackbar.make(this.findViewById(android.R.id.content), "Import Trip Successful", Snackbar.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setSelectedDate(String date) {
        currentSelectedDate = date;
        Snackbar.make(this.findViewById(android.R.id.content), currentSelectedDate, Snackbar.LENGTH_LONG).show();
    }
}
