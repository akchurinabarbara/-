package com.example.myalarmclock;

import android.Manifest;
import android.app.Application;
import android.content.Context;

import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import com.example.myalarmclock.Location.UserLocationListener;

import net.time4j.android.ApplicationStarter;

public class App extends Application {
    public static  App instance;

    private AppDataBase dataBase;
    private int alarmsCount;

    final Context mainContext = this;

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationStarter.initialize(this, true);

        instance = this;

        //Подключение к БД
        dataBase = Room.databaseBuilder(mainContext, AppDataBase.class, "database")
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
