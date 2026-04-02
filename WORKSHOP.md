# Project name: brain-slop game engine

Team members:
* Samuel Melander: chief observer 🗿
* Irma Palo Hjärtström: code monkey 🐒

Repository link: https://github.com/Melander00/d7049e_project

1. Engine vision ✅

This engine is meant for building quick, replayable mobile games. Players continuously acquire upgrades, make quick decisions between competing options, and watch their power scale in real time. The system emphasizes clarity and spectacle, ensuring every choice visibly impacts combat and leads to either a decisive win or an obvious failure. Short gameplay loops and escalating difficulty make each run feel intense while encouraging immediate retries. By modularizing upgrades, enemies, and progression, the engine enables rapid production of many different games.

- Target: Mobile

- Age: Children aged 8-14

- Tags: Singleplayer, Offline, Fast-paced, Infinite rounds, P2W, ADS, MicroTX, Customization

2. Target genre/VIE ✅

The engine is optimized for mobile 3d army controller shooter.

3. Core scenario ✅

You control a single soldier that have to shoot enemies and barrels so he can expand his army and kill more enemies.
<img width="960" height="540" alt="D7049E STORYBOARD" src="https://github.com/user-attachments/assets/5c777c48-8397-4c73-82d4-277699bb5009" />



4. Reusable engine features ✅
- Movement (left to right)
- Physics (collisions, bullet travel, gravity)
- Attack (the soldiers attack the same way, albeit with different force and frequency)
- Camera
- Audio
- Level loading
- Event system
- import 3d models

5. Game-specific features ✅
- Enemy models and stats
- Soldier models and stats
- Items
- Powerups
- Upgrades
- Level design

6. Non-goals for milestone 1 ✅
- 3D model editor/import
- Complete gameplay loop 
- Enemy AI beyond placeholder behavior
- Army system (only a single controllable entity is enough)


7. Data-driven elements (milestone 1?) ✅

- Basic configuration loading
- Entity prototype definition
- Scene bootstrap data

8. Chosen libraries and tools ✅
- 3D: LibGDX
- Physics: Bullet Phyics
- Audio: LibGDX
- Build: LibGDX (gradle)
- Other: Ashley (ESC), 

9. Engineering rules ✅

License: MIT
Code style: see document
Folder structure: TBD
Branch / commit convention:
Once structure has been defined and template code setup: Branch into `feat/` or `fix/` -> Create pull request -> Wait for PR review -> Merge into main -> Delete branch. Commit often, small commits -> easier reviews. Create small pull requests. No 100K changes reviews.
