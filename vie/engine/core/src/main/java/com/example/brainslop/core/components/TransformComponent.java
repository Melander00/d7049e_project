package com.example.brainslop.core.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class TransformComponent implements Component {
    public Vector3 position = new Vector3();
    public Quaternion rotation = new Quaternion();
    public Vector3 scale = new Vector3(1,1,1);
}
