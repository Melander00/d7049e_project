package com.example.brainslop.core;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.g3d.Model;
import com.example.brainslop.core.components.CameraComponent;
import com.example.brainslop.core.components.InputComponent;
import com.example.brainslop.core.components.ModelComponent;
import com.example.brainslop.core.components.TransformComponent;

public class Mappers {

    public static final ComponentMapper<TransformComponent> transform = ComponentMapper.getFor(TransformComponent.class);
    public static final ComponentMapper<ModelComponent> model = ComponentMapper.getFor(ModelComponent.class);
    public static final ComponentMapper<CameraComponent> camera = ComponentMapper.getFor(CameraComponent.class);
    public static final ComponentMapper<InputComponent> input = ComponentMapper.getFor(InputComponent.class);



}
