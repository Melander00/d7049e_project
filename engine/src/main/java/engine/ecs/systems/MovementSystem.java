// engine/ecs/systems/MovementSystem.java
package engine.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import engine.ecs.components.InputComponent;
import engine.ecs.components.TransformComponent;

public class MovementSystem extends IteratingSystem {

    private final ComponentMapper<TransformComponent> tm =
            ComponentMapper.getFor(TransformComponent.class);
    private final ComponentMapper<InputComponent> im =
            ComponentMapper.getFor(InputComponent.class);

    public MovementSystem() {
        super(Family.all(TransformComponent.class, InputComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent transform = tm.get(entity);
        InputComponent input = im.get(entity);
    }

}
