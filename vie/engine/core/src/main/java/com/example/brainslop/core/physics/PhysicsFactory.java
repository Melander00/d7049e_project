package com.example.brainslop.core.physics;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.example.brainslop.core.Mappers;
import com.example.brainslop.core.components.PhysicsComponent;
import com.example.brainslop.core.components.TransformComponent;

public class PhysicsFactory {
    public static PhysicsComponent createBox(
            Entity entity,
            float mass,
            btCollisionShape shape
    ) {

        TransformComponent transform = Mappers.transform.get(entity);

        Vector3 inertia = new Vector3();
        if (mass > 0f) shape.calculateLocalInertia(mass, inertia);

        btMotionState motionState = new ECSMotionState(transform);

        btRigidBody.btRigidBodyConstructionInfo info =
                new btRigidBody.btRigidBodyConstructionInfo(
                        mass,
                        motionState,
                        shape,
                        inertia
                );

        btRigidBody body = new btRigidBody(info);

        PhysicsComponent pc = new PhysicsComponent();

        pc.rigidBody = body;

        body.userData = entity;

        return pc;
    }
}
