package com.lucaryholt.ambrscope.Model;

import android.graphics.Bitmap;

import java.util.Objects;
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

    public Spot() {
        this.id = UUID.randomUUID().toString();
    }

    public Spot(String id) {
        this.id = id;
    }

    public Spot(String userID, String timeStamp, double latitude, double longitude, String chance, String time, String finderMethod, boolean precise, String description, Bitmap bitmap) {
        this.userID = userID;
        this.timeStamp = timeStamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.chance = chance;
        this.time = time;
        this.finderMethod = finderMethod;
        this.precise = precise;
        this.description = description;
        this.bitmap = bitmap;
    }

    public Spot(String id, String userID, String timeStamp, double latitude, double longitude, String chance, String time, String finderMethod, boolean precise, String description) {
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

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getChance() {
        return chance;
    }

    public void setChance(String chance) {
        this.chance = chance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFinderMethod() {
        return finderMethod;
    }

    public void setFinderMethod(String finderMethod) {
        this.finderMethod = finderMethod;
    }

    public boolean isPrecise() {
        return precise;
    }

    public void setPrecise(boolean precise) {
        this.precise = precise;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String toString() {
        return "Spot{" +
                "id='" + id + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", chance=" + chance +
                ", time='" + time + '\'' +
                ", finderMethod=" + finderMethod +
                ", precise=" + precise +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Spot spot = (Spot) o;
        return id.equals(spot.id);
    }
}
