package com.garrytrue.tryopengl.renderers;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.garrytrue.tryopengl.actions.GLAction;
import com.garrytrue.tryopengl.actions.GLMapAction;
import com.garrytrue.tryopengl.actions.GLUserAction;
import com.garrytrue.tryopengl.gl_objects.GLPointer;
import com.garrytrue.tryopengl.utils.Shaders;
import com.garrytrue.tryopengl.utils.TextureUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

/**
 * Created by tiv on 21.03.2016.
 */
public class TextureRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = TextureRenderer.class.getSimpleName();
    private final static int POSITION_COUNT = 3;
    private static final int TEXTURE_COUNT = 2;
    private static final int STRIDE = (POSITION_COUNT
            + TEXTURE_COUNT) * 4;
    private GLAction action;

    private FloatBuffer vertexData;
    private GLPointer mGLPointer = new GLPointer();


    private int aPositionLocation;
    private int aTextureLocation;
    private int uTextureUnitLocation;
    private int uMatrixLocation;
    private int aColorLocation;

    private int programId;

    private float[] mProjectionMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mModelMatrix = new float[16];
    private float[] mResultMatrix = new float[16];


    private int texture;

    @Override
    public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
        Log.d(TAG, "onSurfaceCreated: ");
        glClearColor(1f, 1f, 1f, 1f);
        glEnable(GL_DEPTH_TEST);
        createAndUseProgram();
        getLocations();
        prepareData();
        bindData();
        createViewMatrix();
        mGLPointer.updatePointerPos(0, 0);
    }

    @Override
    public void onSurfaceChanged(GL10 arg0, int width, int height) {
        Log.d(TAG, "onSurfaceChanged() called with: " + "arg0 = [" + arg0 + "], width = [" + width + "], height = [" + height + "]");
        glViewport(0, 0, width, height);
        createProjectionMatrix(width, height);
        bindMatrix();
    }

    private void prepareData() {

        float[] vertices = {
//                floor rectangle
                -1, 1, 0, 0, 0,
                -1, -1, 0, 0, 1,
                1, 1, 0, 1, 0,
                1, -1, 0, 1, 1,
//                user location rectangle
                -.05f, .05f, 0f, 0, 0,
                .05f, .05f, 0f, 0, 0,
                0f, 0f, 0f, 0, 0,
        };


        vertexData = ByteBuffer
                .allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(vertices);

        texture = TextureUtils.loadTexture();
    }


    private void createAndUseProgram() {
        int vertexShaderId = Shaders.makeShader(Shaders.TEXTURE_VERTEX_SHADER, GLES20.GL_VERTEX_SHADER);
        int fragmentShaderId = Shaders.makeShader(Shaders.TEXTURE_FRAGMENT_SHADER, GLES20.GL_FRAGMENT_SHADER);
        programId = Shaders.makeProgram(vertexShaderId, fragmentShaderId);
        glUseProgram(programId);
    }

    private void getLocations() {
        aPositionLocation = glGetAttribLocation(programId, "a_Position");
        aTextureLocation = glGetAttribLocation(programId, "a_Texture");
        uTextureUnitLocation = glGetUniformLocation(programId, "u_TextureUnit");
        uMatrixLocation = glGetUniformLocation(programId, "u_Matrix");
        aColorLocation = glGetAttribLocation(programId, "a_Color");
        Log.d(TAG, "getLocations: Color_Location " + aColorLocation);
    }

    private void bindData() {
        // vertex coord
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COUNT, GL_FLOAT,
                false, STRIDE, vertexData);
        glEnableVertexAttribArray(aPositionLocation);


        // texture coord
        vertexData.position(POSITION_COUNT);
        glVertexAttribPointer(aTextureLocation, TEXTURE_COUNT, GL_FLOAT,
                false, STRIDE, vertexData);
        glEnableVertexAttribArray(aTextureLocation);

        // put texture to unit 0
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture);
        // юнит текстуры
        glUniform1i(uTextureUnitLocation, 0);
//        color unit
        glEnableVertexAttribArray(aColorLocation);
    }

    private void createProjectionMatrix(int width, int height) {
        Log.d(TAG, "createProjectionMatrix() called with: " + "width = [" + width + "], height = [" + height + "]");
        float ratio = 1;
        float left = -1;
        float right = 1;
        float bottom = -1;
        float top = 1;
        float near = 0;
        float far = 20;
        if (width > height) {
            ratio = (float) width / height;
            left *= ratio;
            right *= ratio;
        } else {
            ratio = (float) height / width;
            bottom *= ratio;
            top *= ratio;
        }

        Matrix.orthoM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    private void createViewMatrix() {
        // точка положения камеры
        float eyeX = 0;
        float eyeY = 0;
        float eyeZ = 0.5f;

        // точка направления камеры
        float centerX = 0;
        float centerY = 0;
        float centerZ = 0;

        // up-вектор
        float upX = 0;
        float upY = 1;
        float upZ = 0;

        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }


    private void bindMatrix() {
        Matrix.multiplyMM(mResultMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mResultMatrix, 0, mProjectionMatrix, 0, mResultMatrix, 0);
        glUniformMatrix4fv(uMatrixLocation, 1, false, mResultMatrix, 0);
    }


    @Override
    public void onDrawFrame(GL10 arg0) {
        Log.d(TAG, "onDrawFrame: ");
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        drawPointer();
        drawMap();
    }

    private void drawMap() {
        Matrix.setIdentityM(mModelMatrix, 0);
        handleUserAction(false);
        bindMatrix();
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

    }

    private void drawPointer() {
        glDisable(GL_TEXTURE_2D);
        Matrix.setIdentityM(mModelMatrix, 0);
        handleUserAction(true);
        bindMatrix();
        glUniform4f(aColorLocation, 1.0f, 1.0f, 0.0f, 1.0f);
        mGLPointer.drawObject();
    }

    public void scaleMap(boolean reset, float translateToX, float translateToY) {
        Log.d(TAG, "scaleMap() called with: " + "reset = [" + reset + "]");
        if (reset) {
            Matrix.setIdentityM(mModelMatrix, 0);
        } else {
            Matrix.scaleM(mModelMatrix, 0, 2f, 2f, 0f);
            Matrix.translateM(mModelMatrix, 0, -translateToX, -translateToY, 0f);
        }
    }

    public void  moveMap(float moveToX, float moveToY, int zoom) {
        Log.d(TAG, "moveMap() called with: " + "moveToX = [" + moveToX + "], moveToY = [" + moveToY + "], zoom = [" + zoom + "]");
        final float divider = zoom == 0 ? 1f : 1f * zoom;
        Matrix.translateM(mModelMatrix, 0, (moveToX / divider), (moveToY / divider), 0f);
    }

    public void movePointerToPosition(float x, float y) {
        Log.d(TAG, "movePointerToPosition() called with: " + "x = [" + x + "], y = [" + y + "]");
        mGLPointer.updatePointerPos(x, y);
        Log.d(TAG, "movePointerToPosition: Move to x = [ " + mGLPointer.getCurrentX() + " ], y = [ " + mGLPointer.getCurrentY() + "]");
        Matrix.translateM(mModelMatrix, 0, mGLPointer.getCurrentX(), mGLPointer.getCurrentY(), 0f);
        mGLPointer.updatePointerPos(x, y);

    }

    private void copyMatrix(float[] source, float[] dest) {
        System.arraycopy(source, 0, dest, 0, 16);
    }

    private boolean isEquals(float[] matrix, float[] anotherMatrix) {
        if (matrix.length != anotherMatrix.length) {
            return false;
        }
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i] != anotherMatrix[i]) {
                return false;
            }
        }
        return true;
    }

    public void setAction(GLAction action) {
        this.action = action;
    }

    private void handleUserAction(boolean isPointer) {
        if (action != null) {
            final int actionType = action.getActionType();
            if (isPointer) {
//                Make Action only for pointer
                if (actionType == GLUserAction.ON_USER_LOCATION_ACTION) {
                    Log.d(TAG, "handleUserAction: Make User LocationAction");
                    movePointerToPosition(action.getX(), action.getY());
                }
//                If user move map, we need move map with pointer
                if (actionType == GLMapAction.MOVE_ACTION) {
                    Log.d(TAG, "handleUserAction: Make Move action");
                    moveMap(action.getX(), action.getY(), 0);
                }
            } else {
//                Make Action only for camera
                if (actionType == GLMapAction.SCALE_ACTION) {
                    Log.d(TAG, "handleUserAction: Make Scale action");
                    scaleMap(((GLMapAction) action).isReset(), action.getX(), action.getY());
                }
                if (actionType == GLMapAction.MOVE_ACTION) {
                    Log.d(TAG, "handleUserAction: Make Move action");
                    moveMap(action.getX(), action.getY(), 0);
                }
            }
        }
    }
}
