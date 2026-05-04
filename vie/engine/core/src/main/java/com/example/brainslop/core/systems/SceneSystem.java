package com.example.brainslop.core.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.example.brainslop.core.Assets;
import com.example.brainslop.core.Mappers;
import com.example.brainslop.core.components.ModelComponent;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneManager;

public class SceneSystem extends IteratingSystem {

    private final SceneManager sceneManager;
    private final Assets assets;

    public SceneSystem(SceneManager sceneManager, Assets assets) {
        super(Family.all(ModelComponent.class).get());
        this.sceneManager = sceneManager;
        this.assets = assets;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(
                Family.all(ModelComponent.class).get(),
                new EntityListener() {
                    public void entityAdded(Entity entity) {
                        return;
                    }

                    public void entityRemoved(Entity entity) {
                        ModelComponent model = Mappers.model.get(entity);
                        if(model.addedToScene && model.scene != null) {
                            sceneManager.removeScene(model.scene);
                            model.addedToScene = false;
                        }
                    }
                }
        );
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        sceneManager.updateViewport(
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
        );
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ModelComponent model = Mappers.model.get(entity);

        if(this.assets.getAssetManager().isLoaded(model.assetPath) && model.scene == null) {
            model.scene = new Scene(this.assets.getGLB(model.assetPath).scene);
            model.requested = true;
        }

        if(!model.requested) {
            this.assets.loadGLB(model.assetPath);
            model.requested = true;
        }

        if(!model.addedToScene && model.scene != null) {
            sceneManager.addScene(model.scene);
            model.addedToScene = true;
        }
    }
}
