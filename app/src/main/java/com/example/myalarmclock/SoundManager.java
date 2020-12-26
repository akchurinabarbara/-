package com.example.myalarmclock;

import android.content.Context;
import android.media.MediaPlayer;

//Менеджер работы со звуком
public class SoundManager {

    MediaPlayer acceptanceSound;
    MediaPlayer rejectionSound;
    Context context;

    public SoundManager(Context newContext) {
        context = newContext;
        acceptanceSound = MediaPlayer.create(newContext, R.raw.acceptance);
        rejectionSound = MediaPlayer.create(newContext, R.raw.rejection);
    }

    //Воиспроизведение звука удачного завершения
    public void playAccceptanceSound(){
        acceptanceSound.start();
    }

    //Воиспроизвеение звука неудачного зарешения
    public void playRejectionSound(){
        rejectionSound.start();
    }

    //Воиспроизведение звука мелодии
    public void play(int id){
        MediaPlayer player = MediaPlayer.create(context, id);
        player.start();
    }

}
