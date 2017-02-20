// Nathan Wharry
// APD2 - 1702
// AddTripActivity

package com.project3w.wharrynathan_tripjournal;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.iid.FirebaseInstanceId;
import com.project3w.wharrynathan_tripjournal.Fragments.AddTripFragment;
import com.project3w.wharrynathan_tripjournal.Fragments.AllTripsFragment;
import com.project3w.wharrynathan_tripjournal.Helpers.FirebaseDataHelper;
import com.project3w.wharrynathan_tripjournal.Objects.Trip;

public class AddTripActivity extends AppCompatActivity {


    public static final String ADD_TRIP_TAG = "com.project3w.wharrynathan_tripjournal.ADD_TRIP_TAG";

    FirebaseDataHelper firebaseDataHelper = new FirebaseDataHelper();

    public interface OnSaveTripListener {
        Trip getSavedTrip();
    }

    OnSaveTripListener onSaveTripListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        Toolbar toolbar = (Toolbar) findViewById(R.id.addtrip_toolbar);
        setSupportActionBar(toolbar);

        // add our AddTripFragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AddTripFragment aFrag = new AddTripFragment();

        // try to add our interface listener
        try {
            onSaveTripListener = (OnSaveTripListener) aFrag;
        } catch (ClassCastException e) {
            throw new ClassCastException(aFrag.toString() + " must implement OnSaveTripListener");
        }

        // commit the transaction
        fragmentTransaction.add(R.id.add_trip_container, aFrag, ADD_TRIP_TAG);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save_trip, menu);
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
            Trip enteredTrip = onSaveTripListener.getSavedTrip();

            if (enteredTrip != null) {

                // save our trip
                firebaseDataHelper.saveTrip(enteredTrip);

                // pass us over to the view trip activity
                Intent viewNewTrip = new Intent(this, ViewTripActivity.class);
                viewNewTrip.putExtra(AllTripsFragment.EXTRA_SELECTED_TRIP, enteredTrip);
                startActivity(viewNewTrip);

                // close this activity
                finish();

            } else {
                Snackbar.make(this.findViewById(android.R.id.content), "You must fill out all fields to save trip.", Snackbar.LENGTH_LONG).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // TODO: save entered values to restore
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);

        // TODO: restore saved values
    }
}
