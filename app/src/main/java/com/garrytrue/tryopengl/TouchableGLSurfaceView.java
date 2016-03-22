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
        final float normalizedX = TextureUtils.getNormalizedX(event.getX(), getWidth());
        final float normalizedY = TextureUtils.getNormalizedY(event.getY(), getHeight());
        if (event.getAction() == MotionEvent.ACTION_UP) {
            makeClickAction(normalizedX, normalizedY);
        }
        if(event.getAction() == MotionEvent.ACTION_MOVE){
            Log.d(TAG, "onTouchEvent: MOVE_ACTION");
            Log.d(TAG, "onTouchEvent: NormalizedX " + TextureUtils.getNormalizedX(event.getX(), getWidth()));
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

    private void makeClickAction(final float actionX, final float actionY) {
        Log.d(TAG, "makeClickAction: IsFirstClick "+ isFirstClick);
        Log.d(TAG, "makeClickAction() called with: " + "actionX = [" + actionX + "], actionY = [" + actionY + "]");
        clickFactor ++;
        queueEvent(new Runnable() {
            @Override
            public void run() {
                renderer.zoomImage(clickFactor > 3, isFirstClick.get(), actionX, actionY);
                if(isFirstClick.get()){
                    isFirstClick.compareAndSet(true, false);
                }
                if(clickFactor > 3){
                    clickFactor = 0;
                    isFirstClick.compareAndSet(false, true);
                }
                Log.d(TAG, "makeClickAction: IsFirstClick "+ isFirstClick);
                Log.d(TAG, "makeClickAction() called with: " + "actionX = [" + actionX + "], actionY = [" + actionY + "]");
                Log.d(TAG, "run: ClickFactor " + clickFactor);
            }
        });
    }
}
