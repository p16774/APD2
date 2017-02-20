package com.project3w.wharrynathan_tripjournal.Helpers;

import android.app.Activity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project3w.wharrynathan_tripjournal.Objects.TripItem;

import java.util.ArrayList;

/**
 * Created by Nate on 2/18/17.
 */

public class GetTripDetailsHelper {

    public interface UpdateTripListListener {

        void updateTripItems(ArrayList<TripItem> tripItemArrayList);
    }

    private UpdateTripListListener onUpdateTripListListener;


    public GetTripDetailsHelper(Activity activity) {
        // attach our listener
        try {
            onUpdateTripListListener = (UpdateTripListListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement UpdateTripListListener");
        }
    }

    public void getTripDetails(String tripId) {

        // Get a reference to our trips
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tripItemsRef = database.getReference("tripitems/" + tripId);

        // create event listeners to add to the userTripsRef
        ValueEventListener tripItemsListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pullTripItems(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        tripItemsRef.orderByChild("timeStamp").addValueEventListener(tripItemsListener);

    }

    private void pullTripItems(DataSnapshot tripItemsSnapshot) {

        ArrayList<TripItem> tripItemsArray = new ArrayList<>();

        for (DataSnapshot tripItem : tripItemsSnapshot.getChildren()) {
            TripItem newTripItem = tripItem.getValue(TripItem.class);
            tripItemsArray.add(newTripItem);
        }

        onUpdateTripListListener.updateTripItems(tripItemsArray);

    }
}
