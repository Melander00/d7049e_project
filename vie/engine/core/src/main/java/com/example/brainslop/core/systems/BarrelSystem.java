package com.example.brainslop.core.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.example.brainslop.core.components.AutoShooterComponent;
import com.example.brainslop.core.components.BarrelComponent;
import com.example.brainslop.core.components.DamageComponent;
import com.example.brainslop.core.components.HealthComponent;
import com.example.brainslop.core.messages.*;

public class BarrelSystem extends EntitySystem {
    private final MessageManager messageManager;

    private ImmutableArray<Entity> autoShooters;

    private final MessageListener listener = new MessageListener() {
        @Override
        public void onMessage(Message message) {
            if(message instanceof DestroyRequested msg) {

                BarrelComponent barrel = msg.entity.getComponent(BarrelComponent.class);

                if(barrel == null) {
                    return;
                }

                for (Entity entity : autoShooters) {
                    AutoShooterComponent asc = entity.getComponent(AutoShooterComponent.class);

                    asc.shotsPerSecond += barrel.attackSpeedUp;
                }

            }
        }
    };

    public BarrelSystem(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        autoShooters = engine.getEntitiesFor(Family.all(AutoShooterComponent.class).get());
        this.messageManager.subscribe(MessageType.DESTROY_REQUESTED, listener);
    }
}
