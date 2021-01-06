package com.example.myalarmclock;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

//Доступ к данным о будильниках
@Dao
public interface AlarmDAO {
    @Query("SELECT * FROM alarms ORDER BY time")
    List<AlarmData> getAll();

    @Insert
    void insert(AlarmData alarm);

    @Update
    void update(AlarmData alarm);

    @Delete
    void delete(AlarmData alarm);

}
