package com.garrytrue.tryopengl;

import android.opengl.GLES20;

/**
 * Created by garrytrue on 20.03.16.
 */
public class Shaders {
    private Shaders(){
        throw new AssertionError("Don't make instance.");
    }
    public static final String SIMPLE_VERTEX_SHADERS =
            "attribute vec4 a_Position;     \n" +
            "void main()                    \n" +
            "{                              \n" +
            "    gl_Position = a_Position;  \n" +
            "}                              \n";
    public static final String SIMPLE_FRAGMENT_SHADERS =
            "precision mediump float;       \n"
            +            "varying vec4 v_Color;          \n"
            + "void main()                    \n"		// The entry point for our fragment shader.
            + "{                              \n"
            + "   gl_FragColor = v_Color;     \n"		// Pass the color directly through the pipeline.
            + "}                              \n";

    public static int makeShader(String command, int shaderType){
        int shader = GLES20.glCreateShader(shaderType);
        if(shader != 0){
            GLES20.glShaderSource(shader, command);
            GLES20.glCompileShader(shader);
            final int [] compileStatus = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
            if(compileStatus[0] == 0){
//                Compile complete with error. Delete shader
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
            if(shader == 0){
                throw new RuntimeException("Shader[" + shaderType + "] doesn't compile");
            }
        return shader;
        }else
            throw new RuntimeException("Problem with shader[" + shaderType + "]");
    }
    public static int makeProgram(){
        int vertexShader = makeShader(SIMPLE_VERTEX_SHADERS, GLES20.GL_VERTEX_SHADER);
        int fragmentShader = makeShader(SIMPLE_FRAGMENT_SHADERS, GLES20.GL_FRAGMENT_SHADER);
        int program = GLES20.glCreateProgram();
        if(program != 0){
//            attach vertex shader to program
            GLES20.glAttachShader(program, vertexShader);
            //            attach fragment shader to program
            GLES20.glAttachShader(program, fragmentShader);
//            Bind attr. see shader constant(SIMPLE_VERTEX_SHADERS, SIMPLE_FRAGMENT_SHADERS)
            GLES20.glBindAttribLocation(program, 0, "a_Position");
            GLES20.glBindAttribLocation(program, 1, "v_Color");

            GLES20.glLinkProgram(program);
            final int [] linkStatus = new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            if(linkStatus[0] == 0){
                GLES20.glDeleteProgram(program);
                program = 0;
            }
            if(program == 0){
                throw new RuntimeException("GL program doesn't link");
            }
        }
        return program;
    }
}
