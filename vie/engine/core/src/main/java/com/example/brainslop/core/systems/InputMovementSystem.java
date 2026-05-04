package com.example.brainslop.core.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.example.brainslop.core.Mappers;
import com.example.brainslop.core.components.InputComponent;
import com.example.brainslop.core.components.TransformComponent;

public class InputMovementSystem extends IteratingSystem {

    public InputMovementSystem() {
        super(Family.all(TransformComponent.class, InputComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        InputComponent i = Mappers.input.get(entity);
        TransformComponent t = Mappers.transform.get(entity);

        float moveSpeed = 10 * deltaTime;

        float moveX = i.axisX * moveSpeed;
        float moveY = i.axisY * moveSpeed;

        t.position.x -= moveX;
        t.position.z += moveY;
    }
}
