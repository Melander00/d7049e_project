# brain-slop game engine

A Java-based virtual interactive environment (VIE) engine for building fast-paced, replayable **3D mobile games**, with a focus on ECS architecture, physics simulation, and performance.

---

## Course Context

This project is developed for the course **D7049E - Virtual Interactive Environments**.

The focus of the course is on designing and implementing the underlying technology of interactive environments, particularly:

* 3D worlds
* simulation systems
* component-based architectures

---

## Team

* Samuel Melander — Chief Observer 🗿
* Irma Palo Hjärtström — Code Monkey 🐒

Repository: https://github.com/Melander00/d7049e_project

---

## Engine Vision

This engine is designed for building **quick, replayable mobile games** with short, intense gameplay loops.

Players:

* continuously acquire upgrades
* make fast decisions between competing options
* experience visible power scaling in real time

The engine emphasizes:

* clarity of feedback
* scalability of entities
* rapid iteration of game concepts

The goal is to enable fast production of multiple similar games using reusable systems.

---

## Target Platform

* Platform: Mobile
* Audience: Ages 8–14
* Tags: Singleplayer, Offline, Fast-paced, Infinite rounds, Microtransactions, Customization

---

## Target Genre / VIE

The engine is optimized for a:

> **3D army controller shooter**

* Player controls a unit moving forward
* Shooting enemies and objects increases army size
* Gameplay consists of continuous scaling and decision-making

---

## Core Scenario

* Player controls a single soldier
* Shooting barrels or enemies provides upgrades or units
* Army grows over time
* Player must choose between upgrade paths
* Failure occurs when scaling is insufficient before boss encounter

---

## Technical Focus

The project focuses on engine architecture rather than game content.

We aim to:

* design a clean ECS (Entity Component System)
* build a modular 3D runtime architecture
* integrate physics simulation (collisions, projectiles, forces)
* support autonomous agents
* enable scripting for behavior definition (Lua)
* support configurable and scalable entity counts
* measure and analyze performance-critical systems

---

## MVP Scope

### Included

* ECS-based engine core (Ashley ECS)
* 3D rendering using LibGDX
* physics integration (Bullet Physics)
* basic movement system (left/right control)
* projectile system (shooting)
* collision detection and response
* simple enemy behavior (placeholder AI)
* one playable test scene
* configurable entity spawning (scaling test)
* camera system
* basic scripting support (Lua or equivalent)
* performance profiling (simulation time)

---

### Not Included

* full gameplay loop
* advanced enemy AI
* army system (only one controllable entity initially)
* advanced UI systems
* content pipeline tools (model editors, etc.)
* polished graphics or animations

---

## Engine vs Game Separation

### Engine (Reusable)

* ECS framework (Ashley)
* movement system
* physics system
* rendering system
* scripting system
* camera system
* level loading
* audio

### Game (Demo)

* soldier and enemy definitions
* upgrade logic
* level design
* progression system

---

## Development Environment

* Language: Java
* Framework: LibGDX
* ECS: Ashley
* Physics: Bullet Physics
* Audio: LibGDX
* Build tool: Gradle
* Scripting: Lua (planned)

---

## Architecture Overview

The engine is built around an ECS architecture:

* **Entities** → IDs representing objects
* **Components** → data (Transform, Physics, Render, Script, etc.)
* **Systems** → logic (movement, collision, rendering, scripting)

Core systems include:

* MovementSystem
* PhysicsSystem
* CollisionSystem
* RenderSystem
* ScriptSystem
* AgentSystem
* ProfilingSystem

The game loop updates systems sequentially and ensures simulation remains under the required time budget.

---

## Data-Driven Design

The engine supports:

* configuration loading
* entity prototypes
* scene bootstrap data

This enables flexible content without modifying engine code.

---

## Engineering Rules

* License: MIT
* Code style:

  * components = data only
  * systems = logic
  * clear separation of engine and demo
* Workflow:

  * feature branches (feat/*, fix/*)
  * pull requests with review
  * small commits, frequent integration

---

## Project Structure

```
docs/        -> design documents
src/
  engine/    -> core engine (ECS, systems)
  demo/      -> test game implementation
assets/      -> models, textures, audio
tests/       -> testing code
tools/       -> scripts/utilities
```


