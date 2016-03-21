package com.garrytrue.tryopengl;

import android.app.ActivityManager;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private GLSurfaceView surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        surfaceView = (GLSurfaceView) findViewById(R.id.surfaceView);
        if(hasOpenGLES2())
        surfaceView.setEGLContextClientVersion(2);
        surfaceView.setPreserveEGLContextOnPause(true);
        surfaceView.setRenderer(new TextureRenderer());
        surfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        surfaceView.setOnTouchListener(touchListener);
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
    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    };
}
