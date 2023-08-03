package com.gemecode.metroapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Stations")
public class Stations {

    @PrimaryKey(autoGenerate = true)
    public int StationID;

    public String StationNameEN, StationNameAR;

    public double Latitude, Longitude;
}
