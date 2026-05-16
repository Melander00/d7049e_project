package com.example.brainslop.core.components;

import com.badlogic.ashley.core.Component;

public class AutoShooterComponent implements Component {
    public float shotsPerSecond = 0;
    public transient float timeSinceLastShot = 0;
}
