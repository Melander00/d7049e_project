package com.example.brainslop.core.physics;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.example.brainslop.core.components.TransformComponent;

public class ECSMotionState extends btMotionState {
    private final TransformComponent transform;

    public ECSMotionState(TransformComponent transform) {
        this.transform = transform;
    }

    @Override
    public void getWorldTransform(Matrix4 worldTrans) {
        worldTrans.set(transform.position, transform.rotation, transform.scale);
    }

    @Override
    public void setWorldTransform(Matrix4 worldTrans) {
        worldTrans.getTranslation(transform.position);
        worldTrans.getRotation(transform.rotation);
    }
}
