package com.example.brainslop.core.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.example.brainslop.core.components.InputComponent;

import com.badlogic.gdx.Gdx;

/**
 * Abstract base system that reads input each frame and writes abstract signals
 * into each entity's InputComponent. Subclasses decide how to read
 * input by implementing the four abstract read methods.
 * The game layer selects and registers the appropriate subclass at startup.
 * Nothing else in the engine needs to change when the input source changes.
 */


public abstract class InputSystem extends IteratingSystem {

    public static final int PRIORITY = 0;

    private final ComponentMapper<InputComponent> inputMapper;

    public InputSystem() {
        super(Family.all(InputComponent.class).get(), PRIORITY);
        this.inputMapper = ComponentMapper.getFor(InputComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        InputComponent input = inputMapper.get(entity);
        input.axisX = readAxisX();
        input.axisY = readAxisY();
        input.action1 = readAction1();
        input.action2 = readAction2();

        // TEMPORARY — remove after testing
        Gdx.app.log("Input", "axisX: " + input.axisX + " axisY: " + input.axisY + " action1: " + input.action1);
    }

    protected abstract float readAxisX();
    protected abstract float readAxisY();
    protected abstract boolean readAction1();
    protected abstract boolean readAction2();

}
