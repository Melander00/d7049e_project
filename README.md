# d7049e_project

A Java-based virtual interactive environment (VIE) engine for fixed-room 2D dungeon-style scenes, with a focus on ECS architecture, layered rendering/grouping, and performance.

---

## Course Context

This project is developed for the course D7049E - Virtual Interactive Environments.

The focus of the course is on designing and implementing the underlying technology of interactive environments, rather than building a complete or polished game.

---

## Project Idea (VIE Concept)

We are building a lightweight 2D game engine for creating fixed-room dungeon environments inspired by games such as Binding of Isaac and Undertale.

The environment:
- uses a fixed-size room (canvas)
- supports **layered scene composition** (e.g. floor, walls, entities, projectiles, effects)
- supports **layered rendering and entity grouping** for controlling draw order and interaction separation
- allows users to define behavior through **scripts**
- contains autonomous agents that interact with the player
- allows dynamic and configurable object counts

The demo/game itself is minimal and only used to validate engine functionality.

---

## Problem Statement

Many small-scale game projects suffer from tightly coupled logic, inconsistent asset usage, and difficulty organizing multiple interacting elements (e.g. movement, effects, collisions, projectiles).

This project explores a structured engine approach where:
- logic is separated using ECS
- entities are organized using layers for clear rendering and grouping
- behaviors can be composed through systems and scripting
- runtime behavior can be modified without changing core engine code

---

## Technical Focus

The main focus is the engine architecture.

We aim to:
- design a clean ECS (Entity Component System)
- build a modular runtime architecture
- support layered rendering and entity grouping
- integrate a simple scripting system for behavior definition
- support autonomous agents
- handle configurable object counts
- measure and analyze performance-critical systems

---

## MVP Scope

This project is intentionally scoped as a minimal engine MVP.

### Included
- ECS-based engine core
- one fixed-room 2D test scene
- layered scene composition (background, entities, projectiles, effects)
- basic rendering using LibGDX
- simple scripting support (behavior layer)
- one autonomous agent behavior
- configurable entity count for scalability testing
- basic collision handling (library or simple implementation)
- simulation timing and profiling support

### Not Included
- full game content
- advanced editor tools
- multiple levels or world systems
- complex AI systems
- advanced scripting language features
- polished UI or toolchain features

---

## Scripting

A core part of the project is allowing behaviors to be defined outside of core engine logic.

This includes:
- attaching scripts to entities
- defining behavior such as movement, interaction, or effects
- enabling flexible composition of behaviors without modifying engine systems

The goal is to explore how scripting and ECS can work together to create flexible and reusable behavior systems.

---

## Development Environment

- Language: Java
- Framework: LibGDX
- Build tool: (TBD - Gradle recommended)
- Physics/collision: TBD (library or simple custom solution)
- Scripting: TBD (lightweight custom scripting or embedded solution)

---

## Project Structure

```text
docs/        -> design documents (vision, architecture)
src/
  engine/    -> core engine code (ECS, systems, runtime)
  demo/      -> minimal demo used for validation
assets/      -> test assets
tests/       -> testing code
tools/       -> scripts and utilities
