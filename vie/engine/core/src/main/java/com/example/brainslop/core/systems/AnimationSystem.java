package com.example.brainslop.core.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.example.brainslop.core.Mappers;
import com.example.brainslop.core.components.AnimationComponent;
import com.example.brainslop.core.components.ModelComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimationSystem extends IteratingSystem {

    private final Map<Entity, String> playingAnimations = new HashMap<>();

    public AnimationSystem() {
        super(Family.all(ModelComponent.class, AnimationComponent.class).get());
    }

    public List<String> getAnimationIds(Entity entity) {
        ModelComponent model = Mappers.model.get(entity);
        if (model == null || model.scene == null) {
            return List.of();
        }
        List<String> ids = new ArrayList<>();
        for (com.badlogic.gdx.graphics.g3d.model.Animation a : model.scene.modelInstance.animations) {
            ids.add(a.id);
        }
        return ids;
    }
    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(
                Family.all(ModelComponent.class, AnimationComponent.class).get(),
                new EntityListener() {
                    @Override
                    public void entityAdded(Entity entity) {
                    }

                    @Override
                    public void entityRemoved(Entity entity) {
                        playingAnimations.remove(entity);
                    }
                }
        );
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        ModelComponent model = Mappers.model.get(entity);
        AnimationComponent animation = Mappers.animation.get(entity);

        if (model.scene == null) {
            return;
        }

        if (animation.currentAnimationId == null || animation.currentAnimationId.isEmpty()) {
            return;
        }

        if (model.scene.animationController == null) {
            return;
        }

        String currentlyPlaying = playingAnimations.get(entity);

        if (!animation.currentAnimationId.equals(currentlyPlaying)) {
            int loopCount = animation.loop ? -1 : 1;
            model.scene.animationController.setAnimation(animation.currentAnimationId, loopCount);
            playingAnimations.put(entity, animation.currentAnimationId);
        }

        model.scene.animationController.update(deltaTime * animation.playbackSpeed);
    }
}
