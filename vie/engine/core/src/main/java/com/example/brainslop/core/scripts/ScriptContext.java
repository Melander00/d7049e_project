package com.example.brainslop.core.scripts;

import com.badlogic.ashley.core.Entity;
import com.example.brainslop.core.ECS;
import com.example.brainslop.core.components.*;
import com.example.brainslop.core.messages.DestroyRequested;

public class ScriptContext {

    private final Entity entity;
    private final ECS ecs;

    public ScriptContext(Entity entity, ECS ecs) {
        this.entity = entity;
        this.ecs = ecs;
    }

    public HealthComponent getHealth() {
        return ecs.getComponent(entity, HealthComponent.class);
    }
/**
   public GateComponent getGate() {
        return ecs.getComponent(entity, GateComponent.class);
    }

    public PickupComponent getPickup() {
        return ecs.getComponent(entity, PickupComponent.class);
    }

    public AgentComponent getAgent() {
        return ecs.getComponent(entity, AgentComponent.class);
    }

 **/

    public PhysicsComponent getPhysics() {
        return ecs.getComponent(entity, PhysicsComponent.class);
    }

    public InputComponent getInput() {
        return ecs.getComponent(entity, InputComponent.class);
    }

    public void sendMessage(com.example.brainslop.core.messages.Message message) {
        ecs.messageManager.sendMessage(message);
    }

    public void destroy() {
        ecs.messageManager.sendMessage(new DestroyRequested(entity));
    }

    public Entity createEntity() {
        return ecs.createEntity();
    }

}
