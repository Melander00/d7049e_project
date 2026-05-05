package com.example.brainslop.core.messages;
import com.badlogic.ashley.core.Entity;

public class CollisionEntered implements Message {

    public final Entity entityA;
    public final Entity entityB;

    public CollisionEntered(Entity entityA, Entity entityB) {
        this.entityA = entityA;
        this.entityB = entityB;
    }

    @Override
    public MessageType getType() {
        return MessageType.COLLISION_ENTERED;
    }
}
