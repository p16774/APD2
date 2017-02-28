package com.project3w.wharrynathan_tripjournal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project3w.wharrynathan_tripjournal.Fragments.ViewTripListFragment;
import com.project3w.wharrynathan_tripjournal.Helpers.ZoomOutPageTransformer;
import com.project3w.wharrynathan_tripjournal.Objects.Trip;
import com.project3w.wharrynathan_tripjournal.Objects.TripItem;

import java.util.ArrayList;

/**
 * Created by Nate on 2/19/17.
 */

public class ViewItemActivity extends AppCompatActivity {

    int selectedTrip;
    ArrayList<TripItem> itemArrayList;
    TripItemPagerAdapter mTripItemPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.viewitem_toolbar);
        setSupportActionBar(toolbar);

        // pull in the bundle
        Bundle itemBundle = getIntent().getExtras();
        selectedTrip = itemBundle.getInt(ViewTripListFragment.EXTRA_ITEM_POSITION);
        itemArrayList = (ArrayList<TripItem>) itemBundle.getSerializable(ViewTripActivity.EXTRA_ITEM_ARRAY);

        // call in our ViewPager to swipe through items
        mTripItemPagerAdapter = new TripItemPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewitem_pager);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mViewPager.setAdapter(mTripItemPagerAdapter);
        mViewPager.setCurrentItem(selectedTrip);

        // add page listener to invalidate the menu
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When changing pages, reset the action bar actions since they are dependent
                // on which page is currently active. An alternative approach is to have each
                // fragment expose actions itself (rather than the activity exposing actions),
                // but for simplicity, the activity provides the actions in this sample.
                invalidateOptionsMenu();
            }
        });

    }

    public class TripItemPagerAdapter extends FragmentStatePagerAdapter {
        public TripItemPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return TripItemFragment.create(itemArrayList.get(position));
        }

        @Override
        public int getCount() {
            return itemArrayList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return itemArrayList.get(position).getItemTitle();
        }
    }

    public static class TripItemFragment extends Fragment {
        public static final String ARG_TRIP_ITEM = "com.project3w.wharrynathan_tripjournal.ARG_TRIP_ITEM";

        private TripItem mTripItem;

        public static TripItemFragment create(TripItem tripItem) {
            TripItemFragment fragment = new TripItemFragment();
            Bundle args = new Bundle();
            args.putSerializable(ARG_TRIP_ITEM, tripItem);
            System.out.println(tripItem + " this is the trip item being passed to the fragment.");
            fragment.setArguments(args);
            return fragment;
        }

        public TripItemFragment() {
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            mTripItem = (TripItem) getArguments().getSerializable(ARG_TRIP_ITEM);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // get reference to our view
            View rootView = inflater.inflate(R.layout.fragment_view_item, container, false);

            ImageView itemImage = (ImageView) rootView.findViewById(R.id.viewitem_image);
            TextView itemTitle = (TextView) rootView.findViewById(R.id.viewitem_title);
            TextView itemDesc = (TextView) rootView.findViewById(R.id.viewitem_desc);

            // get our storage reference
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://wharrynathantripjournal.appspot.com");
            StorageReference imageRef = storageRef.child("tripimages/" + mTripItem.getTripId() + "/" + mTripItem.getImageName());

            // set our memory text
            itemImage.setImageResource(R.drawable.placeholder);
            itemTitle.setText(mTripItem.getItemTitle());
            itemDesc.setText(mTripItem.getItemDesc());

            // download and set our imageview
            Glide.with(getActivity())
                    .using(new FirebaseImageLoader())
                    .load(imageRef)
                    .into(itemImage);

            return rootView;
        }
    }



    @Override
    protected void onPostResume() {

        super.onPostResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_deleteitem) {
            Snackbar.make(this.findViewById(android.R.id.content), R.string.failed_delete_function, Snackbar.LENGTH_LONG).show();

        }

        return super.onOptionsItemSelected(item);
    }

}
