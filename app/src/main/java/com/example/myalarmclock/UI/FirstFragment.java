package com.example.myalarmclock.UI;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.TimeZone;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
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
import com.example.myalarmclock.Location.UserLocationListener;
import com.example.myalarmclock.R;
import com.example.myalarmclock.adapter.AlarmAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.time4j.Moment;
import net.time4j.PlainDate;
import net.time4j.PlainTimestamp;
import net.time4j.calendar.astro.SolarTime;
import net.time4j.engine.CalendarDate;
import net.time4j.engine.ChronoFunction;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

public class FirstFragment extends Fragment {
    private FloatingActionButton mAddNewAlarm;
    private TextView mSunriseTimeText;
    private TextView mSunriseText;
    private TextView mSunsetTimeText;
    private TextView mSunsetText;
    private RecyclerView alarmsRecyclerView;
    private TextView mCoordinates;
    private ImageView mSunRiseImage;
    private ImageView mSunSetImage;

    private final int SunRise_image = 1;
    private final int SunSet_image = 2;

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

        mSunsetTimeText = (TextView) view.findViewById(R.id.sunsetTimeText);

        //Установка координат
        mCoordinates = (TextView) view.findViewById(R.id.coordinates);
        Location location = UserLocationListener.getUserLocation();
        mCoordinates.setText(formatLocation(location));


        //Обработка нажатия кнопки создания нового будильника
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

        //Установка изображений
        mSunRiseImage = (ImageView) view.findViewById(R.id.sunriseImage);
        mSunSetImage = (ImageView) view.findViewById(R.id.sunsetImage);

        //Обработка нажатия по изображениям и открытие галереи для выбора изображения
        mSunRiseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery(SunRise_image);
            }
        });

        //Обработка нажатия по изображениям и открытие галереи для выбора изображения
        mSunSetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery(SunSet_image);
            }
        });

        //Установка времени восхода и заката
        SolarTime solarTime = SolarTime.ofLocation(
                UserLocationListener.getUserLocation().getLatitude(),
                UserLocationListener.getUserLocation().getLongitude());

        SimpleDateFormat formatterTime = new SimpleDateFormat("hh:mm aa");

        Moment resultSunrise = PlainDate.nowInSystemTime().get(solarTime.sunrise());
        Moment resultSunset = PlainDate.nowInSystemTime().get(solarTime.sunset());

        PlainTimestamp sunrise = resultSunrise.toLocalTimestamp();
        PlainTimestamp sunset = resultSunset.toLocalTimestamp();

        mSunriseTimeText.setText(String.format("%02d:%02d",sunrise.getHour(),sunrise.getMinute()));
        mSunsetTimeText.setText(String.format("%02d:%02d",sunset.getHour(),sunset.getMinute()));

    }

    //Обрабатываем результат выбора в галерее:
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SunRise_image:
                if (resultCode == RESULT_OK) {
                    setImage(mSunRiseImage, imageReturnedIntent, getView().getContext());
                }
                break;
            case SunSet_image:
                if (resultCode == RESULT_OK) {
                    setImage(mSunSetImage, imageReturnedIntent, getView().getContext());
                }
                break;
        }
    }


    //Откратие галереи
    private void openGallery(int code) {
        //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        //Тип получаемых объектов - image:
        photoPickerIntent.setType("image/*");
        //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
        startActivityForResult(photoPickerIntent, code);
    }

    //Загрузка изображения
    private void setImage(ImageView imageView, Intent imageReturnedIntent, Context context) {
        try {
            //Получаем URI изображения, преобразуем его в Bitmap
            //объект и отображаем в элементе ImageView нашего интерфейса:
            final Uri imageUri = imageReturnedIntent.getData();
            final InputStream imageStream = context.getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            imageView.setImageBitmap(selectedImage);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //Формирование строки с информацией о местоположении
    private String formatLocation(Location location) {
        if (location == null)
            return "";
        return String.format(
                "Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3$tF %3$tT",
                location.getLatitude(), location.getLongitude(), new Date(
                        location.getTime()));
    }
}