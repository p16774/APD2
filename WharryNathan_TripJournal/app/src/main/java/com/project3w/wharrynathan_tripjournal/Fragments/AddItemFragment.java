package com.project3w.wharrynathan_tripjournal.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.project3w.wharrynathan_tripjournal.AddTripItemActivity;
import com.project3w.wharrynathan_tripjournal.Objects.TripItem;
import com.project3w.wharrynathan_tripjournal.R;

import java.util.Date;

/**
 * Created by Nate on 2/18/17.
 */

public class AddItemFragment extends Fragment implements AddTripItemActivity.OnSaveTripItemListener {

    // class variables
    ImageView itemImageView;
    EditText itemTitleView, itemDescView;

    public AddItemFragment() {
        // empty constructor
    }

    @Override
    public TripItem getSavedItem(String imagePath) {

        // pull the entered values to save
        String itemImageUrl, itemTitle, itemDesc;
        long timeStamp;

        itemImageUrl = imagePath;
        itemTitle = itemTitleView.getText().toString();
        itemDesc = itemDescView.getText().toString();
        timeStamp = System.currentTimeMillis();

        // validate for null and return TripItem
        if (!itemImageUrl.equals("") && !itemTitle.equals("") && !itemDesc.equals("")) {
            return new TripItem(itemImageUrl, itemTitle, itemDesc, timeStamp);
        }

        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_item, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        itemImageView = (ImageView) getActivity().findViewById(R.id.additem_picture);
        itemTitleView = (EditText) getActivity().findViewById(R.id.additem_title);
        itemDescView = (EditText) getActivity().findViewById(R.id.additem_desc);

        itemImageView.setImageResource(R.drawable.placeholder);

    }

}
