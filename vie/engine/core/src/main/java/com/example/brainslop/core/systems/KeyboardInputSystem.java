package com.example.brainslop.core.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

/**
 * Keyboard implementation of InputSystem for desktop development.
 * When a config loading system exists, this class can be updated to read
 * bindings from a file instead of constants without changing anything else.
 */
public class KeyboardInputSystem extends InputSystem {

    private static final int AXIS_X_NEGATIVE = Keys.A;
    private static final int AXIS_X_NEGATIVE_ALT = Keys.LEFT;
    private static final int AXIS_X_POSITIVE = Keys.D;
    private static final int AXIS_X_POSITIVE_ALT = Keys.RIGHT;

    private static final int AXIS_Y_NEGATIVE = Keys.S;
    private static final int AXIS_Y_NEGATIVE_ALT = Keys.DOWN;
    private static final int AXIS_Y_POSITIVE = Keys.W;
    private static final int AXIS_Y_POSITIVE_ALT = Keys.UP;

    private static final int ACTION_1 = Keys.SPACE;
    private static final int ACTION_2 = Keys.SHIFT_LEFT;


    private float readAxis(int negative, int negativeAlt, int positive, int positiveAlt) {
        boolean neg = Gdx.input.isKeyPressed(negative) || Gdx.input.isKeyPressed(negativeAlt);
        boolean pos = Gdx.input.isKeyPressed(positive) || Gdx.input.isKeyPressed(positiveAlt);

        if (neg && !pos) {
            return -1f;
        }
        if (pos && !neg) {
            return 1f;
        }
        return 0f;
    }

    @Override
    protected float readAxisX() {
        return readAxis(AXIS_X_NEGATIVE, AXIS_X_NEGATIVE_ALT, AXIS_X_POSITIVE, AXIS_X_POSITIVE_ALT);
    }

    @Override
    protected float readAxisY() {
        return readAxis(AXIS_Y_NEGATIVE, AXIS_Y_NEGATIVE_ALT, AXIS_Y_POSITIVE, AXIS_Y_POSITIVE_ALT);
    }
    @Override
    protected boolean readAction1() {
        return Gdx.input.isKeyPressed(ACTION_1);
    }

    @Override
    protected boolean readAction2() {
        return Gdx.input.isKeyPressed(ACTION_2);
    }
}