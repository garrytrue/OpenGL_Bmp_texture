package com.garrytrue.tryopengl.utils;

import android.opengl.GLES20;
import android.util.Log;

/**
 * Created by garrytrue on 20.03.16.
 */
public class Shaders {
    private static final String TAG = Shaders.class.getSimpleName();

    private Shaders() {
        throw new AssertionError("Don't make instance.");
    }

    public static final String SIMPLE_VERTEX_SHADERS =
                    "attribute vec4 a_Position;     \n" +
                    "void main()                    \n" +
                    "{                              \n" +
                    "    gl_Position = a_Position;  \n" +
                    "    gl_PointSize = 10.0;       \n" +
                    "}                              \n";
    public static final String SIMPLE_FRAGMENT_SHADERS =
                    "precision mediump float;       \n" +
                    "uniform vec4 v_Color;          \n" +
                    "void main()                    \n" +        // The entry point for our fragment shader.
                    "{                              \n" +
                    "   gl_FragColor = v_Color;     \n" +        // Pass the color directly through the pipeline.
                    "}                              \n";
    public static final String TEXTURE_VERTEX_SHADER =
                    "attribute vec4 a_Position;\n" +
                    "attribute vec2 a_Texture;\n" +
                    "attribute vec4 a_Color;\n" +
                    "uniform mat4 u_Matrix;\n" +
                    "varying vec2 v_Texture;\n" +
                    "varying vec4 v_Color;\n" +
                    "void main()\n" +
                    "{\n" +
                    "    gl_Position = u_Matrix * a_Position;\n" +
                    "    v_Texture = a_Texture;\n" +
                    "    gl_PointSize = 10.0;\n" +
                    "    v_Color = a_Color; \n" +
                    "}";
    public static final String TEXTURE_FRAGMENT_SHADER = "precision mediump float;\n" +
            "uniform sampler2D u_TextureUnit;\n" +
            "varying vec2 v_Texture;\n" +
            "varying vec4 v_Color;\n" +
            "void main()\n" +
            "{\n" +
            "    gl_FragColor = texture2D(u_TextureUnit, v_Texture) /* v_Color*/    ; \n" +
            "gl_FragColor.rgb *= v_Color.a;\n" +
            "}";

    public static int makeShader(String command, int shaderType) {
        int shader = GLES20.glCreateShader(shaderType);
        if (shader != 0) {
            GLES20.glShaderSource(shader, command);
            GLES20.glCompileShader(shader);
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
            if (compileStatus[0] == 0) {
//                Compile complete with error. Delete shader
                Log.d(TAG, "makeShader: Error " + GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
            if (shader == 0) {
                throw new RuntimeException("Shader[" + shaderType + "] doesn't compile");
            }
            return shader;
        } else
            throw new RuntimeException("Problem with shader[" + shaderType + "]");
    }

    public static int makeProgram() {
        int vertexShader = makeShader(SIMPLE_VERTEX_SHADERS, GLES20.GL_VERTEX_SHADER);
        int fragmentShader = makeShader(SIMPLE_FRAGMENT_SHADERS, GLES20.GL_FRAGMENT_SHADER);
        Log.d(TAG, "makeProgram: vertex " + vertexShader + " fragment " + fragmentShader);
        int program = GLES20.glCreateProgram();
        if (program != 0) {
//            attach vertex shader to program
            GLES20.glAttachShader(program, vertexShader);
            //            attach fragment shader to program
            GLES20.glAttachShader(program, fragmentShader);
//            Bind attr. see shader constant(SIMPLE_VERTEX_SHADERS, SIMPLE_FRAGMENT_SHADERS)
            GLES20.glBindAttribLocation(program, 0, "a_Position");
            GLES20.glBindAttribLocation(program, 1, "v_Color");

            GLES20.glLinkProgram(program);
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            Log.d(TAG, "makeProgram: " + linkStatus[0]);
            if (linkStatus[0] == GLES20.GL_FALSE) {
                Log.d(TAG, "makeProgram: " + GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
            }
            if (program == 0) {
                throw new RuntimeException("GL program doesn't link");
            }
        }
        return program;
    }

    public static int makeProgram(int vertexShader, int fragmentShader) {
        Log.d(TAG, "makeProgram: vertex " + vertexShader + " fragment " + fragmentShader);
        int program = GLES20.glCreateProgram();
        if (program != 0) {
//            attach vertex shader to program
            GLES20.glAttachShader(program, vertexShader);
            //            attach fragment shader to program
            GLES20.glAttachShader(program, fragmentShader);
//            Bind attr. see shader constant(SIMPLE_VERTEX_SHADERS, SIMPLE_FRAGMENT_SHADERS)
//            GLES20.glBindAttribLocation(program, 0, "a_Position");
//            GLES20.glBindAttribLocation(program, 1, "v_Color");

            GLES20.glLinkProgram(program);
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            Log.d(TAG, "makeProgram: " + linkStatus[0]);
            if (linkStatus[0] == GLES20.GL_FALSE) {
                Log.d(TAG, "makeProgram: " + GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
            }
            if (program == 0) {
                throw new RuntimeException("GL program doesn't link");
            }
        }
        return program;
    }
}
