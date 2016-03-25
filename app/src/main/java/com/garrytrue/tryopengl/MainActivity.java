package com.garrytrue.tryopengl;

import android.app.ActivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.garrytrue.tryopengl.custom_views.TouchableGLSurfaceView;

public class MainActivity extends AppCompatActivity {
    private TouchableGLSurfaceView surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        surfaceView = (TouchableGLSurfaceView) findViewById(R.id.surfaceView);
    }
    private boolean hasOpenGLES2(){
        return ((ActivityManager) getSystemService(ACTIVITY_SERVICE)).getDeviceConfigurationInfo().reqGlEsVersion >= 0x20000;
    }

    @Override
    protected void onResume() {
        super.onResume();
        surfaceView.onResume();
    }

    @Override
    protected void onPause() {
        surfaceView.onPause();
        super.onPause();
    }
}
