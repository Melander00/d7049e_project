package com.example.brainslop.core.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector3;
import com.example.brainslop.core.Mappers;
import com.example.brainslop.core.components.FreezeRotationComponent;
import com.example.brainslop.core.components.PhysicsComponent;

public class FreezeRotationSystem extends IteratingSystem {

    private final Vector3 tempVec = new Vector3();

    public FreezeRotationSystem() {
        super(Family.all(FreezeRotationComponent.class, PhysicsComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        FreezeRotationComponent freeze = Mappers.freezeRotation.get(entity);
        PhysicsComponent pc = Mappers.physics.get(entity);

        tempVec.x = freeze.freezePitch ? 0f : 1f;
        tempVec.y = freeze.freezeYaw ? 0f : 1f;
        tempVec.z = freeze.freezeRoll ? 0f : 1f;

        pc.rigidBody.setAngularFactor(tempVec);
    }
}
