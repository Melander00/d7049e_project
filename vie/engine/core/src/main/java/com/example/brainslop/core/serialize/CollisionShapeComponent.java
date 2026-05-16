package com.example.brainslop.core.serialize;

import com.badlogic.gdx.math.Vector3;
import com.example.brainslop.core.physics.CollisionShape;

public class CollisionShapeComponent {

    public CollisionShape type;
    public float a;
    public float b;
    public float c;
    public String shapeName = null;
    public Vector3 offsetPosition = new Vector3();

}
