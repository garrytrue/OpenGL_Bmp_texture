package com.garrytrue.tryopengl.primirives;

import android.util.Log;

import static android.opengl.GLES20.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by tiv on 21.03.2016.
 */
public class Triangle  implements Shapes {
    private static final String TAG = Triangle.class.getSimpleName();
    float[] vertices = {
            -0.8f, 0.8f,
            -0.8f, -0.8f,
            0.8f, -0.8f
    };

    public Triangle(int programId) {
        this.programId = programId;
        prepareData();
        bindData();
    }

    private int programId;

    public FloatBuffer getVertexData() {
        return vertexData;
    }

    private FloatBuffer vertexData;

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

    public void onDrawShape() {
        glDrawArrays(GL_TRIANGLES, 0, 3);
    }

}
