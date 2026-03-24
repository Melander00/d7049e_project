# d7049e_project

A Java-based virtual interactive environment (VIE) engine for fixed-room dungeon-style scenes with a focus on ECS architecture and performance.

## Project Idea (VIE Concept)

We are building a lightweight game engine for creating fixed-room dungeon environments inspired by games such as Binding of Isaac and Undertale.

The environment:
- uses a fixed-size room (canvas)
- supports layered object composition (floor, walls, entities, effects)
- represents objects in a 3D scene with pixel-art style visuals
- contains autonomous agents that interact with the player
- allows dynamic and configurable object counts

The game/demo itself is minimal and only used to validate engine functionality.

---

## Problem Statement

Pixel-style games often suffer from inconsistent asset scaling and mismatched visuals. This project explores a structured engine approach where room layout, object placement, and simulation are handled consistently within a unified system.

---

## Technical Focus

We aim to:
- design a clean ECS (Entity Component System)
- build a modular runtime architecture
- support autonomous agents
- handle configurable object counts
- measure and analyze performance-critical systems

---

## MVP Scope

This project is intentionally scoped as a minimal engine MVP.

### Included
- ECS-based engine core
- one fixed-room test scene
- simple 3D representation of room objects and entities
- basic rendering sufficient to visualize the scene
- one autonomous agent behavior
- configurable entity count for scalability testing
- basic collision integration
- simulation timing and profiling support

### Not Included
- full game content
- advanced editor features
- multiple levels or world streaming
- complex AI behaviors
- custom physics implementation
- polished UI or toolchain features

---

## Development Environment

- Language: Java
- Build tool: 
- Rendering library: 
- Physics/collision:

---

## Project Structure

```text
docs/        -> design documents (vision, architecture)
src/         -> engine and runtime code
assets/      -> test assets
tests/       -> testing code
tools/       -> scripts and utilities
