package com.project3w.wharrynathan_tripjournal;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project3w.wharrynathan_tripjournal.Fragments.AllTripsFragment;
import com.project3w.wharrynathan_tripjournal.Helpers.GetAllTripsHelper;
import com.project3w.wharrynathan_tripjournal.Objects.Trip;

import java.util.ArrayList;
import java.util.HashMap;

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
        fragmentTransaction.commitAllowingStateLoss();

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
            enterTripCode();

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

    public void enterTripCode() {
        // display trip code entry dialog
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.trip_code_input, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setTitle(getString(R.string.import_shared_trip));

        // setup our form fields
        final EditText tripCode = (EditText) promptView.findViewById(R.id.tripcode_number);

        // enter and validate information
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // pull in our employee values
                        final String code = tripCode.getText().toString();

                        // validate code is a valid trip
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        final DatabaseReference tripRef = database.getReference().child("trips").child(code);
                        tripRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // get current user
                                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                    String currentUser = mAuth.getCurrentUser().getUid();

                                    // get our user reference
                                    DatabaseReference userDataRef = database.getReference("users").child(currentUser).child("trips");

                                    // create HashMap for updating user with trip
                                    HashMap<String, Object> updateUser = new HashMap<>();
                                    updateUser.put(code, true);

                                    // update the user
                                    userDataRef.updateChildren(updateUser);

                                    // create intent to send user to the newly added trip
                                    tripRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Trip enterdTrip = dataSnapshot.getValue(Trip.class);
                                            callTrip(enterdTrip);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                } else {
                                    Toast.makeText(getApplicationContext(), "ERROR: Invalid Trip Code", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {

                            }
                        });
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();

    }

    public void callTrip(Trip enteredTrip) {
        // pass us over to the view trip activity
        Intent viewNewTrip = new Intent(this, ViewTripActivity.class);
        viewNewTrip.putExtra(AllTripsFragment.EXTRA_SELECTED_TRIP, enteredTrip);
        startActivity(viewNewTrip);
    }
}
