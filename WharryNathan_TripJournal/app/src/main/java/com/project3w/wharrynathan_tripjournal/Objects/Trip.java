package com.project3w.wharrynathan_tripjournal.Objects;

import java.io.Serializable;

/**
 * Created by Nate on 2/7/17.
 */

public class Trip implements Serializable {

    // object variables
    private String tripId, tripTitle, tripDesc;
    private long tripStartDate, tripEndDate;

    public Trip() {
        // empty constructor for Firebase
    }

    public Trip(String _tripId, String _tripTitle, String _tripDesc, long _tripStartDate, long _tripEndDate) {
        this.tripId = _tripId;
        this.tripTitle = _tripTitle;
        this.tripDesc = _tripDesc;
        this.tripStartDate = _tripStartDate;
        this.tripEndDate = _tripEndDate;
    }

    public String getTripId() { return tripId; }

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
        return tripTitle;
    }
}
