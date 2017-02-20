package com.project3w.wharrynathan_tripjournal.Objects;

import java.io.Serializable;

/**
 * Created by Nate on 2/7/17.
 */

public class TripItem implements Serializable {

    // class variables
    private String itemImageUrl, itemTitle, itemDesc;
    private long timeStamp;

    public TripItem() {
        // empty constructor for Firebase
    }

    public TripItem(String _itemImageUrl, String _itemTitle, String _itemDesc, long _timeStamp) {
        this.itemImageUrl = _itemImageUrl;
        this.itemTitle = _itemTitle;
        this.itemDesc = _itemDesc;
        this.timeStamp = _timeStamp;
    }

    public String getItemImageUrl() {
        return itemImageUrl;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public long getTimeStamp() { return timeStamp; }

    @Override
    public String toString() {
        return itemTitle;
    }
}
