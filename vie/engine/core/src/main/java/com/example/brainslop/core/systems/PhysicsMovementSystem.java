package com.example.brainslop.core.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.example.brainslop.core.Mappers;
import com.example.brainslop.core.components.MovementComponent;
import com.example.brainslop.core.components.PhysicsComponent;
import com.example.brainslop.core.components.TransformComponent;

public class PhysicsMovementSystem extends IteratingSystem {

    private final Vector3 tmp = new Vector3();

    public PhysicsMovementSystem() {
        super(Family.all(MovementComponent.class, PhysicsComponent.class, TransformComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MovementComponent m = Mappers.movement.get(entity);
        PhysicsComponent p = Mappers.physics.get(entity);
        TransformComponent t = Mappers.transform.get(entity);

        btRigidBody body = p.rigidBody;

        tmp.set(m.moveDir);
        tmp.scl(m.movementSpeed);

        t.rotation.transform(tmp);

        tmp.y = body.getLinearVelocity().y;
        body.setLinearVelocity(tmp);
        body.activate();
    }


}
