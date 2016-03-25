package com.garrytrue.tryopengl.actions;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by tiv on 24.03.2016.
 */
public class GLUserAction implements GLAction {
    public static final int ON_MAP_ACTION = 1001;
    public static final int ON_USER_LOCATION_ACTION = 1002;

    @IntDef({ON_MAP_ACTION, ON_USER_LOCATION_ACTION})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Action {
    }

    private float x;
    private float y;

    @Action
    private int actionType;

    GLUserAction(@Action int actionType, float x, float y) {
        this.actionType = actionType;
        this.x = x;
        this.y = y;
    }


    public static GLUserAction makeAction(@Action int actionType, float x, float y) {
        return new GLUserAction(actionType, x, y);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @Override
    public int getScaleFactor() {
        return -1;
    }

    public int getActionType() {
        return actionType;
    }
}
