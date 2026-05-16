package com.example.brainslop.core.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;

public class MovementComponent implements Component {
    public float movementSpeed = 5f;

    public transient Vector3 moveDir = new Vector3();
}
