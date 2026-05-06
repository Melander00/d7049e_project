package com.example.brainslop.core.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.example.brainslop.core.Mappers;
import com.example.brainslop.core.components.LifetimeComponent;

public class LifetimeSystem extends IteratingSystem {
    public LifetimeSystem() {
        super(Family.all(LifetimeComponent.class).get());
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        LifetimeComponent l = Mappers.lifetime.get(entity);

        l.timeLived += deltaTime;
        if(l.timeLived >= l.maxTimeAlive) {
            getEngine().removeEntity(entity);
        }
    }
}
