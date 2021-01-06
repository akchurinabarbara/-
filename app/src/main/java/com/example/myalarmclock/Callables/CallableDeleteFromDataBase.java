package com.example.myalarmclock.Callables;

import com.example.myalarmclock.AlarmDAO;
import com.example.myalarmclock.AlarmData;
import com.example.myalarmclock.App;
import com.example.myalarmclock.AppDataBase;

import java.util.concurrent.Callable;

//Асинхронное удаление из БД
public class CallableDeleteFromDataBase implements Callable<Integer> {

    private final AlarmData data;

    public CallableDeleteFromDataBase(AlarmData data){
        this.data = data;
    }

    @Override
    public Integer call() {
        App.getInstance().setAlarmsCount(data.id);
        AppDataBase appDB = App.getInstance().getDataBase();
        AlarmDAO alarmDAO = appDB.alarmDAO();
        alarmDAO.delete(data);
        return 0;
    }
}