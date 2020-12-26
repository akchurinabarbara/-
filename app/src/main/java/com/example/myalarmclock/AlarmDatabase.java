package com.example.myalarmclock;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = AlarmData.class, exportSchema = false, version = 1)
public abstract class AlarmDatabase extends RoomDatabase {
    private  static String DB_Name = "alarms_db";
    private static AlarmDatabase instance;

    public static synchronized AlarmDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), AlarmDatabase.class, DB_Name)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return  instance;
    }

    public  abstract  AlarmDAO alarmDAO();
}
