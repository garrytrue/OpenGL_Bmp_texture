package com.garrytrue.tryopengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by tiv on 21.03.2016.
 */
public class TouchableGLSurfaceView extends GLSurfaceView {
    private static final String TAG = TouchableGLSurfaceView.class.getSimpleName();
    private static int clickFactor = 0;
    private AtomicBoolean isFirstClick = new AtomicBoolean(true);
    private MotionEvent previosEvent;
    private TextureRenderer renderer;
    private float previousX, previousY;
    private GestureDetector gestureDetector;

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


        if(previosEvent != null && previosEvent.getAction() == MotionEvent.ACTION_MOVE){
            previosEvent = null;
            return true;
        }
        if(event.getAction() == MotionEvent.ACTION_MOVE){
//            if(Math.abs(previousX - normalizedX) > 0.001f & Math.abs(previousY - normalizedY) > 0.001f ){
//                Log.d(TAG, "onTouchEvent: prX " + previousX + " prY " + previousY);
//                previousX = normalizedX;
//                previousY = normalizedY;
//                makeMoveAction(previousX, previousY);
//                requestRender();
//            }
            makeMoveAction(normalizedX, normalizedY, clickFactor);
            requestRender();
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP && previosEvent != null) {
            makeClickAction(normalizedX, normalizedY);
        }
        requestRender();
        if(previosEvent != event){
            previosEvent = event;
        }
        Log.d(TAG, "onTouchEvent: Previous Event " + previosEvent.getAction());
//        gestureDetector.onTouchEvent(event);
//        requestRender();
        return true;
    }

    private void init() {
        Log.d(TAG, "init: ");
        renderer = new TextureRenderer();
        setEGLContextClientVersion(2);
        setPreserveEGLContextOnPause(true);
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        gestureDetector = new GestureDetector(getContext(), new GlGestureListener());
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
    private void makeMoveAction(final float moveToX, final float moveToY, final int zoom){
        Log.d(TAG, "makeMoveAction() called with: " + "moveToX = [" + moveToX + "], moveToY = [" + moveToY + "]");
        queueEvent(new Runnable() {
            @Override
            public void run() {
                renderer.moveTo(moveToX, moveToY, zoom);
            }
        });
    }
    private class GlGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.d(TAG, "onScroll() called with: " + "e1 = [" + e1 + "], e2 = [" + e2 + "], distanceX = [" + distanceX + "], distanceY = [" + distanceY + "]");
//            makeMoveAction(TextureUtils.getNormalizedX(distanceX, getWidth()), TextureUtils.getNormalizedY(distanceY, getHeight()));
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }
}
