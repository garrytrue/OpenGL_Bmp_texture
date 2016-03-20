package com.garrytrue.tryopengl;

import android.opengl.GLES20;
import android.util.Log;

/**
 * Created by garrytrue on 20.03.16.
 */
public class ImageRenderer extends GlRenderer {
    private static final String TAG = ImageRenderer.class.getSimpleName();

    @Override
    public void onCreate(int width, int height, boolean contextLost) {
        Log.d(TAG, "onCreate() called with: " + "width = [" + width + "], height = [" + height + "], contextLost = [" + contextLost + "]");
        GLES20.glClearColor(0.5f, 0.7f, 0f, 0f);
        GLES20.glViewport(0,0, width, height);
    }

    @Override
    public void onDrawFrame(boolean firsDraw) {
        Log.d(TAG, "onDrawFrame() called with: " + "firsDraw = [" + firsDraw + "]");
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT
                | GLES20.GL_DEPTH_BUFFER_BIT);

    }
}
