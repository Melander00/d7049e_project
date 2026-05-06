package com.example.brainslop.core;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCylinderShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.example.brainslop.core.components.*;
import com.example.brainslop.core.physics.PhysicsFactory;
import com.example.brainslop.core.systems.*;
import com.example.brainslop.core.util.ExternalAssetsResolver;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import com.example.brainslop.core.messages.*;



import java.util.List;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    Assets assets;
    SceneManager sceneManager;

    World world;
    ECS engine;

    MessageManager messageManager;
    btDynamicsWorld btWorld;

    ExternalAssetsResolver fileResolver;

    private static final boolean SHOW_COLLISION_WIREFRAMES = true;

    private DebugDrawer debugDrawer;

    @Override
    public void create() {
        Bullet.init();

        fileResolver = new ExternalAssetsResolver(".");

        assets = new Assets(new AssetManager(fileResolver));
        sceneManager = new SceneManager();
        world = new World(sceneManager);
        world.setupCubeMap();

        messageManager = new MessageManager();



        PhysicsSystem physicsSystem = new PhysicsSystem(60, messageManager);
        btWorld = physicsSystem.getWorld();
        debugDrawer = new DebugDrawer();
        if(SHOW_COLLISION_WIREFRAMES) {
            debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_DrawWireframe);
        }
        btWorld.setDebugDrawer(debugDrawer);

        List<EntitySystem> systems = List.of(
                new SceneSystem(sceneManager, assets), // Handles new model instances.

                // LightSystem
                new KeyboardInputSystem(),
                new InputMovementSystem(),
                new PhysicsMovementSystem(),

                new ScriptSystem(fileResolver),
                new AutoShooterSystem(),
                physicsSystem,
                new LifetimeSystem(),

                new CameraSystem(sceneManager),
                new ModelTransformSystem(), // Prepare models for rendering
                new RenderSystem(sceneManager) // Renders models
        );

        engine = new ECS(systems);
        engine.loadSystems();

        assets.loadGLB("model/sahur.glb");
        assets.loadGLB("model/ground.glb");
        assets.loadGLB("model/bullet.glb");

        addPlayer();
        addGround();
        addCamera();
    }

    private void addPlayer() {
        Entity entity = engine.createEntity();

        TransformComponent c = new TransformComponent();
//        c.scale.scl(10);
        entity.add(c);

        entity.add(new InputComponent());
        entity.add(new MovementComponent());

        ModelComponent m = new ModelComponent();
//        m.assetPath = "model/soldier.glb";
        m.assetPath = "model/sahur.glb";
        entity.add(m);

        AutoShooterComponent autoShooterComponent = new AutoShooterComponent();
        autoShooterComponent.shotsPerSecond = 20;
        entity.add(autoShooterComponent);

        btCollisionShape cylinder = new btCylinderShape(new Vector3(1f, 2f, 1f));
        PhysicsComponent pc = PhysicsFactory.createComponent(entity, 1f, cylinder);
        entity.add(pc);

        HealthComponent health = new HealthComponent();
        health.currentHP = 100f;
        health.maxHP = 100f;
        entity.add(health);
        entity.add(new ScriptComponent("lua/test.lua"));
    }


    private void addGround() {
        btCollisionShape groundShape = new btBoxShape(new Vector3(10, 1, 8));

        Entity ground = engine.createEntity();

        TransformComponent t = new TransformComponent();
        t.position.set(0, -5, 0);
        ground.add(t);

        ModelComponent m = new ModelComponent();
        m.assetPath = "model/ground.glb";
        ground.add(m);

        CollisionComponent c = new CollisionComponent();
        c.shape = groundShape;
        ground.add(c);
    }

    private void addCamera() {
        Entity cam = engine.createEntity();
        CameraComponent camComp = new CameraComponent();
        camComp.camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camComp.camera.near = 0.1f;
        camComp.camera.far = 100f;
        camComp.enabled = true;
        cam.add(camComp);
        TransformComponent t = new TransformComponent();
        t.position.set(0,9,-9);
        t.rotation.setEulerAngles(180,-30,0);
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

        float dt = Gdx.graphics.getDeltaTime();

        engine.update(dt);

        debugDrawer.begin(sceneManager.camera);
        btWorld.debugDrawWorld();
        debugDrawer.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        sceneManager.dispose();
        engine.dispose();
    }
}
