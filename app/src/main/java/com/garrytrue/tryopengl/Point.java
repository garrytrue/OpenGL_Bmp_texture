package com.garrytrue.tryopengl;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * Created by tiv on 21.03.2016.
 */
public class Point implements Shapes {
    private static final String TAG = Point.class.getSimpleName();
    float[] vertices = {0.5f,0.5f};
    private int programId;

    private FloatBuffer vertexData;
    private void prepareData() {
        vertexData = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexData.put(vertices);
    }

    public Point(int programId) {
        this.programId = programId;
        prepareData();
        bindData();

    }

    private void bindData() {
        int uColorLocation = glGetUniformLocation(programId, "v_Color");
        glUniform4f(uColorLocation, 0.5f, 0.0f, 0.0f, 1.0f);
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
        glDrawArrays(GL_POINTS, 0, 1);
    }

}
