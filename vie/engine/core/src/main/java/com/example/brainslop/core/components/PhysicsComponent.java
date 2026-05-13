package com.example.brainslop.core.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.example.brainslop.core.serialize.CollisionShapeComponent;

public class PhysicsComponent implements Component {
    public transient btRigidBody rigidBody;
    public float mass;
    public CollisionShapeComponent shape;
    public boolean hasGravity = true;
}
