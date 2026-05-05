package com.example.brainslop.core.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;

public class CollisionComponent implements Component {
    public btCollisionObject collisionObject;
    public btCollisionShape shape;
    public boolean isTrigger = false;
}
