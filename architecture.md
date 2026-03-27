## ECS Model
- entities are IDs
- components are data-only
- systems operate on component combinations

## architecture/relationship diagram

Entity

  |
  
  +--> TransformComponent
  
  +--> RenderComponent
  
  +--> VelocityComponent
  
  +--> ColliderComponent
  
  +--> ScriptComponent

Systems:
- MovementSystem uses Transform + Velocity
- CollisionSystem uses Transform + Collider
- RenderSystem uses Transform + Render
- ScriptSystem uses Script
- AgentSystem uses Agent + Transform


## ECS should not be used here:

- LibGDX app bootstrap
- asset loading
- camera setup
- global config
- game loop orchestration
- renderer backend integration
