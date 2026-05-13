package com.example.brainslop.core.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.example.brainslop.core.Mappers;
import com.example.brainslop.core.components.CameraComponent;
import com.example.brainslop.core.components.CollisionComponent;
import com.example.brainslop.core.components.PhysicsComponent;
import com.example.brainslop.core.components.TransformComponent;
import net.mgsx.gltf.scene3d.scene.SceneManager;

public class CameraSystem extends IteratingSystem {
    private final SceneManager sceneManager;

    public CameraSystem(SceneManager sceneManager) {
        super(Family.all(CameraComponent.class, TransformComponent.class).get());
        this.sceneManager = sceneManager;
    }

    private void setupEntity(Entity entity) {
        CameraComponent c = Mappers.camera.get(entity);
        if(c.camera == null) {
            c.camera = new PerspectiveCamera(c.fov, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            c.camera.near = c.near;
            c.camera.far = c.far;
        }
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        getEngine().addEntityListener(
                Family.all(CameraComponent.class).get(),
                new EntityListener() {
                    public void entityAdded(Entity entity) {
                        setupEntity(entity);
                    }

                    public void entityRemoved(Entity entity) {
                    }
                }
        );
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        CameraComponent cameraComponent = Mappers.camera.get(entity);

        if(!cameraComponent.enabled) {
            return;
        }

        Camera camera = cameraComponent.camera;

        camera.near = cameraComponent.near;
        camera.far = cameraComponent.far;

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
