package com.example.brainslop.core.scripts;

import com.badlogic.ashley.core.Entity;
import com.example.brainslop.core.ECS;
import com.example.brainslop.core.components.HealthComponent;
import com.example.brainslop.core.components.InputComponent;
import com.example.brainslop.core.messages.DestroyRequested;
import com.example.brainslop.core.messages.Message;

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

    public InputComponent getInput() {
        return ecs.getComponent(entity, InputComponent.class);
    }

    public void sendMessage(Message message) {
        ecs.messageManager.sendMessage(message);
    }

    public void destroy() {
        ecs.messageManager.sendMessage(new DestroyRequested(entity));
    }

    public Entity createEntity() {
        return ecs.createEntity();
    }
}