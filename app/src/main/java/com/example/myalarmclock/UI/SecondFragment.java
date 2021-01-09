package com.example.myalarmclock.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myalarmclock.AlarmData;
import com.example.myalarmclock.App;
import com.example.myalarmclock.Callables.CallableInsertInDataBase;
import com.example.myalarmclock.Location.UserLocationListener;
import com.example.myalarmclock.Managers.Managers;
import com.example.myalarmclock.R;
import com.google.android.material.snackbar.Snackbar;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SecondFragment extends Fragment {
    private Button mAddMusicButton;
    private Button mSaveButton;

    private final int SelectMusic = 1;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAddMusicButton = (Button) view.findViewById(R.id.addMusicButton);
        mSaveButton = (Button) view.findViewById(R.id.saveButton);

        mAddMusicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery(SelectMusic);
                Managers.soundManager.playAccceptanceSound();
                Snackbar.make(view,R.string.addMusicSuccessful, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //Форимирование времени из введенных пользователем данных
                    EditText newTimeText = (EditText) getView().findViewById(R.id.newTime);

                    String str = newTimeText.getText().toString();
                    DateFormat formatter = new SimpleDateFormat("hh:mm");

                    Date dateTime = formatter.parse(str);

                    //Установка текущего времени пользователя
                    Date date = new Date(UserLocationListener.getUserLocation().getTime());
                    //если время меньше текущего - установить на след день
                    if (dateTime.getHours() < date.getHours() || dateTime.getMinutes() < date.getMinutes())
                    {
                        date.setDate(date.getDate() + 1);
                    }

                    //Установка введенного пользователем времени
                    date.setHours(dateTime.getHours());
                    date.setMinutes(dateTime.getMinutes());

                    //Создание нового будильника и запись его в БД
                    AlarmData alarm = new AlarmData(App.getInstance().getAlarmsCount() + 1, date.getTime(), "", 1);

                    //Асинхронное добавление в БД
                    Observable.fromCallable(new CallableInsertInDataBase(alarm))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<Integer>() {
                                @Override
                                public void accept(Integer result) throws Exception {
                                }
                            });


                            //Показ уведомления об успешном создании будильника
                    Managers.soundManager.playAccceptanceSound();
                    Snackbar.make(view, R.string.alarmCreateSuccessful, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    NavHostFragment.findNavController(SecondFragment.this)
                            .navigate(R.id.action_SecondFragment_to_FirstFragment);
                } catch (ParseException e) {
                    //Показ уведомления об неуспешном создании будильника
                    Managers.soundManager.playRejectionSound();
                    Snackbar.make(view, R.string.alarmCreateNotSuccessful, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    NavHostFragment.findNavController(SecondFragment.this)
                            .navigate(R.id.action_SecondFragment_to_FirstFragment);
                }
            }
        });
    }

    //Откратие галереи
    private void openGallery(int code) {
        //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        //Тип получаемых объектов - image:
        photoPickerIntent.setType("audio/*");
        //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
        startActivityForResult(photoPickerIntent, code);
    }
}