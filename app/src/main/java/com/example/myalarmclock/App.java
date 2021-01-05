package com.example.myalarmclock;

import android.app.Application;

import androidx.room.Room;

public class App extends Application {
    public static  App instance;

    private AppDataBase dataBase;
    private int alarmsCount;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        dataBase = Room.databaseBuilder(this, AppDataBase.class, "database")
                .build();
    }

    public  static  App getInstance(){
        return instance;
    }

    public AppDataBase getDataBase(){
        return  dataBase;
    }

    public int getAlarmsCount() {
        return alarmsCount;
    }

    public void setAlarmsCount(int alarmsCount) {
        this.alarmsCount = alarmsCount;
    }
}
