package com.example.brainslop.core.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.example.brainslop.core.Mappers;
import com.example.brainslop.core.components.*;
import com.example.brainslop.core.physics.PhysicsFactory;

import java.util.Vector;

public class AutoShooterSystem extends IteratingSystem {

    btSphereShape bulletShape = new btSphereShape(0.1f);

    private final Vector3 tempVector = new Vector3();

    public AutoShooterSystem() {
        super(Family.all(TransformComponent.class, AutoShooterComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AutoShooterComponent autoShooter = Mappers.autoShooter.get(entity);

        autoShooter.timeSinceLastShot += deltaTime;
        if(autoShooter.timeSinceLastShot >= 1f / autoShooter.shotsPerSecond) {
            shoot(entity);
            autoShooter.timeSinceLastShot = 0;
        }
    }

    private void shoot(Entity entity) {
        System.out.println("shoot");
        TransformComponent t = Mappers.transform.get(entity);

        Entity bullet = getEngine().createEntity();
        TransformComponent bulletTransform = new TransformComponent();
        bulletTransform.position.set(t.position);
        bulletTransform.rotation.set(t.rotation);
        bullet.add(bulletTransform);

        PhysicsComponent pc = PhysicsFactory.createComponent(bullet, 0.1f, bulletShape, false);
        pc.rigidBody.setCollisionFlags(btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
        tempVector.set(10,0,0);
        bulletTransform.rotation.transform(tempVector);
        pc.rigidBody.setLinearVelocity(tempVector);
        bullet.add(pc);

        ModelComponent bulletModel = new ModelComponent();
        bulletModel.assetPath = "model/bullet.glb";
        bullet.add(bulletModel);

        LifetimeComponent lifetimeComponent = new LifetimeComponent();
        lifetimeComponent.maxTimeAlive = 1f;
        bullet.add(lifetimeComponent);

    }
}
