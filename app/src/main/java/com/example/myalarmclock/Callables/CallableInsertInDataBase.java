package com.example.myalarmclock.Callables;

import com.example.myalarmclock.AlarmDAO;
import com.example.myalarmclock.AlarmData;
import com.example.myalarmclock.App;
import com.example.myalarmclock.AppDataBase;

import java.util.concurrent.Callable;

//Асинхронное добавление в базу данных
public class CallableInsertInDataBase implements Callable<Integer> {

    private final AlarmData data;

    public CallableInsertInDataBase(AlarmData data){
        this.data = data;
    }

    @Override
    public Integer call() {
        AppDataBase appDB = App.getInstance().getDataBase();
        AlarmDAO alarmDAO = appDB.alarmDAO();
        alarmDAO.insert(data);
        return 0;
    }
}
