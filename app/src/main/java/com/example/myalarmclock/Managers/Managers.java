package com.example.myalarmclock.Managers;

import android.content.Context;

import com.example.myalarmclock.Sound.SoundManager;

public class Managers {
    public static SoundManager soundManager;

    public static void initialization(Context newContext){
        soundManager = new SoundManager(newContext);

    }
}
