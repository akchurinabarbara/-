package com.example.myalarmclock;

import android.content.Context;
import android.telephony.mbms.MbmsErrors;

public class Managers {
    public static  SoundManager soundManager;

    public static void initialization(Context newContext){
        soundManager = new SoundManager(newContext);

    }
}
