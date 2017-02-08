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

import com.project3w.wharrynathan_tripjournal.Fragments.AddTripFragment;
import com.project3w.wharrynathan_tripjournal.Objects.Trip;

public class AddTripActivity extends AppCompatActivity {

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
            throw new ClassCastException(aFrag.toString() + " must implement OnSelectedEventListener");
        }

        // commit the transaction
        fragmentTransaction.add(R.id.add_trip_container, aFrag);
        fragmentTransaction.commit();
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
            Trip enteredTrip = onSaveTripListener.getSavedTrip();

            System.out.println(enteredTrip.toString());

            // TODO: save trip in a helper
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
