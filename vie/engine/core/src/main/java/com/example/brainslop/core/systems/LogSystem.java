package com.example.brainslop.core.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.example.brainslop.core.Mapper;
import com.example.brainslop.core.components.TransformComponent;

public class LogSystem extends IteratingSystem {
    public LogSystem() {
        super(Family.all(TransformComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent t = Mapper.transform.get(entity);
        System.out.println(t.position.x);
    }
}
