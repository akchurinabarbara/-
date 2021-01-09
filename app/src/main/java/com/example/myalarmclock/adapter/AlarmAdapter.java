package com.example.myalarmclock.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myalarmclock.AlarmData;
import com.example.myalarmclock.App;
import com.example.myalarmclock.Callables.CallableDeleteFromDataBase;
import com.example.myalarmclock.Callables.CallableInsertInDataBase;
import com.example.myalarmclock.Callables.CallableReadFromDataBase;
import com.example.myalarmclock.Callables.CallableUpdateInDataBase;
import com.example.myalarmclock.R;
import com.example.myalarmclock.UI.FirstFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

//Адаптер для работы с RecyclerView
public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>{

    private List<AlarmData> alarmDataList = new ArrayList<>();

    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alarm_list_element_layout, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
            holder.bind(alarmDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return alarmDataList.size();
    }

    public void setItems(Collection<AlarmData> alarms) {
        alarmDataList.addAll(alarms);
        //Перерисовка объектов на экране
        notifyDataSetChanged();
    }

    public void clearItems() {
        alarmDataList.clear();
        //Перерисовка объектов на экране
        notifyDataSetChanged();
    }

    class AlarmViewHolder extends RecyclerView.ViewHolder {
        private TextView alarmTime;
        private Switch alarmIsOn;
        private ImageButton alarmDelete;
        private TextView alarmDate;

        public AlarmViewHolder(View itemView) {
            super(itemView);
            //Получение объектов элемента списка
            alarmTime = itemView.findViewById(R.id.alarmTime);
            alarmIsOn = itemView.findViewById(R.id.switchOn);
            alarmDelete = itemView.findViewById(R.id.deleteAlarm);
            alarmDate = itemView.findViewById(R.id.alarmDate);
        }

        //Установка значений элемента списка
        public void bind(final AlarmData alarm) {
            DateFormat formatterTime = new SimpleDateFormat("hh:mm aa");
            DateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy");

            Date date = new Date(alarm.triggerAtMillis);

            String dateString = formatterTime.format(date);
            alarmTime.setText(dateString);
            dateString = formatterDate.format(date);
            alarmDate.setText(dateString);

            alarmIsOn.setChecked(alarm.isOn == 1? true : false);
            alarmIsOn.setText(alarm.isOn == 1? R.string.on : R.string.off);
            //Обработчик изменения переключателя
            alarmIsOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    updateAlarm(alarm);
                }
            });

            //Обработчик нажатия на кнопку удаления
            alarmDelete.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteAlarm(alarm);
                        }
                    }
            );
        }

        //Удаление будильника
        private void deleteAlarm (AlarmData alarm){
            //Асинхронное удаление будильника из БД
            Observable.fromCallable(new CallableDeleteFromDataBase(alarm))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer result) throws Exception {
                        }
                    });

            //Удаление будильника из списка отображаемых
            alarmDataList.remove(alarm);
            //Перерисовка объектов на экране
            notifyDataSetChanged();
        }

        //Обновление будильника
        private void updateAlarm (AlarmData alarm){
            alarm.isOn = alarm.isOn == 1? 0 : 1;
            alarmIsOn.setText(alarm.isOn == 1? R.string.on : R.string.off);

            //Асинхронное удаление будильника из БД
            Observable.fromCallable(new CallableUpdateInDataBase(alarm))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer result) throws Exception {
                        }
                    });

            //Перерисовка объектов на экране
            notifyDataSetChanged();
        }
    }
}
