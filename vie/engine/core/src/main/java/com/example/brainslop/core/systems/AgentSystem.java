package com.example.brainslop.core.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.example.brainslop.core.Mappers;
import com.example.brainslop.core.components.AgentComponent;
import com.example.brainslop.core.components.PlayerComponent;
import com.example.brainslop.core.components.TransformComponent;

/**
 * Updates targetEntity on each agent each frame.
 * Enemy agents target the nearest player. Ally agents target the nearest enemy agent.
 * Scripts may override targetEntity to implement custom targeting behavior.
 */
public class AgentSystem extends IteratingSystem {

    private ImmutableArray<Entity> playerEntities;
    private ImmutableArray<Entity> enemyEntities;

    private final Vector3 tmp = new Vector3();

    public AgentSystem() {
        super(Family.all(AgentComponent.class, TransformComponent.class).get());
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        playerEntities = engine.getEntitiesFor(
                Family.all(PlayerComponent.class, TransformComponent.class).get()
        );
        enemyEntities = engine.getEntitiesFor(
                Family.all(AgentComponent.class, TransformComponent.class).get()
        );
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AgentComponent agent = Mappers.agent.get(entity);

        if (agent.faction.equals("enemy")) {
            agent.targetEntity = findNearest(entity, playerEntities);
        } else if (agent.faction.equals("ally")) {
            agent.targetEntity = findNearest(entity, enemyEntities);
        }
//        if (agent.targetEntity != null) {
//            Gdx.app.log("AgentSystem", "Agent has target: " + agent.targetEntity);
//        } else {
//            Gdx.app.log("AgentSystem", "Agent has no target");
//        }

    }

    private Entity findNearest(Entity from, ImmutableArray<Entity> candidates) {
        TransformComponent fromTransform = Mappers.transform.get(from);
        Entity nearest = null;
        float nearestDist = Float.MAX_VALUE;

        for (Entity candidate : candidates) {
            if (candidate == from) {
                continue;
            }
            TransformComponent t = Mappers.transform.get(candidate);
            float dist = tmp.set(t.position).sub(fromTransform.position).len2();
            if (dist < nearestDist) {
                nearestDist = dist;
                nearest = candidate;
            }
        }
        return nearest;
    }
}