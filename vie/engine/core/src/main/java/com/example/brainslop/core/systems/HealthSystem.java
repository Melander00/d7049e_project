package com.example.brainslop.core.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.example.brainslop.core.ECS;
import com.example.brainslop.core.Mappers;
import com.example.brainslop.core.components.HealthComponent;
import com.example.brainslop.core.messages.DamageTaken;
import com.example.brainslop.core.messages.DestroyRequested;
import com.example.brainslop.core.messages.Message;
import com.example.brainslop.core.messages.MessageListener;
import com.example.brainslop.core.messages.MessageType;

public class HealthSystem extends EntitySystem {

    private ECS ecs;

    private final MessageListener damageTakenListener = new MessageListener() {
        @Override
        public void onMessage(Message message) {
            DamageTaken damageTaken = (DamageTaken) message;
            applyDamage(damageTaken.target, damageTaken.amount);
        }
    };

    @Override
    public void addedToEngine(Engine engine) {
        this.ecs = (ECS) engine;
        ecs.messageManager.subscribe(MessageType.DAMAGE_TAKEN, damageTakenListener);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        if (ecs != null) {
            ecs.messageManager.unsubscribe(MessageType.DAMAGE_TAKEN, damageTakenListener);
            ecs = null;
        }
    }

    private void applyDamage(Entity target, float amount) {
        HealthComponent health = Mappers.health.get(target);

        if (health == null) {
            return;
        }

        health.currentHP -= amount;

        if (health.currentHP <= 0f) {
            health.currentHP = 0f;
            ecs.messageManager.sendMessage(new DestroyRequested(target));
        }
    }
}