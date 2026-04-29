package com.example.brainslop.core;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.example.brainslop.core.components.TransformComponent;
import com.example.brainslop.core.systems.MovementSystem;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {

    Engine engine;
    Entity entity;

    @Override
    public void create() {
        engine = new Engine();
        engine.addSystem(new MovementSystem());

        entity = new Entity();
        TransformComponent c = new TransformComponent();
        entity.add(c);

        engine.addEntity(entity);
    }

    @Override
    public void render() {
        engine.update(Gdx.graphics.getDeltaTime());

//        TransformComponent t = Mapper.transform.get(entity);
//
//        System.out.println(t.position);
    }
}
