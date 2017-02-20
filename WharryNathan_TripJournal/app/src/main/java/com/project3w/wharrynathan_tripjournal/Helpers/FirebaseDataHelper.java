package com.project3w.wharrynathan_tripjournal.Helpers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project3w.wharrynathan_tripjournal.Objects.Trip;
import com.project3w.wharrynathan_tripjournal.Objects.TripItem;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Nate on 2/11/17.
 */

public class FirebaseDataHelper {


    public FirebaseDataHelper() {
        // empty constructor
    }


    public void saveTrip(Trip trip) {

        // get current logged in user
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUser = mAuth.getCurrentUser().getUid();

        // get Firebase Database instances
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tripDataRef = database.getReference("trips");
        DatabaseReference userDataRef = database.getReference("users").child(currentUser).child("trips");

        // create HashMap for updating trips
        HashMap<String, Object> newTrip = new HashMap<>();
        newTrip.put(trip.getTripId(), trip);

        // save the trip
        tripDataRef.updateChildren(newTrip);

        // create HashMap for updating user with trip
        HashMap<String, Object> updateUser = new HashMap<>();
        updateUser.put(trip.getTripId(), true);

        // update the user
        userDataRef.updateChildren(updateUser);

    }

    public void saveTripItem(String tripId, TripItem tripItem) {

        // get our firebase reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tripItemsRef = database.getReference("tripitems/" + tripId);

        // push in our trip item
        tripItemsRef.push().setValue(tripItem);

    }
}
