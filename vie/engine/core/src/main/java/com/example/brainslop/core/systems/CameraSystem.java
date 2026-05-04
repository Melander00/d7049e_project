package com.example.brainslop.core.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.example.brainslop.core.Mappers;
import com.example.brainslop.core.components.CameraComponent;
import com.example.brainslop.core.components.TransformComponent;
import net.mgsx.gltf.scene3d.scene.SceneManager;

public class CameraSystem extends IteratingSystem {
    private final SceneManager sceneManager;

    public CameraSystem(SceneManager sceneManager) {
        super(Family.all(CameraComponent.class, TransformComponent.class).get());
        this.sceneManager = sceneManager;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        CameraComponent cameraComponent = Mappers.camera.get(entity);

        if(!cameraComponent.enabled) {
            return;
        }

        Camera camera = cameraComponent.camera;

        sceneManager.setCamera(camera);
        TransformComponent t = Mappers.transform.get(entity);
        camera.position.set(t.position);
        camera.direction.set(0, 0, -1);
        camera.up.set(0, 1, 0);
        t.rotation.transform(camera.direction);
        t.rotation.transform(camera.up);

        camera.update();
    }
}
