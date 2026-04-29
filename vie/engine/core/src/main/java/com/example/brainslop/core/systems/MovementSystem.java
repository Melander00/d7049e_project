package com.example.brainslop.core.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector3;
import com.example.brainslop.core.Mapper;
import com.example.brainslop.core.components.TransformComponent;

public class MovementSystem extends IteratingSystem {

    public MovementSystem() {
        super(Family.all(TransformComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent t = Mapper.transform.get(entity);

        Vector3 position = t.position;

        t.position.set(new Vector3(position.x+1, position.y, position.z));
    }
}
