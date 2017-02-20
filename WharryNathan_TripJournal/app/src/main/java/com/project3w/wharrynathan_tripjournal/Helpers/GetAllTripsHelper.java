package com.project3w.wharrynathan_tripjournal.Helpers;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project3w.wharrynathan_tripjournal.Objects.Trip;

import java.util.ArrayList;

/**
 * Created by Nate on 2/15/17.
 */

public class GetAllTripsHelper {

    private DatabaseReference tripJournalRef;
    private DataSnapshot userTripsSnapshot, tripsSnapshot;
    private Activity mActivity;


    public interface UpdateListViewListener {

        void updateTripsList(ArrayList<Trip> userTripsArrayList);
    }

    UpdateListViewListener onUpdateListViewListener;

    public GetAllTripsHelper(Activity activity) {
        // assign our activity variable
        mActivity = activity;

        // attach our listener
        try {
            onUpdateListViewListener = (UpdateListViewListener) mActivity;
        } catch (ClassCastException e) {
            throw new ClassCastException(mActivity.toString() + " must implement UpdateListViewListener");
        }
    }

    public void getUserTrips() {

        // get current logged in user
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUser = mAuth.getCurrentUser().getUid();

        // Get a reference to our trips
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        tripJournalRef = database.getReference();

        // grab our reference to quickly add to other needs
        DatabaseReference userTripsRef = tripJournalRef.child("users").child(currentUser).child("trips");

        // create event listeners to add to the userTripsRef
        ValueEventListener userTripListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userTripsSnapshot = dataSnapshot;

                tripJournalRef.child("trips").orderByChild("tripStartDate").addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        tripsSnapshot = snapshot;
                        pullTripData(userTripsSnapshot, tripsSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        // attach our listeners then remove them
        userTripsRef.addListenerForSingleValueEvent(userTripListener);

    }

    private void pullTripData(DataSnapshot userTripsSnapshot, DataSnapshot tripsSnapshot) {

        ArrayList<Trip> userTripsArrayList = new ArrayList<>();

        for (DataSnapshot tripDetails : tripsSnapshot.getChildren()) {

            String tripId = tripDetails.getKey();

            for (DataSnapshot userTrip : userTripsSnapshot.getChildren()) {
                String userTripId = userTrip.getKey();
                if (userTripId.equals(tripId)) {
                    userTripsArrayList.add(tripDetails.getValue(Trip.class));
                }
            }
        }

        onUpdateListViewListener.updateTripsList(userTripsArrayList);

    }
}
