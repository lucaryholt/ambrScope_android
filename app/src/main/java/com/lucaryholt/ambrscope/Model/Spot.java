package com.lucaryholt.ambrscope.Model;

import java.util.Objects;
import java.util.UUID;

public class Spot {

    private String id;
    private String timeStamp;
    private double latitude;
    private double longitude;
    private String pictureID; //TODO maybe not needed
    private String chance;
    private String time;
    private String finderMethod;
    private boolean precise;
    private String description;

    public Spot() {
        this.id = UUID.randomUUID().toString();
    }

    public Spot(String id) {
        this.id = id;
    }

    public Spot(String timeStamp, double latitude, double longitude, String pictureID, String chance, String time, String finderMethod, boolean precise, String description) {
        this.id = UUID.randomUUID().toString();
        this.timeStamp = timeStamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pictureID = pictureID;
        this.chance = chance;
        this.time = time;
        this.finderMethod = finderMethod;
        this.precise = precise;
        this.description = description;
    }

    public Spot(String id, String timeStamp, double latitude, double longitude, String pictureID, String chance, String time, String finderMethod, boolean precise, String description) {
        this.id = id;
        this.timeStamp = timeStamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pictureID = pictureID;
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

    public String getPictureID() {
        return pictureID;
    }

    public void setPictureID(String pictureID) {
        this.pictureID = pictureID;
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

    public String toString() {
        return "Spot{" +
                "id='" + id + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", picture=" + pictureID +
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
