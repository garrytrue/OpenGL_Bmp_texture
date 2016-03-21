package com.garrytrue.tryopengl;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * Created by tiv on 21.03.2016.
 */
public class Rectangle implements Shapes {
    private static final String TAG = Rectangle.class.getSimpleName();
    float [] vertices = {
////            triangle bottom-left
//            -0.8f, -0.8f,
//            -0.8f, 0.8f,
//            0.8f, -0.8f
////            top-right
//            -0.8f, 0.8f,
//            0.8f, -0.8f,
//            0.8f, 0.8f
//            Rectangle
            -0.8f, 0.8f,
            -0.8f, -0.8f,
            0.8f, 0.8f,
            0.8f, -0.8f,
    };

    private int programId;

    public FloatBuffer getVertexData() {
        return vertexData;
    }

    private FloatBuffer vertexData;

    public Rectangle(int programId) {
        this.programId = programId;
        prepareData();
        bindData();
    }
    private void prepareData(float[] vertices) {
        vertexData = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexData.put(vertices);
    }
    private void prepareData() {
        vertexData = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexData.put(vertices);
    }

    private void bindData() {
        int uColorLocation = glGetUniformLocation(programId, "v_Color");
        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        Log.d(TAG, "bindData: ColorLocation " + uColorLocation);
        int aPositionLocation = glGetAttribLocation(programId, "a_Position");
        Log.d(TAG, "bindData: PosLoc " + aPositionLocation);
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, 2, GL_FLOAT,
                false, 0, vertexData);
        glEnableVertexAttribArray(aPositionLocation);
    }
    @Override
    public void onDrawShape() {
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
    }
}
