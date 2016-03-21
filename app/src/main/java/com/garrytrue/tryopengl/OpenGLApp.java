package com.garrytrue.tryopengl;

import android.app.Application;
import android.content.Context;

/**
 * Created by tiv on 21.03.2016.
 */
public class OpenGLApp extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
    public static Context getContext(){
        return context;
    }
}
