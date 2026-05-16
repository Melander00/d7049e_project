package com.example.brainslop.core.physics;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBodyFlags;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.example.brainslop.core.Mappers;
import com.example.brainslop.core.serialize.CollisionShapeComponent;
import com.example.brainslop.core.components.PhysicsComponent;
import com.example.brainslop.core.components.TransformComponent;

import java.util.HashMap;
import java.util.Map;

public class PhysicsFactory {

    private static final Map<String, btCollisionShape> shapeCache = new HashMap<>();

    private static btCollisionShape createShape(
            CollisionShape type,
            float a,
            float b,
            float c
    ) {

        System.out.println(type);

        return switch(type) {
            case CYLINDER -> new btCylinderShape(new Vector3(a,b,c));
            case CAPSULE -> new btCapsuleShape(a, b);
            case SPHERE -> new btSphereShape(a);
//            case BOX -> new btBoxShape(new Vector3(a,b,c));
            default -> new btBoxShape(new Vector3(a,b,c));
        };
    }

    public static btCollisionShape createShape(
            CollisionShape type,
            float a,
            float b,
            float c,
            String cacheName
    ) {

        if(cacheName == null || cacheName.isEmpty()) {
            return createShape(type, a, b, c);
        }

        if(shapeCache.containsKey(cacheName)) {
            return shapeCache.get(cacheName);
        }

        btCollisionShape shape = createShape(type, a, b, c);
        shapeCache.put(cacheName, shape);
        return shape;
    }



    public static btRigidBody createRigidBody(
            Entity entity,
            float mass,
            CollisionShapeComponent shapeC
    ) {
        TransformComponent transform = Mappers.transform.get(entity);

        btCollisionShape shape = createShape(shapeC.type, shapeC.a, shapeC.b, shapeC.c, shapeC.shapeName);

        btCompoundShape compoundShape = new btCompoundShape();
        compoundShape.addChildShape(new Matrix4().setTranslation(shapeC.offsetPosition), shape);

        Vector3 inertia = new Vector3();
        if (mass > 0f) shape.calculateLocalInertia(mass, inertia);

        btMotionState motionState = new ECSMotionState(transform);

        btRigidBody.btRigidBodyConstructionInfo info =
                new btRigidBody.btRigidBodyConstructionInfo(
                        mass,
                        motionState,
                        compoundShape,
                        inertia
                );

        btRigidBody body = new btRigidBody(info);

        info.dispose();

        return body;
    }

    private static PhysicsComponent createPC(
            Entity entity,
            float mass,
            CollisionShapeComponent shape
    ) {

        btRigidBody body = createRigidBody(entity, mass, shape);

        PhysicsComponent pc = new PhysicsComponent();

        pc.rigidBody = body;

        pc.mass = mass;
        pc.shape = shape;

        body.userData = entity;

        return pc;
    }

    public static PhysicsComponent createComponent(
            Entity entity,
            float mass,
            CollisionShapeComponent shape
    ) {
        return createPC(entity, mass, shape);
    }

    public static PhysicsComponent createComponent(
            Entity entity,
            float mass,
            CollisionShapeComponent shape,
            boolean hasGravity
    ) {

        PhysicsComponent pc = createPC(entity, mass, shape);

        if (!hasGravity) {
            pc.rigidBody.setFlags(btRigidBodyFlags.BT_DISABLE_WORLD_GRAVITY);
            pc.rigidBody.setGravity(new Vector3(0,0,0));
        }

        return pc;
    }
}
