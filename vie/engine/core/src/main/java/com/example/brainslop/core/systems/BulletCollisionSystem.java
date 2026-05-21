package com.example.brainslop.core.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.example.brainslop.core.ECS;
import com.example.brainslop.core.components.DamageComponent;
import com.example.brainslop.core.components.HealthComponent;
import com.example.brainslop.core.messages.*;

public class BulletCollisionSystem extends EntitySystem {


    private final MessageManager messageManager;

    private final MessageListener listener = new MessageListener() {
        @Override
        public void onMessage(Message message) {
            if(message instanceof  CollisionEntered col) {

                DamageComponent dmg = col.entityA.getComponent(DamageComponent.class);
                DamageComponent dmg2 = col.entityB.getComponent(DamageComponent.class);

                if(dmg != null) {
                    HealthComponent h = col.entityB.getComponent(HealthComponent.class);
                    messageManager.sendMessage(new DamageTaken(col.entityB, dmg.damage, col.entityA));
                    if(h != null) {
                        messageManager.sendMessage(new DestroyRequested(col.entityA));
                    }
                }

                if(dmg2 != null) {
                    HealthComponent h = col.entityA.getComponent(HealthComponent.class);
                    messageManager.sendMessage(new DamageTaken(col.entityA, dmg2.damage, col.entityB));
                    if(h != null) {
                        messageManager.sendMessage(new DestroyRequested(col.entityB));
                    }
                }
            }
        }
    };

    public BulletCollisionSystem(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.messageManager.subscribe(MessageType.COLLISION_ENTERED, listener);
    }
}
