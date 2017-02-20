package com.project3w.wharrynathan_tripjournal.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import com.project3w.wharrynathan_tripjournal.Objects.Trip;
import com.project3w.wharrynathan_tripjournal.R;
import com.project3w.wharrynathan_tripjournal.ViewTripActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.project3w.wharrynathan_tripjournal.AllTripsActivity.EXTRA_TRIP_ARRAY;

/**
 * Created by Nate on 2/6/17.
 */

public class AllTripsFragment extends Fragment implements CalendarView.OnDateChangeListener, AdapterView.OnItemClickListener {

    // class variables
    CalendarView tripCalendar;
    ListView tripList;
    Activity mActivity;
    ArrayList<Trip> userTripsArray;

    // string variable
    public static final String EXTRA_SELECTED_TRIP = "com.project3w.wharrynathan_tripjournal.EXTRA_SELECTED_TRIP";

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

        Bundle tripArray = getArguments();
        userTripsArray = (ArrayList<Trip>) tripArray.getSerializable(EXTRA_TRIP_ARRAY);

        try {
            onSelectedDateListener = (SelectedDateListener) mActivity;
        } catch (ClassCastException e) {
            throw new ClassCastException(mActivity.toString() + " must implement OnSelectedEventListener");
        }

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
        ArrayAdapter<Trip> arrayAdapter = new ArrayAdapter<>(mActivity, android.R.layout.simple_list_item_1, userTripsArray);
        tripList.setOnItemClickListener(this);
        tripList.setAdapter(arrayAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // get our selected trip
        Trip selectedTrip = userTripsArray.get(position);

        System.out.println(selectedTrip + " this is the selectedTrip");

        // create intent and pass in bundle
        Intent viewTripIntent = new Intent(getActivity(), ViewTripActivity.class);
        viewTripIntent.putExtra(EXTRA_SELECTED_TRIP, selectedTrip);
        startActivity(viewTripIntent);

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
