package com.example.brainslop.core.components;

import com.badlogic.ashley.core.Component;

public class AnimationComponent implements Component {
    public String currentAnimationId = "";
    public float playbackSpeed = 1.0f;
    public boolean loop = true;
}