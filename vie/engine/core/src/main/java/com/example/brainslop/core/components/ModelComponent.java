package com.example.brainslop.core.components;

import com.badlogic.ashley.core.Component;
import net.mgsx.gltf.scene3d.scene.Scene;

public class ModelComponent implements Component {
    public String assetPath;
    public Scene scene;
    public boolean requested = false;
    public boolean addedToScene = false;
}

