package editor;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import engine.ecs.components.InputComponent;
import engine.ecs.components.TransformComponent;
import engine.ecs.systems.MovementSystem;

public class Main {
    public static void main(String[] args) {
        Engine engine = new Engine();

        // add systems
        engine.addSystem(new MovementSystem());

        // create a test entity
        Entity player = engine.createEntity();
        player.add(new TransformComponent());
        player.add(new InputComponent());
        engine.addEntity(player);

        // simulate a few frames
        for (int i = 0; i < 5; i++) {
            engine.update(0.016f); // ~60fps
            System.out.println("frame " + i);
        }
    }
}