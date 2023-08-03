package com.gemecode.metroapp;

import androidx.room.Dao;
import androidx.room.Query;
import java.util.List;

@Dao
public interface StationsDAO {

    @Query("SELECT StationNameEN FROM Stations")
    List<String> selectAllStationsEN();

    @Query("SELECT StationNameAR FROM Stations")
    List<String> selectAllStationsAR();

    @Query("SELECT Latitude FROM Stations")
    List<Double> selectAllLatitude();

    @Query("SELECT Longitude FROM Stations")
    List<Double> selectAllLongitude();



    @Query("SELECT StationNameEN FROM Stations WHERE StationID BETWEEN 1 AND 35")
    List<String> selectLine1EN();

    @Query("SELECT StationNameEN FROM Stations WHERE StationID BETWEEN 36 AND 55")
    List<String> selectLine2EN();

    @Query("SELECT StationNameEN FROM Stations WHERE StationID BETWEEN 56 AND 89")
    List<String> selectLine3EN();


    @Query("SELECT StationNameAR FROM Stations WHERE StationID BETWEEN 1 AND 35")
    List<String> selectLine1AR();

    @Query("SELECT StationNameAR FROM Stations WHERE StationID BETWEEN 36 AND 55")
    List<String> selectLine2AR();

    @Query("SELECT StationNameAR FROM Stations WHERE StationID BETWEEN 56 AND 89")
    List<String> selectLine3AR();


}
