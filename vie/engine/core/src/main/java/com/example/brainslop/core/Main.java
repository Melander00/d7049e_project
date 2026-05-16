package com.example.brainslop.core;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCylinderShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonWriter;
import com.example.brainslop.core.components.*;
import com.example.brainslop.core.physics.CollisionShape;
import com.example.brainslop.core.serialize.*;
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
    public ECS engine;

    MessageManager messageManager;
    btDynamicsWorld btWorld;

    ExternalAssetsResolver fileResolver;

    private DebugDrawer debugDrawer;

    Json json = new Json();

    @Override
    public void create() {
        Bullet.init();

        fileResolver = new ExternalAssetsResolver(".");

        assets = new Assets(new AssetManager(fileResolver));
        sceneManager = new SceneManager(60);
        world = new World(sceneManager);
        world.setupCubeMap();

        messageManager = new MessageManager();

        Config config = loadConfig(fileResolver);

        PhysicsSystem physicsSystem = new PhysicsSystem(messageManager); // step frequency is deprecated in favor of global fixedUpdate
        List<EntitySystem> systems = List.of(
                new SceneSystem(sceneManager, assets), // Handles new model instances.

                // LightSystem
                new KeyboardInputSystem(),
                new InputMovementSystem(),
                new PhysicsMovementSystem(),

                new FreezeRotationSystem(),
                new ScriptSystem(fileResolver),
                new AutoShooterSystem(),
                physicsSystem,
                new LifetimeSystem(),

                new CameraSystem(sceneManager),
                new ModelTransformSystem(), // Prepare models for rendering
                new RenderSystem(sceneManager), // Renders models
                new AnimationSystem(),
                new TextRenderSystem(sceneManager), // Renders in-world text
                new ProfilingSystem()
                // HUDRenderSystem
        );

        engine = new ECS(systems, config.fixedTimeFrequency, messageManager);
        engine.loadSystems();

        preloadAssetsList(fileResolver, assets);
        createEntities(fileResolver);

//        printEntity(createCamera());
//        printEntity(addPlayer());
//        printEntity(addGround());
//        Entity player = addPlayer();
//        engine.addEntity(player);

        btWorld = physicsSystem.getWorld();
        debugDrawer = new DebugDrawer();
        if(config.debugCollisionWireframe) {
            debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_DrawWireframe);
        }
        btWorld.setDebugDrawer(debugDrawer);
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
        assets.getAssetManager().dispose();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        sceneManager.updateViewport(width, height);
    }


    private Config loadConfig(FileHandleResolver resolver) {
        try {
            FileHandle configFile = resolver.resolve("config.json");
            return json.fromJson(Config.class, configFile);
        } catch (RuntimeException e) {
            return new Config();
        }

    }

    private void preloadAssetsList(FileHandleResolver resolver, Assets assets) {
        try {
            FileHandle glbAssets = resolver.resolve("glb-assets.json");
            List<String> assetPaths = json.fromJson(AssetPaths.class, glbAssets).paths;
            for (String path : assetPaths) {
                assets.loadGLB(path);
            }
        } catch (GdxRuntimeException ignored) {

        }
    }

    private void createEntities(FileHandleResolver resolver) {
        try {
            FileHandle entitiesFile = resolver.resolve("entities.jsonl");
            String data = entitiesFile.readString();
            String[] lines = data.split("\\r?\\n");
            for (String line : lines) {
                if(line.isBlank()) continue;
                GameObject object = json.fromJson(GameObject.class, line);
                addGameObject(object);
            }
        } catch (GdxRuntimeException e) {
            throw e;
        }
    }

    private void addGameObject(GameObject object) {
        Entity entity = engine.createEntity();
        for (Component component : object.components) {
            entity.add(component);
        }
    }

    private void printEntity(Entity entity) {
        GameObject obj = new GameObject();
        ImmutableArray<Component> comps = entity.getComponents();
        json.setUsePrototypes(false);
        for (Component comp : comps) {
            obj.components.add(comp);
            try {
//                System.out.println(json.toJson(comp));
            } catch (Throwable t) {
                System.out.println(comp.getClass());
                t.printStackTrace();
            }
        }
        String s = json.toJson(obj);
        System.out.println(s);

//        engine.addEntity(entity);
    }



    private Entity addPlayer() {
        Entity entity = new Entity();

        TransformComponent c = new TransformComponent();
//        c.scale.scl(10);
        entity.add(c);

        entity.add(new InputComponent());
        entity.add(new MovementComponent());
        entity.add(new PlayerComponent());

        ModelComponent m = new ModelComponent();
//        m.assetPath = "model/soldier.glb";
        m.assetPath = "model/sahur.glb";
        entity.add(m);

        AutoShooterComponent autoShooterComponent = new AutoShooterComponent();
        autoShooterComponent.shotsPerSecond = 20;
        entity.add(autoShooterComponent);

        CollisionShapeComponent shape = new CollisionShapeComponent();
        shape.type = CollisionShape.CYLINDER;
        shape.a = 1;
        shape.b = 2;
        shape.c = 1;
        PhysicsComponent pc = PhysicsFactory.createComponent(entity, 1f, shape);
        entity.add(pc);

        HealthComponent health = new HealthComponent();
        health.currentHP = 100f;
        health.maxHP = 100f;
        entity.add(health);
        entity.add(new ScriptComponent("lua/test.lua"));

        TextComponent text = new TextComponent();
        text.text = "Tung Tung Tung Sahur";
        text.scale = 2f;
        text.offsetPosition.set(0, 5, 0);
        entity.add(text);


        FreezeRotationComponent freeze = new FreezeRotationComponent();
        freeze.freezePitch = true;
        freeze.freezeRoll = true;
        entity.add(freeze);

        AnimationComponent anim = new AnimationComponent();
        anim.currentAnimationId = "Armature|mixamo.com|Layer0"; // replace after checking logs
        anim.loop = true;
        anim.playbackSpeed = 1.0f;
        entity.add(anim);

        return entity;
    }


    private Entity addGround() {
        Entity ground = new Entity();

        TransformComponent t = new TransformComponent();
        t.position.set(0, -5, 0);
        ground.add(t);

        ModelComponent m = new ModelComponent();
        m.assetPath = "model/ground.glb";
        ground.add(m);

        CollisionComponent c = new CollisionComponent();

        c.shape = new CollisionShapeComponent();
        c.shape.type = CollisionShape.BOX;
        c.shape.a = 10;
        c.shape.b = 1;
        c.shape.c = 8;
        ground.add(c);

        return ground;
    }

    private Entity createCamera() {
        Entity cam = new Entity();
        CameraComponent camComp = new CameraComponent();
        camComp.fov = 70;
        camComp.near = 0.1f;
        camComp.far = 100f;
        camComp.enabled = true;
        cam.add(camComp);

        TransformComponent t = new TransformComponent();
        t.position.set(0,9,-9);
        t.rotation.setEulerAngles(180,-30,0);
        cam.add(t);

        return cam;
    }



    private void addEnemy() {
        Entity enemy = engine.createEntity();

        TransformComponent t = new TransformComponent();
        t.position.set(5, 0, 5);
        enemy.add(t);

        AgentComponent agent = new AgentComponent();
        agent.faction = "enemy";
        enemy.add(agent);

        ModelComponent model = new ModelComponent();
        model.assetPath="model/sahur.glb";
        enemy.add(model);

        engine.addSystem(new AgentSystem());
    }
}
