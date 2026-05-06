package com.example.brainslop.core.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.example.brainslop.core.ECS;
import com.example.brainslop.core.components.ScriptComponent;
import com.example.brainslop.core.scripts.ScriptContext;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;
import com.badlogic.gdx.Gdx;

/**
 * Runs Lua scripts attached to entities via ScriptComponent.
 * Each script runs in its own isolated LuaTable environment — scripts cannot
 * access each other's state. ScriptContext is coerced to a Lua value and injected
 * as "context" into each script's environment, giving scripts controlled access
 * to engine components and messages without exposing engine internals.
 * Scripts must define an update(dt) function. If absent, the entity is skipped silently.
 */
public class ScriptSystem extends IteratingSystem {

    private final ECS ecs;
    private final Globals globals;

    public ScriptSystem(ECS ecs) {
        super(Family.all(ScriptComponent.class).get());
        this.ecs = ecs;
        this.globals = JsePlatform.standardGlobals();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ScriptComponent script = ecs.getComponent(entity, ScriptComponent.class);

        if (script.environment == null) {
            loadScript(script);
            if (script.environment == null) {
                return;
            }
        }

        ScriptContext context = new ScriptContext(entity, ecs);
        script.environment.set("context", CoerceJavaToLua.coerce(context));
        script.environment.set("dt", LuaValue.valueOf(deltaTime));

        LuaValue update = script.environment.get("update");
        if (!update.isnil()) {
            update.call();
        }
    }

    private void loadScript(ScriptComponent script) {
        try {
            LuaTable env = new LuaTable();
            LuaTable meta = new LuaTable();
            meta.set(LuaValue.INDEX, globals);
            env.setmetatable(meta);

            LuaValue chunk = globals.load(
                    new java.io.FileReader(script.scriptPath),
                    script.scriptPath,
                    env
            );
            chunk.call();
            script.environment = env;
        } catch (java.io.FileNotFoundException e) {
            Gdx.app.error("ScriptSystem", "Script not found: " + script.scriptPath);
        }
    }
}
