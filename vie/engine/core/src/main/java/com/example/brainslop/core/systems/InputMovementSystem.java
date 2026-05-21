package com.example.brainslop.core.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.example.brainslop.core.Mappers;
import com.example.brainslop.core.components.InputComponent;
import com.example.brainslop.core.components.MovementComponent;
import com.example.brainslop.core.components.TransformComponent;

public class InputMovementSystem extends IteratingSystem {

    public InputMovementSystem() {
        super(Family.all(MovementComponent.class, InputComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        InputComponent i = Mappers.input.get(entity);
        MovementComponent m = Mappers.movement.get(entity);

        m.moveDir.set(-i.axisX, m.moveDir.y, i.axisY);
    }
}
