package com.example.brainslop.core.messages;

import com.badlogic.ashley.core.Entity;

public class CollisionExit implements Message {

    public final Entity entityA;
    public final Entity entityB;

    public CollisionExit(Entity entityA, Entity entityB) {
        this.entityA = entityA;
        this.entityB = entityB;
    }

    @Override
    public MessageType getType() {
        return MessageType.COLLISION_EXIT;
    }
}
