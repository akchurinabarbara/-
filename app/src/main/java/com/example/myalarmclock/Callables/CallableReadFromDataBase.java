package com.example.myalarmclock.Callables;

import com.example.myalarmclock.AlarmDAO;
import com.example.myalarmclock.AlarmData;
import com.example.myalarmclock.App;
import com.example.myalarmclock.AppDataBase;

import java.util.List;
import java.util.concurrent.Callable;

//Асинхронное чтение из базы
public class CallableReadFromDataBase implements Callable<List<AlarmData>> {

    public CallableReadFromDataBase(){
    }

    @Override
    public List<AlarmData> call() {
        AppDataBase appDB = App.getInstance().getDataBase();
        AlarmDAO alarmDAO = appDB.alarmDAO();
        return alarmDAO.getAll();
    }
}
