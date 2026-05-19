package com.example.brainslop.core;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.utils.ObjectSet;
import com.example.brainslop.core.messages.DestroyRequested;
import com.example.brainslop.core.messages.MessageManager;
import com.example.brainslop.core.messages.MessageType;
import com.example.brainslop.core.systems.FixedUpdateSystem;

import java.util.ArrayList;
import java.util.List;

public class ECS extends Engine {

    private final List<EntitySystem> systems;
    private final List<FixedUpdateSystem> fixedUpdateSystems;
    private final ObjectSet<Entity> pendingDestroy;

    public final MessageManager messageManager;

    private float accum = 0;
    private float stepSize = 1f / 60f;

    public ECS(List<EntitySystem> systems, float fixedUpdateFrequency, MessageManager messageManager) {
        this.systems = systems;
        this.messageManager = messageManager;
        this.fixedUpdateSystems = new ArrayList<>();
        this.pendingDestroy = new ObjectSet<>();
        this.stepSize = 1f / fixedUpdateFrequency;

        /*
         * ECS owns entity lifecycle.
         * DestroyRequested messages are queued first and flushed after update work,
         * so entities are not removed while systems are still iterating.
         */
        this.messageManager.subscribe(MessageType.DESTROY_REQUESTED, message -> {
            DestroyRequested destroyRequested = (DestroyRequested) message;
            pendingDestroy.add(destroyRequested.entity);
        });
    }

    /**
     * Loads and enables all the systems.
     */
    public void loadSystems() {
        for (EntitySystem system : systems) {
            this.addSystem(system);

            if (system instanceof FixedUpdateSystem fixed) {
                fixedUpdateSystems.add(fixed);
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        accum += deltaTime;

        while (accum >= stepSize) {
            for (FixedUpdateSystem system : fixedUpdateSystems) {
                system.fixedUpdate(stepSize);
            }

            accum -= stepSize;
        }

        flushDestroyedEntities();
    }

    /**
     * Creates an empty entity and adds it to the engine.
     */
    @Override
    public Entity createEntity() {
        Entity entity = super.createEntity();
        this.addEntity(entity);
        return entity;
    }

    /**
     * Removes all components from the entity and removes the entity from the engine.
     */
    public void destroyEntity(Entity entity) {
        if (entity == null) {
            return;
        }

//        entity.removeAll();
        this.removeEntity(entity);
    }

    private void flushDestroyedEntities() {
        for (Entity entity : pendingDestroy) {
            destroyEntity(entity);
        }

        pendingDestroy.clear();
    }

    /**
     * Adds the component to the entity provided.
     */
    public <T extends Component> void addComponent(Entity e, T component) {
        e.add(component);
    }

    /**
     * Removes the component from the entity.
     */
    public <T extends Component> void removeComponent(Entity e, Class<T> component) {
        e.remove(component);
    }

    /**
     * Checks if the entity has the given component type.
     */
    public <T extends Component> boolean hasComponent(Entity e, Class<T> component) {
        return e.getComponent(component) != null;
    }

    /**
     * Returns the component. Use a ComponentMapper in hot systems for faster access.
     */
    public <T extends Component> T getComponent(Entity e, Class<T> component) {
        return e.getComponent(component);
    }

    /**
     * Prints the entity and its component list.
     */
    public void debugInspect(Entity e) {
        System.out.printf("%s, %s%n", e, e.getComponents());
    }

    public void dispose() {
        pendingDestroy.clear();
        this.removeAllEntities();
        this.removeAllSystems();
    }
}
