package com.example.brainslop.core.messages;
import com.badlogic.ashley.core.Entity;

public class DamageTaken implements Message {

    public final Entity target;
    public final float amount;
    public final Entity source;

    public DamageTaken(Entity target, float amount, Entity source) {
        this.target = target;
        this.amount = amount;
        this.source = source;
    }

    @Override
    public MessageType getType() {
        return MessageType.DAMAGE_TAKEN;
    }
}
