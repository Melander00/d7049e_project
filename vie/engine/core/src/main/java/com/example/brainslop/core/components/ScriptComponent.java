package com.example.brainslop.core.components;

import com.badlogic.ashley.core.Component;
import org.luaj.vm2.LuaTable;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores script state for one entity.
 * ScriptSystem loads the script and initializes the environment on first update.
 */
public class ScriptComponent implements Component {

    public final String scriptPath;

    public transient LuaTable environment;

    public transient final Map<String, Object> parameters = new HashMap<>();

    public ScriptComponent(String scriptPath) {
        this.scriptPath = scriptPath;
    }

    public ScriptComponent() {
        this.scriptPath = "";
    }
}