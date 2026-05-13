package com.example.brainslop.core.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Camera;

public class CameraComponent implements Component {
    public transient Camera camera;
    public boolean enabled = true;
    public float fov = 67;
    public float near = 0.1f;
    public float far = 100f;

}
