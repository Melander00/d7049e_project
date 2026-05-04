package com.example.brainslop.core;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.example.brainslop.core.components.CameraComponent;
import com.example.brainslop.core.components.ModelComponent;
import com.example.brainslop.core.components.InputComponent;
import com.example.brainslop.core.components.TransformComponent;
import com.example.brainslop.core.systems.CameraSystem;
import com.example.brainslop.core.systems.ModelTransformSystem;
import com.example.brainslop.core.systems.RenderSystem;
import com.example.brainslop.core.systems.SceneSystem;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import java.util.List;
import com.example.brainslop.core.systems.KeyboardInputSystem;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    Assets assets;
    SceneManager sceneManager;

    World world;
    ECS engine;

    private boolean initialized = false;

    @Override
    public void create() {
        Bullet.init();
        assets = new Assets(new AssetManager());
        sceneManager = new SceneManager();
        world = new World(sceneManager);
        world.setupCubeMap();

        List<EntitySystem> systems = List.of(
                new SceneSystem(sceneManager, assets), // Handles new model instances.
                // LightSystem
                // InputSystem
                new KeyboardInputSystem(),
                new CameraSystem(sceneManager),
                new ModelTransformSystem(), // Prepare models for rendering
                new RenderSystem(sceneManager) // Renders models
        );

        engine = new ECS(systems);
        engine.loadSystems();

        assets.loadGLB("model/sahur.glb");

        Entity entity = engine.createEntity();
        TransformComponent c = new TransformComponent();
        entity.add(new InputComponent());
        entity.add(c);
        ModelComponent m = new ModelComponent();
        m.assetPath = "model/sahur.glb";
        entity.add(m);

        Entity cam = engine.createEntity();
        CameraComponent camComp = new CameraComponent();
        camComp.camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camComp.camera.near = 0.1f;
        camComp.camera.far = 100f;
        camComp.enabled = true;
        cam.add(camComp);
        TransformComponent t = new TransformComponent();
        t.position.set(-3,3,0);
        t.rotation.setEulerAngles(-90,-30,0);
        cam.add(t);

    }

    @Override
    public void render() {
        boolean hasLoaded = assets.getAssetManager().update();

        if(!hasLoaded) {
            // We haven't loaded all the assets yet.
            float progress = assets.getAssetManager().getProgress();
            System.out.printf("Loading assets %f%n", progress);
            // Display a loading screen?
            return;
        }

        engine.update(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        super.dispose();
        sceneManager.dispose();
        engine.dispose();
    }
}
