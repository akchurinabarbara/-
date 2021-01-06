package com.example.myalarmclock.Callables;

import com.example.myalarmclock.AlarmDAO;
import com.example.myalarmclock.AlarmData;
import com.example.myalarmclock.App;
import com.example.myalarmclock.AppDataBase;

//Асинхронное обновление данных
import java.util.concurrent.Callable;

public class CallableUpdateInDataBase implements Callable<Integer> {

    private final AlarmData data;

    public CallableUpdateInDataBase (AlarmData data){
        this.data = data;
    }

    @Override
    public Integer call() {
        App.getInstance().setAlarmsCount(data.id);
        AppDataBase appDB = App.getInstance().getDataBase();
        AlarmDAO alarmDAO = appDB.alarmDAO();
        alarmDAO.update(data);
        return 0;
    }
}
