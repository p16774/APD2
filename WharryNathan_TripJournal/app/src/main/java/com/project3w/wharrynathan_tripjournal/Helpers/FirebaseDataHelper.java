package com.project3w.wharrynathan_tripjournal.Helpers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project3w.wharrynathan_tripjournal.Objects.Trip;
import com.project3w.wharrynathan_tripjournal.Objects.TripItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Nate on 2/11/17.
 */

public class FirebaseDataHelper {

    public FirebaseDataHelper() {
        // empty constructor
    }

    public ArrayList<Trip> getUserTrips() {

        // our returning ArrayList of trips
        final ArrayList<Trip> userTripArray = new ArrayList<>();

        // get current logged in user
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUser = mAuth.getCurrentUser().getUid();

        // Get a reference to our trips
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference tripJournalRef = database.getReference();

        // fetch a list of Mary's groups
        tripJournalRef.child("users").child(currentUser).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                // for each group, fetch the name and print it
                String tripId = snapshot.getKey();
                tripJournalRef.child("trips/" + tripId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Trip userTrip = snapshot.getValue(Trip.class);
                        userTripArray.add(userTrip);
                        System.out.println(userTripArray + " this is what I'm adding to the array");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return userTripArray;
    }

    public void saveTrip(Trip trip) {

        // get current logged in user
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUser = mAuth.getCurrentUser().getUid();

        // get Firebase Database instances
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tripDataRef = database.getReference("trips");
        DatabaseReference userDataRef = database.getReference("users").child(currentUser);

        // generate unique id to save trip under
        long genericLong = UUID.randomUUID().getLeastSignificantBits();
        String genericId = genericLong + "";

        // create HashMap for updating trips
        HashMap<String, Object> newTrip = new HashMap<>();
        newTrip.put(genericId, trip);

        // save the trip
        tripDataRef.updateChildren(newTrip);

        // create HashMap for updating user with trip
        HashMap<String, Object> updateUser = new HashMap<>();
        updateUser.put(genericId, true);

        // update the user
        userDataRef.updateChildren(updateUser);

    }

    public void saveTripItem(Trip trip, TripItem item) {

    }
}
