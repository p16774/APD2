package com.project3w.wharrynathan_tripjournal.Objects;

import java.io.Serializable;

/**
 * Created by Nate on 2/7/17.
 */

public class TripItem implements Serializable {

    // class variables
    private String tripId, itemImageUri, itemTitle, itemDesc;
    private String imageName;
    private long timeStamp;

    public TripItem() {
        // empty constructor for Firebase
    }

    public TripItem(String _itemImageUrl, String _itemTitle, String _itemDesc, long _timeStamp) {
        this.itemImageUri = _itemImageUrl;
        this.itemTitle = _itemTitle;
        this.itemDesc = _itemDesc;
        this.timeStamp = _timeStamp;
    }

    public String getItemImageUri() {
        return itemImageUri;
    }

    public void setTripId(String _tripId) {
        this.tripId = _tripId;
    }

    public String getTripId() {
        return tripId;
    }

    public void setItemImageUri(String itemImageUri) {
        this.itemImageUri = itemImageUri;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return itemTitle;
    }
}
