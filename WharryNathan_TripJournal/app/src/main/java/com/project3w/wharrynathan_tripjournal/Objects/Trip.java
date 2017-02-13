package com.project3w.wharrynathan_tripjournal.Objects;

import java.util.Date;

/**
 * Created by Nate on 2/7/17.
 */

public class Trip {

    // object variables
    private String tripTitle, tripDesc;
    private long tripStartDate, tripEndDate;

    public Trip() {
        // empty constructor for Firebase
    }

    public Trip(String _tripTitle, String _tripDesc, long _tripStartDate, long _tripEndDate) {
        this.tripTitle = _tripTitle;
        this.tripDesc = _tripDesc;
        this.tripStartDate = _tripStartDate;
        this.tripEndDate = _tripEndDate;
    }

    public String getTripTitle() {
        return tripTitle;
    }

    public String getTripDesc() {
        return tripDesc;
    }

    public long getTripStartDate() {
        return tripStartDate;
    }

    public long getTripEndDate() {
        return tripEndDate;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "tripTitle='" + tripTitle + '\'' +
                ", tripDesc='" + tripDesc + '\'' +
                '}';
    }
}
