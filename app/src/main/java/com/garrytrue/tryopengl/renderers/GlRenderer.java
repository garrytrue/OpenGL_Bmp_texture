package com.garrytrue.tryopengl.renderers;

import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by garrytrue on 20.03.16.
 */
public abstract class GlRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = GlRenderer.class.getSimpleName();
    private boolean mFirstDraw;
    private boolean mSurfaceCreated;
    private int mWidth;
    private int mHeight;
    private long mLastTime;
    private int mFPS;


    public GlRenderer() {
        mFirstDraw = true;
        mSurfaceCreated = false;
        mWidth = -1;
        mHeight = -1;
        mLastTime = System.currentTimeMillis();
        mFPS = 0;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.d(TAG, "onSurfaceCreated() called with: " + "gl = [" + gl + "], config = [" + config + "]");
        mSurfaceCreated = true;
        mWidth = mHeight = -1;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (!mSurfaceCreated && mWidth == width && mHeight == height) {
            Log.d(TAG, "onSurfaceChanged: but not handled");
            return;
        }
        mWidth = width;
        mHeight = height;

        onCreate(mWidth, mHeight, mSurfaceCreated);
        mSurfaceCreated = false;

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        onDrawFrame(mFirstDraw);
//        fps count
        mFPS++;
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastTime >= 1000) {
            mFPS = 0;
            mLastTime = currentTime;
        }
        if (mFirstDraw) {
            mFirstDraw = false;
        }
    }

    public int getFps() {
        return mFPS;
    }

    public abstract void onCreate(int width, int height, boolean contextLost);

    public abstract void onDrawFrame(boolean firsDraw);
}
