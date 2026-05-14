package com.example.brainslop.core.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.example.brainslop.core.ECS;
import com.example.brainslop.core.Mappers;
import com.example.brainslop.core.components.LifetimeComponent;
import com.example.brainslop.core.messages.DestroyRequested;

public class LifetimeSystem extends IteratingSystem {

    public LifetimeSystem() {
        super(Family.all(LifetimeComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        LifetimeComponent lifetime = Mappers.lifetime.get(entity);

        lifetime.timeLived += deltaTime;

        if (lifetime.timeLived >= lifetime.maxTimeAlive) {
            ECS ecs = (ECS) getEngine();
            ecs.messageManager.sendMessage(new DestroyRequested(entity));
        }
    }
}
