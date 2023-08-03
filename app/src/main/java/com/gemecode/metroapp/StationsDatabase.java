package com.gemecode.metroapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Stations.class}, version = 2)
public abstract class StationsDatabase extends RoomDatabase {
    public abstract StationsDAO stationsDAO();

    private static final String DATABASE_NAME = "stations.db";
    private static StationsDatabase ourInstance;

    public static StationsDatabase getInstance(Context context){

        if (ourInstance == null){
            ourInstance = Room.databaseBuilder(context,
                            StationsDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .createFromAsset("databases/stations.db")
                    .allowMainThreadQueries()
                    .build();
        }
        return ourInstance;

    }
}
