package com.example.brainslop.core;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.g3d.Model;
import com.example.brainslop.core.components.*;

public class Mappers {

    public static final ComponentMapper<TransformComponent> transform = ComponentMapper.getFor(TransformComponent.class);
    public static final ComponentMapper<ModelComponent> model = ComponentMapper.getFor(ModelComponent.class);
    public static final ComponentMapper<CameraComponent> camera = ComponentMapper.getFor(CameraComponent.class);
    public static final ComponentMapper<InputComponent> input = ComponentMapper.getFor(InputComponent.class);
    public static final ComponentMapper<HealthComponent> health = ComponentMapper.getFor(HealthComponent.class);
    public static final ComponentMapper<PhysicsComponent> physics = ComponentMapper.getFor(PhysicsComponent.class);
    public static final ComponentMapper<CollisionComponent> collision = ComponentMapper.getFor(CollisionComponent.class);
    public static final ComponentMapper<MovementComponent> movement = ComponentMapper.getFor(MovementComponent.class);
    public static final ComponentMapper<AutoShooterComponent> autoShooter = ComponentMapper.getFor(AutoShooterComponent.class);
    public static final ComponentMapper<LifetimeComponent> lifetime = ComponentMapper.getFor(LifetimeComponent.class);
    public static final ComponentMapper<ScriptComponent> script = ComponentMapper.getFor(ScriptComponent.class);
    public static final ComponentMapper<AgentComponent> agent = ComponentMapper.getFor(AgentComponent.class);
    public static final ComponentMapper<GateComponent> gate = ComponentMapper.getFor(GateComponent.class);
    public static final ComponentMapper<PickupComponent> pickup = ComponentMapper.getFor(PickupComponent.class);
    public static final ComponentMapper<TextComponent> text = ComponentMapper.getFor(TextComponent.class);
    public static final ComponentMapper<PlayerComponent> player = ComponentMapper.getFor(PlayerComponent.class);

    public static final ComponentMapper<FreezeRotationComponent> freezeRotation = ComponentMapper.getFor(FreezeRotationComponent.class);

    public static final ComponentMapper<AnimationComponent> animation = ComponentMapper.getFor(AnimationComponent.class);
}
