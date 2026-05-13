package com.example.brainslop.core.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.example.brainslop.core.serialize.CollisionShapeComponent;

public class CollisionComponent implements Component {
    public transient btCollisionObject collisionObject;
    public CollisionShapeComponent shape;
    public boolean isTrigger = false;
}
