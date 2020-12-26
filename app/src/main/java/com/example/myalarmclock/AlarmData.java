package com.example.myalarmclock;

import android.media.MediaPlayer;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "alarms")
public class AlarmData {
    //id будильника
    @PrimaryKey(autoGenerate = true)
    public int id;
    //Время воиспроизведения буильника
    @ColumnInfo(name = "time")
    long triggerAtMillis;
    //Мелодия будильника
    @ColumnInfo(name = "sound")
    String sound;
    //Включен ли будильник
    @ColumnInfo(name = "isOn")
    int isOn;

    public AlarmData(int id, long triggerAtMillis, String sound, int isOn){
        this.id = id;
        this.triggerAtMillis = triggerAtMillis;
        this.sound = sound;
        this.isOn = isOn;
    }

    @Ignore
    public AlarmData(long triggerAtMillis, String sound, int isOn){
        this.triggerAtMillis = triggerAtMillis;
        this.sound = sound;
        this.isOn = isOn;
    }
}
