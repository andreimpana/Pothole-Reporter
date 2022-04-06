package com.example.potholeapp;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class MapPoint implements Serializable {
    private String MapPointId;
    private String title;
    private String description;
    private double lat;
    private double lon;

    public MapPoint() {
        //public no-arg constructor needed
    }

    @Exclude
    public String getMapPointId() {
        return MapPointId;
    }

    public void setMapPointId(String documentId) {
        this.MapPointId = documentId;
    }

    public MapPoint(String title, String description, double lat, double lon) {
        this.title = title;
        this.description = description;
        this.lat = lat;
        this.lon = lon;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
    public double getLat() {
        return lat;
    }
    public double getLon() {
        return lon;
    }
}
