package com.example.myalarmclock.UI;

import android.app.AlarmManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myalarmclock.AlarmData;
import com.example.myalarmclock.App;
import com.example.myalarmclock.Callables.CallableInsertInDataBase;
import com.example.myalarmclock.Callables.CallableReadFromDataBase;
import com.example.myalarmclock.R;
import com.example.myalarmclock.adapter.AlarmAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FirstFragment extends Fragment {
    private FloatingActionButton mAddNewAlarm;
    private TextView mSunriseTimeText;
    private TextView mSunriseText;
    private TextView mSunsetTimeText;
    private TextView mSunsetText;
    private RecyclerView alarmsRecyclerView;

    private AlarmAdapter alarmAdapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Инициализация всех компонентов
        alarmsRecyclerView = (RecyclerView) view.findViewById(R.id.alarmsRecyclerView);
        alarmsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        alarmAdapter = new AlarmAdapter();
        alarmsRecyclerView.setAdapter(alarmAdapter);

        mAddNewAlarm = (FloatingActionButton) view.findViewById(R.id.addAlarmFab);

        mSunriseTimeText = (TextView) view.findViewById(R.id.sunriseTimeText);
        mSunriseText = (TextView) view.findViewById(R.id.sunriseText);

        mSunsetTimeText = (TextView) view.findViewById(R.id.sunsetTimeText);
        mSunsetText = (TextView) view.findViewById(R.id.sunsetText);

        mAddNewAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        //Асинхронное чтение из БД
        Observable.fromCallable(new CallableReadFromDataBase())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<AlarmData>>() {
                    @Override
                    public void accept(List<AlarmData> alarmData) throws Exception {
                        App.getInstance().setAlarmsCount(alarmData.size());
                        alarmAdapter.clearItems();
                        alarmAdapter.setItems(alarmData);
                    }
                });
    }

}