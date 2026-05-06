package com.example.brainslop.core.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.Gdx;
import com.example.brainslop.core.ECS;
import com.example.brainslop.core.Mappers;
import com.example.brainslop.core.components.ScriptComponent;
import com.example.brainslop.core.scripts.ScriptContext;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.FileReader;
import java.io.IOException;

/**
 * Runs Lua scripts attached to entities via ScriptComponent.
 * Each script gets its own LuaTable environment, so script-local global variables
 * are isolated per entity.
 * ScriptContext is injected as "context". It is the script's controlled access
 * point to allowed gameplay components, entity creation/destruction, and messages.
 * Engine systems and Bullet physics internals are not exposed through ScriptContext.
 * Scripts may optionally define:
 * - update(dt): called during the variable frame update
 * - fixedUpdate(dt): called during the fixed simulation update
 * If a function is missing, it is skipped.
 */
public class ScriptSystem extends IteratingSystem {

    private final Globals globals;
    private final FileHandleResolver resolver;
    private String functionToCall = "update";

//    public ScriptSystem(ECS ecs, FileHandleResolver resolver) {
    public ScriptSystem(FileHandleResolver resolver) {
        super(Family.all(ScriptComponent.class).get());
        this.globals = JsePlatform.standardGlobals();
        this.resolver = resolver;

        // This system is called manually from frame update and fixed update.
        setProcessing(false);
    }

    @Override
    public void update(float deltaTime) {
        functionToCall = "update";
        super.update(deltaTime);
    }

    public void fixedUpdate(float fixedDeltaTime) {
        functionToCall = "fixedUpdate";
        super.update(fixedDeltaTime);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ScriptComponent script = Mappers.script.get(entity);

        if (script.environment == null) {
            loadScript(script);
        }

        if (script.environment == null) {
            return;
        }

        ECS ecs = (ECS) getEngine();

        ScriptContext context = new ScriptContext(entity, ecs);
        script.environment.set("context", CoerceJavaToLua.coerce(context));
//        script.environment.set("dt", LuaValue.valueOf(deltaTime));

        callScriptFunction(script, functionToCall, deltaTime);
    }

    private void callScriptFunction(ScriptComponent script, String functionName, float deltaTime) {
        LuaValue function = script.environment.get(functionName);

        if (function.isnil()) {
            return;
        }

        try {
            function.call(LuaValue.valueOf(deltaTime));
        } catch (LuaError e) {
            Gdx.app.error(
                    "ScriptSystem",
                    "Error in " + functionName + " for script: " + script.scriptPath,
                    e
            );
        }
    }

    private void loadScript(ScriptComponent script) {
        try {
            LuaTable env = new LuaTable();
            LuaTable meta = new LuaTable();
            meta.set(LuaValue.INDEX, globals);
            env.setmetatable(meta);

            FileHandle scriptFile = resolver.resolve(script.scriptPath);

            LuaValue chunk = globals.load(
                    scriptFile.read(),
                    script.scriptPath,
                    "t",
                    env
            );
            chunk.call();
            script.environment = env;
        } catch (Exception e) {
            Gdx.app.error("ScriptSystem", "Script not found: " + script.scriptPath + "\t| " + e);
        }
    }
}
