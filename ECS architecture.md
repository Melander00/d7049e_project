## ECS Model
- entities are IDs
- components are data-only
- systems operate on component combinations

## ECS diagram

<img width="637" height="396" alt="image" src="https://github.com/user-attachments/assets/65173d99-85c6-4221-97da-15dc6ef48625" />

### Transform
Purpose: stores where the entity is in the world.
Typical data:
x, y
rotation
scale

### Render
Purpose: stores what should be drawn.

Typical data:

sprite/texture ID
visible flag
render layer
maybe flip state

### Velocity
Purpose: stores movement direction and speed.

Typical data:

vx, vy

### Collider

Purpose: stores collision or overlap shape.

Typical data:

width/height or radius
offset
solid vs trigger
collision mask/layer

### Script

Purpose: attaches custom scripted behavior to an entity.

Typical data:

script name / file
script parameters
state variables reference

### Agent

Purpose: stores data for autonomous behavior.

Typical data:

AI state
target position
patrol index
behavior type

### Animation

Purpose: stores sprite animation state.

Typical data:

current animation
current frame
elapsed time
loop flag

### Tag

Purpose: provides a lightweight label or category for an entity.

Typical data:

"player"
"wall"
"npc"
"trigger"
"projectile"

### Lifetime

Purpose: stores how long an entity should exist.

Typical data:

remainingTime

### Health

Purpose: stores hit points or durability.

Typical data:

current HP
max HP
invulnerability timer maybe

### Damage

Purpose: stores how much damage an entity can deal.

Typical data:

damage amount
source entity
damage type maybe


## ECS should not be used here:

- LibGDX app bootstrap
- asset loading
- camera setup
- global config
- game loop orchestration
- renderer backend integration
