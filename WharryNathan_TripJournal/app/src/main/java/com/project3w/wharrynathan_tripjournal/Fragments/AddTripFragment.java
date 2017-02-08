package com.project3w.wharrynathan_tripjournal.Fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.project3w.wharrynathan_tripjournal.AddTripActivity;
import com.project3w.wharrynathan_tripjournal.Objects.Trip;
import com.project3w.wharrynathan_tripjournal.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nate on 2/7/17.
 */

public class AddTripFragment extends Fragment implements AddTripActivity.OnSaveTripListener {

    EditText tripTitleView, tripDescView, tripStartDateView, tripEndDateView;
    Calendar myCalendar = Calendar.getInstance();

    public AddTripFragment() {
        // empty constructor
    }

    @Override
    public Trip getSavedTrip() {
        // pull the entered values to save
        String tripTitle, tripDesc, tripStartString, tripEndString;
        Date tripStartDate = null, tripEndDate = null;

        tripTitle = tripTitleView.getText().toString();
        tripDesc = tripDescView.getText().toString();
        tripStartString = tripStartDateView.getText().toString();
        tripEndString = tripEndDateView.getText().toString();

        // format our dates
        try {
            SimpleDateFormat formatDate = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
            tripStartDate = formatDate.parse(tripStartString);
            tripEndDate = formatDate.parse(tripEndString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // validate we have dates set then return our trip
        if (tripStartDate != null && tripEndDate != null) {
            return new Trip(tripTitle, tripDesc, tripStartDate, tripEndDate);
        }

        return null;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_trip, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tripTitleView = (EditText) getActivity().findViewById(R.id.trip_title);
        tripDescView = (EditText) getActivity().findViewById(R.id.trip_desc);
        tripStartDateView = (EditText) getActivity().findViewById(R.id.trip_start);
        tripEndDateView = (EditText) getActivity().findViewById(R.id.trip_end);


        // create our onClick datepickers and formatters
        final DatePickerDialog.OnDateSetListener startDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(tripStartDateView);
            }

        };

        final DatePickerDialog.OnDateSetListener endDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(tripEndDateView);
            }

        };

        tripStartDateView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), startDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        tripEndDateView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), endDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel(EditText dateView) {

        String myFormat = "MMMM dd, yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateView.setText(sdf.format(myCalendar.getTime()));
    }
}
