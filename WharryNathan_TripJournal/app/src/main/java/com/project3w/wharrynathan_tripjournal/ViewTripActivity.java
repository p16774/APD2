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
import com.project3w.wharrynathan_tripjournal.Fragments.ViewTripListFragment;
import com.project3w.wharrynathan_tripjournal.Helpers.GetTripDetailsHelper;
import com.project3w.wharrynathan_tripjournal.Objects.Trip;
import com.project3w.wharrynathan_tripjournal.Objects.TripItem;

import java.util.ArrayList;

/**
 * Created by Nate on 2/12/17.
 */

public class ViewTripActivity extends AppCompatActivity implements GetTripDetailsHelper.UpdateTripListListener {

    // class variables
    String currentSelectedTrip;
    GetTripDetailsHelper mHelper;
    boolean completedFirstLoad = false;

    // class string extras
    public static final String EXTRA_ITEM_ARRAY = "com.project3w.wharrynathan_tripjournal.EXTRA_ITEM_ARRAY";
    public static final String EXTRA_TRIP_ID = "com.project3w.wharrynathan_tripjournal.EXTRA_TRIP_ID";
    public static final String VIEW_TRIP_TAG = "com.project3w.wharrynathan_tripjournal.VIEW_TRIP_TAG";

    public void updateTripItems(ArrayList<TripItem> tripItemArrayList) {

        // attach our fragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ViewTripListFragment vFrag = new ViewTripListFragment();

        // create bundle of arraylist
        Bundle tripDetails = new Bundle();
        tripDetails.putSerializable(EXTRA_ITEM_ARRAY, tripItemArrayList);

        // attach bundle and commit fragment
        vFrag.setArguments(tripDetails);
        fragmentTransaction.replace(R.id.view_trip_container, vFrag, VIEW_TRIP_TAG);
        fragmentTransaction.commitAllowingStateLoss(); // have to call this due to some weird backstack/asynchtask issue

        //View progressBar = findViewById(R.id.alltrips_progressbar);
        //progressBar.setVisibility(View.GONE);

        completedFirstLoad = true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.viewtrip_toolbar);

        // get the data passed in for our trip
        if (getIntent().hasExtra(AllTripsFragment.EXTRA_SELECTED_TRIP)) {

            // pull in the bundle
            Bundle tripBundle = getIntent().getExtras();

            // cast the data to our Trip object
            Trip tripDetails = (Trip) tripBundle.getSerializable(AllTripsFragment.EXTRA_SELECTED_TRIP);
            currentSelectedTrip = tripDetails.getTripId();

            // set the title of our action bar
            toolbar.setTitle(tripDetails.getTripTitle());
            setSupportActionBar(toolbar);

            // assign helper file and call our data
            mHelper = new GetTripDetailsHelper(this);
            mHelper.getTripDetails(currentSelectedTrip);

        } else {
            throw new ClassCastException(this.toString() + " must pass in an EXTRA_SELECTED_TRIP");
        }

    }

    @Override
    protected void onPostResume() {

        if (completedFirstLoad) {
            completedFirstLoad = false; // setting for testing
            getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentByTag(VIEW_TRIP_TAG)).commit();
            //View progressBar = findViewById(R.id.alltrips_progressbar);
            //progressBar.setVisibility(View.VISIBLE);
            mHelper.getTripDetails(currentSelectedTrip);
        }
        super.onPostResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_additem) {
            // create intent to send the user to the AddTripActivity
            Intent addItemIntent = new Intent(this, AddTripItemActivity.class);
            addItemIntent.putExtra(EXTRA_TRIP_ID, currentSelectedTrip);
            startActivity(addItemIntent);
        }

        return super.onOptionsItemSelected(item);
    }

}
