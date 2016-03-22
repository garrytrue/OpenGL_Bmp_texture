package com.garrytrue.tryopengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by tiv on 21.03.2016.
 */
public class TouchableGLSurfaceView extends GLSurfaceView {
    private static final String TAG = TouchableGLSurfaceView.class.getSimpleName();
    private static int clickFactor = 0;
    private AtomicBoolean isFirstClick = new AtomicBoolean(true);
    private TextureRenderer renderer;

    public TouchableGLSurfaceView(Context context) {
        super(context);
        init();
    }

    public TouchableGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent() called with: " + "event = [" + event + "]");
        if (event.getAction() == MotionEvent.ACTION_UP) {
            makeClickAction();
        }
        requestRender();
        return true;
    }

    private void init() {
        Log.d(TAG, "init: ");
        renderer = new TextureRenderer();
        setEGLContextClientVersion(2);
        setPreserveEGLContextOnPause(true);
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public TextureRenderer getRenderer() {
        return renderer;
    }

    private void makeClickAction() {
        Log.d(TAG, "makeClickAction: IsFirstClick "+ isFirstClick);
        clickFactor ++;
        queueEvent(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "makeClickAction: IsFirstClick "+ isFirstClick);
                renderer.zoomImage(clickFactor > 3, isFirstClick.get());
                if(isFirstClick.get()){
                    isFirstClick.getAndSet(false);
                }
            }
        });
        if(clickFactor > 4){
            clickFactor = 0;
            isFirstClick.getAndSet(true);
        }
    }
}
