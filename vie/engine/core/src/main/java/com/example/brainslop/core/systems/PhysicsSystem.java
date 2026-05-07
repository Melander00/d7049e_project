package com.example.brainslop.core.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.utils.ObjectSet;
import com.example.brainslop.core.Mappers;
import com.example.brainslop.core.components.CollisionComponent;
import com.example.brainslop.core.components.PhysicsComponent;
import com.example.brainslop.core.components.TransformComponent;
import com.example.brainslop.core.messages.CollisionEntered;
import com.example.brainslop.core.messages.CollisionExit;
import com.example.brainslop.core.messages.MessageManager;

public class PhysicsSystem extends EntitySystem implements FixedUpdateSystem {
    private final btDynamicsWorld world;
    private final btDispatcher dispatcher;
    private final btCollisionConfiguration config;
    private final btBroadphaseInterface broadphase;
    private final btConstraintSolver solver;

    private final MessageManager msgManager;
    private final ObjectSet<Pair> current = new ObjectSet<>();
    private final ObjectSet<Pair> previous = new ObjectSet<>();

    private float accum = 0f;
    private final float step;

    private ImmutableArray<Entity> entities;
    private ImmutableArray<Entity> collisionObjects;


    private final Matrix4 tempMatrix = new Matrix4();
    private final Vector3 tempVec = new Vector3();
    private final Quaternion tempQuat = new Quaternion();

    public PhysicsSystem(int stepFrequency, MessageManager msgManager) {
        this.config = new btDefaultCollisionConfiguration();
        this.dispatcher = new btCollisionDispatcher(this.config);
        this.broadphase = new btDbvtBroadphase();
        this.solver = new btSequentialImpulseConstraintSolver();
        this.world = new btDiscreteDynamicsWorld(this.dispatcher, this.broadphase, this.solver, this.config);
        this.world.setGravity(new Vector3(0, -10f, 0));

        this.step = 1f / stepFrequency;

        this.msgManager = msgManager;
    }

    public btDynamicsWorld getWorld() {
        return world;
    }

    private void setupEntity(Entity entity) {
        TransformComponent t = Mappers.transform.get(entity);
        PhysicsComponent p = Mappers.physics.get(entity);

        tempMatrix.set(t.position, t.rotation, t.scale);

        p.rigidBody.setWorldTransform(tempMatrix);
        p.rigidBody.userData = entity;

        world.addRigidBody(p.rigidBody);
    }

    private void removeEntity(Entity entity) {
        PhysicsComponent p = Mappers.physics.get(entity);

        this.world.removeRigidBody(p.rigidBody);
        p.rigidBody.dispose();
    }

    private void setupStaticCollision(Entity entity) {
        TransformComponent t = Mappers.transform.get(entity);
        CollisionComponent c = Mappers.collision.get(entity);

        tempMatrix.set(t.position, t.rotation, t.scale);

        c.collisionObject = new btCollisionObject();
        c.collisionObject.setCollisionShape(c.shape);
        c.collisionObject.setWorldTransform(tempMatrix);
        c.collisionObject.userData = entity;

        if(c.isTrigger) {
            c.collisionObject.setCollisionFlags(btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
        }

        world.addCollisionObject(c.collisionObject);
    }

    private void removeStaticCollision(Entity entity) {
        CollisionComponent c = Mappers.collision.get(entity);
        world.removeCollisionObject(c.collisionObject);
        c.collisionObject.dispose();
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = this.getEngine().getEntitiesFor(Family.all(PhysicsComponent.class, TransformComponent.class).get());
        collisionObjects = this.getEngine().getEntitiesFor(Family.all(CollisionComponent.class, TransformComponent.class).get());
        getEngine().addEntityListener(
                Family.all(PhysicsComponent.class, TransformComponent.class).get(),
                new EntityListener() {
                    public void entityAdded(Entity entity) {
                        setupEntity(entity);
                    }

                    public void entityRemoved(Entity entity) {
                        removeEntity(entity);
                    }
                }
        );
        getEngine().addEntityListener(
                Family.all(CollisionComponent.class, TransformComponent.class).get(),
                new EntityListener() {
                    public void entityAdded(Entity entity) {
                        setupStaticCollision(entity);
                    }

                    public void entityRemoved(Entity entity) {
                        removeStaticCollision(entity);
                    }
                }
        );
    }

    @Override
    public void update(float deltaTime) {
//        accum += deltaTime;
//
//        while (accum >= step) {
//            world.stepSimulation(step, 0);
//
////            updateTransforms();
//            checkCollisions();
//
//            accum -= step;
//        }
    }

    @Override
    public void fixedUpdate(float deltaTime) {
        world.stepSimulation(deltaTime, 0);
        checkCollisions();
    }

    private void updateTransforms() {
        for (Entity entity : this.entities) {
            TransformComponent t = Mappers.transform.get(entity);
            PhysicsComponent p = Mappers.physics.get(entity);

            // Fill tempMatrix (NO allocation)
            p.rigidBody.getWorldTransform(tempMatrix);

            // Extract translation (NO allocation)
            tempMatrix.getTranslation(tempVec);

            // Extract rotation (NO allocation)
            tempMatrix.getRotation(tempQuat);

            // Copy into ECS
            t.position.set(tempVec);
            t.rotation.set(tempQuat);
        }
    }

    private void checkCollisions() {
        current.clear();

        int numManifolds = dispatcher.getNumManifolds();

        for (int i = 0; i < numManifolds; i++) {
            btPersistentManifold manifold = dispatcher.getManifoldByIndexInternal(i);

            if (manifold.getNumContacts() == 0) continue;

            Entity a = (Entity) manifold.getBody0().userData;
            Entity b = (Entity) manifold.getBody1().userData;

            if (a == null || b == null) continue;

            Pair pair = new Pair(a, b);
            current.add(pair);

            if (!previous.contains(pair)) {
                msgManager.sendMessage(new CollisionEntered(a, b));
            }
        }

        for (Pair pair : previous) {
            if (!current.contains(pair)) {
                msgManager.sendMessage(new CollisionExit(pair.a, pair.b));
            }
        }

        previous.clear();
        previous.addAll(current);
    }

    private static class Pair {
        Entity a, b;

        Pair(Entity a, Entity b) {
            if (a.hashCode() < b.hashCode()) {
                this.a = a;
                this.b = b;
            } else {
                this.a = b;
                this.b = a;
            }
        }

        @Override
        public int hashCode() {
            return a.hashCode() * 31 + b.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Pair)) return false;
            Pair p = (Pair) o;
            return p.a == a && p.b == b;
        }
    }
}
