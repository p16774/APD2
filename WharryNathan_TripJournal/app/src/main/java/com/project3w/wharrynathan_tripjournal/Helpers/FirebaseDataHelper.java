package com.project3w.wharrynathan_tripjournal.Helpers;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project3w.wharrynathan_tripjournal.Objects.Trip;
import com.project3w.wharrynathan_tripjournal.Objects.TripItem;
import com.project3w.wharrynathan_tripjournal.ViewTripActivity;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Nate on 2/11/17.
 */

public class FirebaseDataHelper {

    Activity mActivity;

    public FirebaseDataHelper(Activity activity) {
        mActivity = activity;
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

        // grab our StorageReference
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://wharrynathantripjournal.appspot.com");
        StorageReference saveLocationRef = storageRef.child("tripimages/" + tripId);

        // get our Uri File reference
        Uri imageUri = Uri.fromFile(new File(tripItem.getItemImageUri()));

        // set our image name and tripId
        tripItem.setImageName(imageUri.getLastPathSegment());
        tripItem.setTripId(tripId);

        // push in our trip item
        tripItemsRef.push().setValue(tripItem);

        // grab our image reference and Uri for File
        StorageReference imageRef = saveLocationRef.child(imageUri.getLastPathSegment());

        // register UploadTask and putFile
        UploadTask uploadTask = imageRef.putFile(imageUri);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Snackbar.make(mActivity.findViewById(android.R.id.content), "Image Upload Failed!!", Snackbar.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                // Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Snackbar.make(mActivity.findViewById(android.R.id.content), "Image Successfully Uploaded", Snackbar.LENGTH_LONG).show();
            }
        });


    }
}
