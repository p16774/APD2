package com.project3w.wharrynathan_tripjournal.Objects;

import java.util.Date;

/**
 * Created by Nate on 2/7/17.
 */

public class Trip {

    // object variables
    private String tripTitle, tripDesc;
    private Date tripStartDate, tripEndDate;

    public Trip(String _tripTitle, String _tripDesc, Date _tripStartDate, Date _tripEndDate) {
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

    public Date getTripStartDate() {
        return tripStartDate;
    }

    public Date getTripEndDate() {
        return tripEndDate;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "tripTitle='" + tripTitle + '\'' +
                ", tripDesc='" + tripDesc + '\'' +
                ", tripStartDate=" + tripStartDate +
                ", tripEndDate=" + tripEndDate +
                '}';
    }
}
