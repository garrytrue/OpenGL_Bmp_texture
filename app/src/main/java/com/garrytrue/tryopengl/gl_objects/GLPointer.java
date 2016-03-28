package com.garrytrue.tryopengl.gl_objects;

import android.opengl.Matrix;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;

/**
 * Created by tiv on 25.03.2016.
 */
public class GLPointer implements GLObject {
    private float currentX;
    private float currentY;

    public float getCurrentX() {
        return currentX;
    }

    public float getCurrentY() {
        return currentY;
    }
    public void updatePointerPos(float x, float y){
        currentX = x;
        currentY = y;
    }

    @Override
    public void drawObject() {
        glDrawArrays(GL_TRIANGLES, 4, 3);
    }
}
