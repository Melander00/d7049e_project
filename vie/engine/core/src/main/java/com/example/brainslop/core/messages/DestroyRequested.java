package com.example.brainslop.core.messages;

import com.badlogic.ashley.core.Entity;

public class DestroyRequested implements Message {

    public final Entity entity;

    public DestroyRequested(Entity entity) {
        this.entity = entity;
    }

    @Override
    public MessageType getType() {
        return MessageType.DESTROY_REQUESTED;
    }
}
