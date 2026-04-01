# Code Style

This document defines the code style and project structure for the brain-slop engine project.

## General

- Language: Java
- IDE: IntelliJ IDEA
- Style baseline: standard Java style with consistent formatting
- Goal: readable, predictable, easy to review

## Formatting

- Use 4 spaces for indentation
- Keep line length reasonable, around 100 to 120 characters
- Use one class per file
- Always use braces, even for one-line `if` statements or loops
- Let IntelliJ auto-format code before committing

## Naming

### Classes and interfaces
Use `PascalCase`

Examples:
- `MovementSystem`
- `PhysicsComponent`
- `ModelLoader`

### Methods and variables
Use `camelCase`

Examples:
- `updatePosition`
- `deltaTime`
- `loadScene`

### Constants
Use `UPPER_SNAKE_CASE`

Examples:
- `MAX_SPEED`
- `DEFAULT_GRAVITY`

### Packages
Use lowercase only

Examples:
- `engine.ecs.systems`
- `engine.physics`

## Class layout

Use this order inside classes:

1. Constants
2. Fields
3. Constructors
4. Public methods
5. Private methods

Example:

```java
public class MovementSystem extends EntitySystem {

    private final ComponentMapper<TransformComponent> transformMapper;

    public MovementSystem() {
        this.transformMapper = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    public void update(float deltaTime) {
        // System logic here
    }

    private void moveEntity(Entity entity, float deltaTime) {
        // Helper logic here
    }
}
