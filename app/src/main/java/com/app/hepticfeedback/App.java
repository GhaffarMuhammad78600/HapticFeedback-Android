package com.app.hepticfeedback;

import android.app.Application;
import android.content.Context;
import android.os.Vibrator;

public class App extends Application {

    public static Context context;


    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();




    }
}
