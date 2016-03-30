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
            int randX = randomLocation();
            int randY = randomLocation();
            Log.d(TAG, "run: [ X = " + randX + ", Y = " + randY + " ]");
            listener.onLocationChanged(randX, randY);
        }
    }
    public void stopEmulation(){
        Log.d(TAG, "stopEmulation: ");
        needEmulate = false;
    }
    private int randomLocation() {
//        int sign = random.nextInt(7) % 2;
//        Log.d(TAG, "randomLocation: " + sign);
//        float pos =  random.nextFloat() * 2 - 1;
        int rand = random.nextInt((65 - 10) + 1) + 10;
        return random.nextInt((65 - 10) + 1) + 10;
    }

}
