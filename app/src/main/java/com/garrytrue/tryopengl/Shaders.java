package com.garrytrue.tryopengl;

import android.opengl.GLES20;
import android.util.Log;

/**
 * Created by garrytrue on 20.03.16.
 */
public class Shaders {
    private static final String TAG = Shaders.class.getSimpleName();
    private Shaders(){
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
            "precision mediump float;       \n"+
            "uniform vec4 v_Color;          \n"+
            "void main()                    \n"+		// The entry point for our fragment shader.
            "{                              \n"+
            "   gl_FragColor = v_Color;     \n"+		// Pass the color directly through the pipeline.
            "}                              \n";
    public static final String TEXTURE_VERTEX_SHADER =
           " uniform mat4 u_MVPMatrix;		"+// A constant representing the combined model/view/projection matrix.
   " uniform mat4 u_MVMatrix;"	+	// A constant representing the combined model/view matrix.

   " attribute vec4 a_Position;"+		// Per-vertex position information we will pass in.
    "attribute vec4 a_Color;"+			// Per-vertex color information we will pass in.
    "attribute vec3 a_Normal;"+		// Per-vertex normal information we will pass in.
    "attribute vec2 a_TexCoordinate;"+ // Per-vertex texture coordinate information we will pass in.

    "varying vec3 v_Position;"+		// This will be passed into the fragment shader.
    "varying vec4 v_Color;"+			// This will be passed into the fragment shader.
    "varying vec3 v_Normal;"+			// This will be passed into the fragment shader.
    "varying vec2 v_TexCoordinate;"+   // This will be passed into the fragment shader.

    // The entry point for our vertex shader.
   " void main()"+
    "{"+
        // Transform the vertex into eye space.
        "v_Position = vec3(u_MVMatrix * a_Position);"+

        // Pass through the color.
        "v_Color = a_Color;"+

        // Pass through the texture coordinate.
        "v_TexCoordinate = a_TexCoordinate;"+

        // Transform the normal's orientation into eye space.
        "v_Normal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));"+

        // gl_Position is a special variable used to store the final position.
        // Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
    "    gl_Position = u_MVPMatrix * a_Position;"+
   " }";
    public static final String TEXTURE_FRAGMENT_SHADER = "";
    public static int makeShader(String command, int shaderType){
        int shader = GLES20.glCreateShader(shaderType);
        if(shader != 0){
            GLES20.glShaderSource(shader, command);
            GLES20.glCompileShader(shader);
            final int [] compileStatus = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
            if(compileStatus[0] == 0){
//                Compile complete with error. Delete shader
                Log.d(TAG, "makeShader: Error " + GLES20.glGetShaderInfoLog(shader));
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
        Log.d(TAG, "makeProgram: vertex " + vertexShader + " fragment " +fragmentShader);
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
            Log.d(TAG, "makeProgram: " + linkStatus[0]);
            if(linkStatus[0] == GLES20.GL_FALSE){
                Log.d(TAG, "makeProgram: " + GLES20.glGetProgramInfoLog(program));
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
