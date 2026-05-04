package com.example.brainslop.core.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import net.mgsx.gltf.scene3d.scene.SceneManager;

public class RenderSystem extends EntitySystem {
    private final SceneManager sceneManager;

    public RenderSystem(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @Override
    public void update(float deltaTime) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        sceneManager.update(deltaTime);
        sceneManager.renderShadows();
        sceneManager.renderColors();
    }
}
