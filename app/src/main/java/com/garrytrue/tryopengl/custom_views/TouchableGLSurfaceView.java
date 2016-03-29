package com.garrytrue.tryopengl.custom_views;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.garrytrue.tryopengl.actions.GLMapAction;
import com.garrytrue.tryopengl.actions.GLUserAction;
import com.garrytrue.tryopengl.helpers.LocationListener;
import com.garrytrue.tryopengl.helpers.UserLocationEmulator;
import com.garrytrue.tryopengl.renderers.TextureRenderer;
import com.garrytrue.tryopengl.utils.TextureUtils;

/**
 * Created by tiv on 21.03.2016.
 */
public class TouchableGLSurfaceView extends GLSurfaceView implements GestureDetector.OnDoubleTapListener, GestureDetector.OnGestureListener {
    private static final String TAG = TouchableGLSurfaceView.class.getSimpleName();
    private static boolean isNeedReset = false;
    private TextureRenderer renderer;
    private GestureDetectorCompat gestureDetector;
    private UserLocationEmulator locationEmulator;

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
        gestureDetector.onTouchEvent(event);
        return true;
    }

    private void init() {
        Log.d(TAG, "init: ");
        renderer = new TextureRenderer();
        setEGLContextClientVersion(2);
        setPreserveEGLContextOnPause(true);
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        gestureDetector = new GestureDetectorCompat(getContext(), this);
        gestureDetector.setOnDoubleTapListener(this);
        startLocationEmulation();
    }
    private void startLocationEmulation(){
       locationEmulator = new UserLocationEmulator(new LocationListener() {
            @Override
            public void onLocationChanged(float x, float y) {
                renderer.setAction(GLUserAction.makeAction(GLUserAction.ON_USER_LOCATION_ACTION, x, y));
                requestRender();
            }
        });
        locationEmulator.start();
    }

    public TextureRenderer getRenderer() {
        return renderer;
    }


    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.d(TAG, "onSingleTapConfirmed() called with: " + "e = [" + e + "]");
        final float normalizedX = TextureUtils.getNormalizedX(e.getX(), getWidth());
        final float normalizedY = TextureUtils.getNormalizedY(e.getY(), getHeight());
        renderer.setAction(GLUserAction.makeAction(GLUserAction.ON_USER_LOCATION_ACTION, normalizedX, normalizedY));
        requestRender();
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.d(TAG, "onDoubleTap() called with: " + "e = [" + e + "]");
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        Log.d(TAG, "onDoubleTapEvent() called with: " + "e = [" + e + "]");
        if (e.getActionMasked() == MotionEvent.ACTION_UP) {
            final float normalizedX = TextureUtils.getNormalizedX(e.getX(), getWidth());
            final float normalizedY = TextureUtils.getNormalizedY(e.getY(), getHeight());
            renderer.setAction(GLMapAction.makeMapAction(GLMapAction.SCALE_ACTION, normalizedX, normalizedY, isNeedReset));
            isNeedReset = !isNeedReset;
            locationEmulator.stopEmulation();
            requestRender();
        }
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d(TAG, "onDown() called with: " + "e = [" + e + "]");
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d(TAG, "onSingleTapUp() called with: " + "e = [" + e + "]");
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d(TAG, "onScroll() called with: " + "e1 = [" + e1 + "], e2 = [" + e2 + "], distanceX = [" + distanceX + "], distanceY = [" + distanceY + "]");
        if (e2.getActionMasked() == MotionEvent.ACTION_MOVE) {
            renderer.setAction(GLMapAction.makeMapAction(GLMapAction.MOVE_ACTION,
                    TextureUtils.getNormalizedX(e2.getX(), getWidth()),
                    TextureUtils.getNormalizedY(e2.getY(), getHeight()), false));
        }
        requestRender();
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return true;
    }
}
