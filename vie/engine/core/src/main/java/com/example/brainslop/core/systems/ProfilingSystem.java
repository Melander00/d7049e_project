package com.example.brainslop.core.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

public class ProfilingSystem extends EntitySystem {

    private final SpriteBatch spriteBatch;
    private final BitmapFont font;
    private final GlyphLayout glyphLayout;

    private final Matrix4 uiMatrix = new Matrix4();

    public ProfilingSystem() {
        this.spriteBatch = new SpriteBatch();
        this.font = new BitmapFont();
        this.glyphLayout = new GlyphLayout();
    }

    @Override
    public void update(float deltaTime) {
        uiMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.setProjectionMatrix(uiMatrix);

        spriteBatch.begin();

        int fps = Gdx.graphics.getFramesPerSecond();

        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        glyphLayout.setText(font, "FPS: " + fps);

        float drawX = width - glyphLayout.width - 10;
        float drawY = height - glyphLayout.height;

        font.draw(
                spriteBatch,
                glyphLayout,
                drawX,
                drawY
        );

        spriteBatch.end();
    }
}
