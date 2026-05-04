package com.example.brainslop.core;

import com.badlogic.gdx.assets.AssetManager;
import net.mgsx.gltf.loaders.glb.GLBAssetLoader;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class Assets {
    private final AssetManager assetManager;
    public Assets(AssetManager assetManager) {
        this.assetManager = assetManager;
        this.assetManager.setLoader(SceneAsset.class, ".glb", new GLBAssetLoader());
    }

    public void loadGLB(String filename) {
        this.assetManager.load(filename, SceneAsset.class);
    }

    public SceneAsset getGLB(String filename) {
        return this.assetManager.get(filename, SceneAsset.class);
    }

    public AssetManager getAssetManager() {
        return this.assetManager;
    }


}
