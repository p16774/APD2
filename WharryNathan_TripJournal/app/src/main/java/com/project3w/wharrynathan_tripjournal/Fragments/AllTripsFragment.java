package com.project3w.wharrynathan_tripjournal.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import com.project3w.wharrynathan_tripjournal.AddTripActivity;
import com.project3w.wharrynathan_tripjournal.Helpers.FirebaseDataHelper;
import com.project3w.wharrynathan_tripjournal.Objects.Trip;
import com.project3w.wharrynathan_tripjournal.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nate on 2/6/17.
 */

public class AllTripsFragment extends Fragment implements CalendarView.OnDateChangeListener {

    // class variables
    CalendarView tripCalendar;
    ListView tripList;
    Activity mActivity;
    FirebaseDataHelper mHelper;

    // interface to pass the selected date up to the main activity
    public interface SelectedDateListener {
        void setSelectedDate(String date);
    }

    // create our interface listener
    SelectedDateListener onSelectedDateListener;

    public AllTripsFragment() {
        // empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // attach our interface listener
        mActivity = getActivity();

        try {
            onSelectedDateListener = (SelectedDateListener) mActivity;
        } catch (ClassCastException e) {
            throw new ClassCastException(mActivity.toString() + " must implement OnSelectedEventListener");
        }

        // pull in our helper file
        mHelper = new FirebaseDataHelper();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // get our references to the calendar view and listview to manipulate data
        tripCalendar = (CalendarView) getActivity().findViewById(R.id.trip_calendar);
        tripList = (ListView) getActivity().findViewById(R.id.trip_listview);

        // set our empty text view
        TextView emptyText = (TextView) getActivity().findViewById(R.id.empty_text);
        tripList.setEmptyView(emptyText);

        // add our DateChangeListener
        tripCalendar.setOnDateChangeListener(this);

        // set up our listview adapter
        ArrayAdapter<Trip> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mHelper.getUserTrips());
        tripList.setAdapter(arrayAdapter);
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {

        // String our changed date
        String selectedDate = (month + 1) + " " + day + ", " + year;
        String formattedDate;

        // format our datetime
        try {
            SimpleDateFormat parseString = new SimpleDateFormat("M dd, yyyy", Locale.US);
            Date parseDate = parseString.parse(selectedDate);

            SimpleDateFormat formatDate = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
            formattedDate = formatDate.format(parseDate);

            // pass the data to the main activity
            onSelectedDateListener.setSelectedDate(formattedDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
