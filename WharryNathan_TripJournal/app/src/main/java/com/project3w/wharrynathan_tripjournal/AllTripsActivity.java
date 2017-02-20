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
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.project3w.wharrynathan_tripjournal.Fragments.AllTripsFragment;
import com.project3w.wharrynathan_tripjournal.Helpers.GetAllTripsHelper;
import com.project3w.wharrynathan_tripjournal.Objects.Trip;

import java.util.ArrayList;

public class AllTripsActivity extends AppCompatActivity implements AllTripsFragment.SelectedDateListener, GetAllTripsHelper.UpdateListViewListener {

    // class variables
    String currentSelectedDate;
    ArrayList<Trip> userTripArray;
    GetAllTripsHelper mHelper;
    boolean completedFirstLoad = false;

    // string variables for intent and fragment
    public static final String ALL_TRIPS_TAG = "com.project3w.wharrynathan_tripjournal.ALL_TRIPS_TAG";
    public static final String EXTRA_CURRENT_DATE = "com.project3w.wharrynathan_tripjournal.EXTRA_CURRENT_DATE";
    public static final String EXTRA_TRIP_ARRAY = "com.project3w.wharrynathan_tripjournal.EXTRA_TRIP_ARRAY";

    public void updateTripsList(ArrayList<Trip> userTripsArrayList) {

        // replace our tripfragment to rerun code
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AllTripsFragment tFrag = new AllTripsFragment();

        // create bundle of arraylist
        Bundle listData = new Bundle();
        listData.putSerializable(EXTRA_TRIP_ARRAY, userTripsArrayList);

        // attach bundle and commit fragment
        tFrag.setArguments(listData);
        fragmentTransaction.replace(R.id.trip_container, tFrag, ALL_TRIPS_TAG);
        fragmentTransaction.commit();

        View progressBar = findViewById(R.id.alltrips_progressbar);
        progressBar.setVisibility(View.GONE);

        completedFirstLoad = true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        // assign helper file and call our data
        mHelper = new GetAllTripsHelper(this);
        mHelper.getUserTrips();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        View progressBar = findViewById(R.id.alltrips_progressbar);
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (completedFirstLoad) {
            getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentByTag(ALL_TRIPS_TAG)).commit();
            View progressBar = findViewById(R.id.alltrips_progressbar);
            progressBar.setVisibility(View.VISIBLE);
            mHelper.getUserTrips();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_trip, menu);
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

        } else if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setSelectedDate(String date) {
        currentSelectedDate = date;
        Snackbar.make(this.findViewById(android.R.id.content), currentSelectedDate, Snackbar.LENGTH_LONG).show();
    }
}
