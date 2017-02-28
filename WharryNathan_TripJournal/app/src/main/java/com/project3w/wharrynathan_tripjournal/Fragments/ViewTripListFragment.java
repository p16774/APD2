package com.project3w.wharrynathan_tripjournal.Fragments;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.project3w.wharrynathan_tripjournal.Objects.TripItem;
import com.project3w.wharrynathan_tripjournal.R;
import com.project3w.wharrynathan_tripjournal.ViewItemActivity;
import com.project3w.wharrynathan_tripjournal.ViewTripActivity;

import java.util.ArrayList;

/**
 * Created by Nate on 2/16/17.
 */

public class ViewTripListFragment extends ListFragment {

    ArrayList<TripItem> tripItemArrayList;

    // string references
    public static final String EXTRA_ITEM_POSITION = "com.project3w.wharrynathan_tripjournal.EXTRA_ITEM_POSITION";


    public ViewTripListFragment() {
        super();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_trip, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get our tripitem array from the bundle
        Bundle tripBundle = getArguments();
        tripItemArrayList = (ArrayList<TripItem>) tripBundle.getSerializable(ViewTripActivity.EXTRA_ITEM_ARRAY);

        ArrayAdapter<TripItem> tripArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, tripItemArrayList);

        setListAdapter(tripArrayAdapter);

        setEmptyText(getString(R.string.no_saved_items));

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        // create intent to send to the ViewItemActivity with selected trip
        Intent viewItemIntent = new Intent(getActivity(), ViewItemActivity.class);
        viewItemIntent.putExtra(EXTRA_ITEM_POSITION, position);
        viewItemIntent.putExtra(ViewTripActivity.EXTRA_ITEM_ARRAY, tripItemArrayList);
        startActivity(viewItemIntent);

    }
}
