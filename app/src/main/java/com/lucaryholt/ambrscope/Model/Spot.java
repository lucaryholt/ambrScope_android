package com.lucaryholt.ambrscope.Model;

import android.graphics.Bitmap;

import java.util.UUID;

public class Spot {

    private String id;
    private String userID;
    private String timeStamp;
    private double latitude;
    private double longitude;
    private String chance;
    private String time;
    private String finderMethod;
    private boolean precise;
    private String description;
    private Bitmap bitmap;
    private String amount;
    private String additionalInfo;

    public Spot(String id) {
        this.id = id;
    }

    public Spot(String userID, String timeStamp, double latitude, double longitude, String chance, String time, String finderMethod, boolean precise, String description, String amount, String additionalInfo) {
        this.id = UUID.randomUUID().toString();
        this.userID = userID;
        this.timeStamp = timeStamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.chance = chance;
        this.time = time;
        this.finderMethod = finderMethod;
        this.precise = precise;
        this.description = description;
        this.amount = amount;
        this.additionalInfo = additionalInfo;
    }

    public Spot(String id, String userID, String timeStamp, double latitude, double longitude, String chance, String time, String finderMethod, boolean precise, String description, String amount, String additionalInfo) {
        this.id = id;
        this.userID = userID;
        this.timeStamp = timeStamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.chance = chance;
        this.time = time;
        this.finderMethod = finderMethod;
        this.precise = precise;
        this.description = description;
        this.amount = amount;
        this.additionalInfo = additionalInfo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getChance() {
        return chance;
    }

    public String getTime() {
        return time;
    }

    public String getFinderMethod() {
        return finderMethod;
    }

    public boolean isPrecise() {
        return precise;
    }

    public String getDescription() {
        return description;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getAmount() {
        return amount;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Spot spot = (Spot) o;
        return id.equals(spot.id);
    }
}
