package com.example.brainslop.core;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;

import com.example.brainslop.core.components.ModelComponent;
import com.example.brainslop.core.messages.MessageManager;

import java.util.ArrayList;
import java.util.List;

public class ECS extends Engine {

    private final List<EntitySystem> systems;
    public final MessageManager messageManager;

    public ECS(List<EntitySystem> systems) {
        this.systems = systems;
        this.messageManager = new MessageManager();
    }

    /**
     * Loads and enables all the systems.
     */
    public void loadSystems() {
        for(EntitySystem system : systems) {
            this.addSystem(system);
        }
    }

    /**
     * Creates an empty entity and adds it to the engine.
     * @return
     */
    @Override
    public Entity createEntity() {
        Entity entity = super.createEntity();
        this.addEntity(entity);
        return entity;
    }

    /**
     * Removes all components from the entity and removes the entity from the engine.
     * @param entity
     */
    public void destroyEntity(Entity entity) {
        entity.removeAll();
        this.removeEntity(entity);
    }

    /**
     * Adds the component to the entity provided.
     * @param e
     * @param component
     */
    public <T extends Component> void addComponent(Entity e, T component) {
        e.add(component);
    }

    /**
     * Removes the component from the entity. Since only one instance per component type can exist on an entity we don't need the exact reference.
     * @param e
     * @param component
     */
    public <T extends Component> void removeComponent(Entity e, Class<T> component) {
        e.remove(component);
    }

    /**
     * Checks if the
     * @param e
     * @param component
     * @return
     */
    public <T extends Component> boolean hasComponent(Entity e, Class<T> component) {
        return e.getComponent(component) != null;
    }

    /**
     * Returns the component. Runs in O(log n) time. Use a ComponentMapper for O(1) access.
     * @param e
     * @param component
     * @return
     */
    public <T extends Component> T getComponent(Entity e, Class<T> component) {
        return e.getComponent(component);
    }

    /**
     * Prints the entity and its component list.
     * @param e
     */
    public void debugInspect(Entity e) {
        // print a component list.
        System.out.printf("%s, %s%n", e, e.getComponents());
    }

    public void dispose() {
        this.removeAllEntities();
        this.removeAllSystems();
    }

}
