ECS Overview

Entities are unique IDs.
Components store data.
Systems process entities with matching component sets.

--------------------------------------------------

| Component  | Player | Enemy | Projectile | Barrel |
|------------|--------|-------|------------|--------|
| Transform  | ✓      | ✓     | ✓          | ✓      |
| Render     | ✓      | ✓     | ✓          | ✓      |
| Physics    | ✓      | ✓     | ✓          | ✓      |
| Velocity   | ✓      | ✓     | ✓          | -      |
| Collider   | ✓      | ✓     | ✓          | ✓      |
| Script     | -      | ✓     | -          | ✓      |
| Agent      | -      | ✓     | -          | -      |

--------------------------------------------------

### Core Components

#### TransformComponent
- position (x, y, z)
- rotation
- scale

#### RenderComponent
- model reference
- material / texture
- visibility flag

#### PhysicsComponent
- rigid body reference (Bullet)
- mass
- gravity flag

#### VelocityComponent
- velocity vector

#### ColliderComponent
- collision shape
- trigger flag

#### ScriptComponent
- script reference (Lua or identifier)

#### AgentComponent
- behavior type
- state 

--------------------------------------------------

### Core Systems

#### MovementSystem
- updates position using velocity

#### PhysicsSystem
- synchronizes ECS with Bullet Physics
- applies forces and gravity

#### CollisionSystem
- detects collisions
- triggers responses

#### RenderSystem
- reads Transform and Render components
- submits data to LibGDX renderer

#### ScriptSystem
- executes scripts
- handles behaviors and events

#### AgentSystem
- controls enemy behavior

#### ProfilingSystem
- measures update time per system
