package com.garrytrue.tryopengl.actions;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by tiv on 24.03.2016.
 */
public class GLMapAction implements GLAction {
    public static final int SCALE_ACTION = 1003;
    public static final int MOVE_ACTION = 1004;
    private float x;
    private float y;

    public boolean isReset() {
        return reset;
    }

    private boolean reset;


    @MapAction
    private int mapAction;

    @Override
    public int getActionType() {
        return mapAction;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public int getScaleFactor() {
        return -1;
    }

    @IntDef({SCALE_ACTION, MOVE_ACTION})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MapAction {
    }

    ;

    private GLMapAction(@MapAction int mapAction, float x, float y, boolean reset) {
        this.mapAction = mapAction;
        this.reset = reset;
        this.x = x;
        this.y = y;


    }

    public static GLMapAction makeMapAction(@MapAction int mapAction, float x, float y, boolean reset) {
        return new GLMapAction(mapAction, x, y, reset);
    }
}
