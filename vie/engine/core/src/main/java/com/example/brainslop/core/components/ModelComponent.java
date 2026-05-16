package com.example.brainslop.core.components;

import com.badlogic.ashley.core.Component;
import net.mgsx.gltf.scene3d.scene.Scene;

public class ModelComponent implements Component {
    public String assetPath;
    public transient Scene scene;
    public transient boolean requested = false;
    public transient boolean addedToScene = false;
}

