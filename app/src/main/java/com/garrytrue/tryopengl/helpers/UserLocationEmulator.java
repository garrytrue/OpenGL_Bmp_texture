package com.garrytrue.tryopengl.helpers;

import android.os.SystemClock;
import android.util.Log;

import java.util.Random;

/**
 * Created by tiv on 28.03.2016.
 */
public class UserLocationEmulator extends Thread {
    private static final String TAG = UserLocationEmulator.class.getSimpleName();
    LocationListener listener;
    private boolean needEmulate = true;
    Random random = new Random();

    public UserLocationEmulator(LocationListener listener) {
        this.listener = listener;
    }



    @Override
    public void run() {
        while (needEmulate) {
            SystemClock.sleep(5000);
            listener.onLocationChanged(randomLocation(), randomLocation());
        }
    }
    public void stopEmulation(){
        needEmulate = false;
        interrupt();
    }
    private float randomLocation(){
        int sign = random.nextInt(7) % 2;
        Log.d(TAG, "randomLocation: " + sign);
        float pos =  random.nextFloat() * 2 - 1;
     return sign == 0 ? pos : pos;
    }
}
