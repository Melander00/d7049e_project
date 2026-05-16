package com.example.brainslop.core.components;

import com.badlogic.ashley.core.Component;

public class LifetimeComponent implements Component {
    public transient float timeLived = 0f;
    public float maxTimeAlive;
}
