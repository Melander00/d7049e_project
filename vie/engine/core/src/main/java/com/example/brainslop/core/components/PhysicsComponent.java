package com.example.brainslop.core.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public class PhysicsComponent implements Component {
    public final btRigidBody rigidBody;
    public PhysicsComponent(btRigidBody.btRigidBodyConstructionInfo info) {
        this.rigidBody = new btRigidBody(info);
    }
}
