package com.garrytrue.tryopengl.gl_objects;

import android.opengl.Matrix;

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
    public void drawObject(float[] matrix) {
        Matrix.translateM(matrix,0, currentX,currentY, 0f);
    }
}
