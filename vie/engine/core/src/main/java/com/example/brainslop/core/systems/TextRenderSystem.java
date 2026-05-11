package com.example.brainslop.core.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.example.brainslop.core.Mappers;
import com.example.brainslop.core.components.TextComponent;
import com.example.brainslop.core.components.TransformComponent;
import net.mgsx.gltf.scene3d.scene.SceneManager;

public class TextRenderSystem extends EntitySystem {

    private final GlyphLayout glyphLayout;

    private final SpriteBatch spriteBatch;
    private final BitmapFont font;
    private final SceneManager sceneManager;

    private final Family family;

    private ImmutableArray<Entity> entities;

    private final Vector3 worldPos = new Vector3();
    private final Vector3 screenPos = new Vector3();

    private final Matrix4 uiMatrix = new Matrix4();

    public TextRenderSystem(
            SceneManager sceneManager
    ) {
        this.sceneManager = sceneManager;

        spriteBatch = new SpriteBatch();
        font = new BitmapFont();
        glyphLayout = new GlyphLayout();

        family = Family.all(
                TextComponent.class,
                TransformComponent.class
        ).get();

    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        entities = engine.getEntitiesFor(family);
    }

    @Override
    public void update(float deltaTime) {

        uiMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.setProjectionMatrix(uiMatrix);


        spriteBatch.begin();

        for (Entity entity : entities) {

            TextComponent text =
                    Mappers.text.get(entity);

            TransformComponent transform =
                    Mappers.transform.get(entity);

            // World position
            worldPos.set(transform.position)
                    .add(text.offsetPosition);

            // Convert to screen coordinates
            screenPos.set(worldPos);

            sceneManager.camera.project(screenPos);

            // Cull behind camera
            if (screenPos.z < 0f)
                continue;

            // Distance scaling
            float dst = sceneManager.camera.position.dst(worldPos);


//            float scale = Math.max(0.5f, 10f / dst);

            float baseHeight = 720f; // design reference resolution
            float screenScale = Gdx.graphics.getHeight() / baseHeight;

            float scale = (10f / dst) * screenScale;


            font.getData().setScale(
                    scale * text.scale
            );

            glyphLayout.setText(font, text.text);

            float drawX = screenPos.x;
            float drawY = screenPos.y;

            if (text.centered) {
                drawX -= glyphLayout.width * 0.5f;
                drawY += glyphLayout.height * 0.5f;
            }

            font.getRegion().getTexture().setFilter(
                    Texture.TextureFilter.Nearest,
                    Texture.TextureFilter.Nearest
            );

            font.draw(
                    spriteBatch,
                    glyphLayout,
                    drawX,
                    drawY
            );
        }

        spriteBatch.end();
    }
}

//public class TextRenderSystem extends EntitySystem {
//
//    private final SpriteBatch spriteBatch;
//    private final BitmapFont font;
//    private final SceneManager sceneManager;
//
//    private ImmutableArray<Entity> entities;
//
//    private final Vector3 worldPos = new Vector3();
//    private final Vector3 screenPos = new Vector3();
//
//    private final Matrix4 transform = new Matrix4();
//
//    public TextRenderSystem(
//            SceneManager sceneManager
//    ) {
//        this.sceneManager = sceneManager;
//
//        spriteBatch = new SpriteBatch();
//        font = new BitmapFont();
//
//
//    }
//
//    @Override
//    public void addedToEngine(Engine engine) {
//        super.addedToEngine(engine);
//        entities = engine.getEntitiesFor(
//                Family.all(
//                        TextComponent.class,
//                        TransformComponent.class
//                ).get()
//        );
//    }
//
//    @Override
//    public void update(float deltaTime) {
//
//        spriteBatch.begin();
//
//        for (Entity entity : entities) {
//
//            TextComponent text =
//                    Mappers.text.get(entity);
//
//            TransformComponent transformComp =
//                    Mappers.transform.get(entity);
//
//            worldPos.set(transformComp.position)
//                    .add(text.offsetPosition);
//
//            // ====================================
//            // BILLBOARD MODE
//            // ====================================
//
//            if (text.faceCamera) {
//
//                screenPos.set(worldPos);
//
//                sceneManager.camera.project(screenPos);
//
//                if (text.cull && screenPos.z < 0f)
//                    continue;
//
//                font.getData().setScale(text.scale);
//
//                font.draw(
//                        spriteBatch,
//                        text.text,
//                        screenPos.x,
//                        screenPos.y
//                );
//            }
//
//            // ====================================
//            // WORLD-ROTATED MODE
//            // ====================================
//
//            else {
//
//                // project world anchor first
//                screenPos.set(worldPos);
//
//                sceneManager.camera.project(screenPos);
//
//                if (text.cull && screenPos.z < 0f)
//                    continue;
//
//                // Build transform matrix
//                transform.idt()
//                        .translate(
//                                screenPos.x,
//                                screenPos.y,
//                                0f
//                        )
//                        .rotate(
//                                Vector3.Z,
//                                transformComp.rotation.getAngleAround(
//                                        0f,
//                                        0f,
//                                        1f
//                                )
//                        )
//                        .scale(
//                                text.scale,
//                                text.scale,
//                                1f
//                        );
//
//                spriteBatch.setTransformMatrix(transform);
//
//                font.draw(
//                        spriteBatch,
//                        text.text,
//                        0,
//                        0
//                );
//
//                // IMPORTANT
//                spriteBatch.setTransformMatrix(
//                        new Matrix4()
//                );
//            }
//        }
//
//        spriteBatch.end();
//    }
//}