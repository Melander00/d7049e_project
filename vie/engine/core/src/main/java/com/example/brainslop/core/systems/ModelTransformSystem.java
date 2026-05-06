package com.example.brainslop.core.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.example.brainslop.core.Mappers;
import com.example.brainslop.core.components.ModelComponent;
import com.example.brainslop.core.components.TransformComponent;

public class ModelTransformSystem extends IteratingSystem {
    public ModelTransformSystem() {
        super(Family.all(ModelComponent.class, TransformComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent transform = Mappers.transform.get(entity);
        ModelComponent model = Mappers.model.get(entity);

        if(model.scene == null) return;

        model.scene.modelInstance.transform.set(
                transform.position,
                transform.rotation,
                transform.scale
        );
    }
}
