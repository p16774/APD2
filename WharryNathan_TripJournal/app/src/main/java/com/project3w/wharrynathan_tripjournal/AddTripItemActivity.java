package com.project3w.wharrynathan_tripjournal;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.project3w.wharrynathan_tripjournal.Fragments.AddItemFragment;
import com.project3w.wharrynathan_tripjournal.Helpers.FirebaseDataHelper;
import com.project3w.wharrynathan_tripjournal.Objects.TripItem;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nate on 2/16/17.
 */

public class AddTripItemActivity extends AppCompatActivity {

    // class variables
    String currentTripId, mCurrentPhotoPath;
    FirebaseDataHelper firebaseDataHelper = new FirebaseDataHelper();

    // class references
    public static final String ADD_ITEM_TAG = "com.project3w.wharrynathan_tripjournal.ADD_ITEM_TAG";
    public static final int REQUEST_IMAGE_CAPTURE = 0x01001;

    public interface OnSaveTripItemListener {
        TripItem getSavedItem();
    }

    OnSaveTripItemListener onSaveTripItemListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.additem_toolbar);
        setSupportActionBar(toolbar);

        // pull in our intent data and set our trip info
        currentTripId = getIntent().getExtras().getString(ViewTripActivity.EXTRA_TRIP_ID);

        // add our AddTripFragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AddItemFragment aFrag = new AddItemFragment();

        // try to add our interface listener
        try {
            onSaveTripItemListener = (OnSaveTripItemListener) aFrag;
        } catch (ClassCastException e) {
            throw new ClassCastException(aFrag.toString() + " must implement OnSaveTripItemListener");
        }

        // commit the transaction
        fragmentTransaction.add(R.id.add_item_container, aFrag, ADD_ITEM_TAG);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_saveitem) {
            TripItem enteredTripItem = onSaveTripItemListener.getSavedItem();

            if (enteredTripItem != null) {

                // save our trip
                firebaseDataHelper.saveTripItem(currentTripId, enteredTripItem);

                // return to our ViewTripActivity
                finish();

            } else {
                Snackbar.make(this.findViewById(android.R.id.content), "You must fill out all fields to save memory.", Snackbar.LENGTH_LONG).show();
            }
        } else if (id == R.id.action_takepicture) {
           // dispatchTakePictureIntent();
            Snackbar.make(this.findViewById(android.R.id.content), "Picture Taken Placeholder", Snackbar.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                System.out.println("ERROR IN CREATING FILE");
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.project3w.wharrynathan_tripjournal.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView itemPhoto = (ImageView) findViewById(R.id.additem_picture);
            itemPhoto.setImageBitmap(imageBitmap);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = System.currentTimeMillis() + "";
        String imageFileName = "ITEM_" + timeStamp;
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Trip_Journal_Images");
        System.out.println(storageDir);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


}
