package com.garrytrue.tryopengl.utils;

import android.graphics.Bitmap;
import android.opengl.GLUtils;
import android.util.Log;

import static android.opengl.GLES20.GL_FALSE;
import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glTexParameteri;

/**
 * Created by tiv on 21.03.2016.
 */
public class TextureUtils {
    private static final String TAG = TextureUtils.class.getSimpleName();


    public static int loadTexture() {
        final int[] textureId = new int[1];
        glGenTextures(1, textureId, 0);
        if (textureId[0] == GL_FALSE) {
            Log.d(TAG, "loadTexture: Error ");
            return textureId[0];
        }
        Bitmap bmp = ImageUtils.loadFromAsserts();
        if (bmp == null) {
            Log.d(TAG, "loadTexture: Failed to load BMP ");
            glDeleteTextures(1, textureId, 0);
            return GL_FALSE;
        }
        Log.d(TAG, "loadTexture: BMP successfully load");
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureId[0]);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bmp, 0);

        bmp.recycle();

        // сброс target
        glBindTexture(GL_TEXTURE_2D, 0);

        return textureId[0];
    }

    public static float getNormalizedX(float eventX, int viewWidth) {
        return (eventX / (float) viewWidth) * 2 - 1;
    }
    public static float getNormalizedY(float eventY, int viewHeight) {
        return -((eventY / (float) viewHeight) * 2 - 1);
    }
}
