package com.example.brainslop.core.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class TextComponent implements Component {
    public Vector3 offsetPosition = new Vector3();
    public Quaternion offsetRotation = new Quaternion();
    public String text = "Placeholder";

    public float scale = 1f;
    public boolean faceCamera = false;
    public boolean cull = true;

    public boolean centered = true;
}
