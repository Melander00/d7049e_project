package com.example.brainslop.core.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

import java.util.Vector;

public class TransformComponent implements Component {
    public Vector3 position;
    public Quaternion rotation;
    public Vector3 scale;
}
