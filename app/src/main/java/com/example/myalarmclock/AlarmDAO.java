package com.example.myalarmclock;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

//Доступ к данным о будильниках
@Dao
public interface AlarmDAO {
    @Query("SELECT * FROM alarms")
    List<AlarmData> getAll();

    @Insert
    void insert(AlarmData alarm);

    @Delete
    void delete(AlarmData alarm);

}
