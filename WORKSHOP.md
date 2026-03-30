Project name: TDB
Team members:
* Samuel Melander: ROLLER
* Irma Palo Hjärtström: ROLLER
Repository link: https://github.com/Melander00/d7049e_project

1. Engine vision
We are building an engine for mobile-slop games. The type of games that people crank out fast with loads of ads. They are interesting for a couple of minutes.

2. Target genre/VIE
The engine is optimized for mobile 3d army controller shooter.

3. Core scenario
The first playable/technical scenario is...

You control a single soldier that have to shoot enemies to earn cash so he can expand his army and kill more enemies.

4. Reusable engine features
- Movement (left to right)
- Physics (collisions, bullet travel, gravity)
- Attack (the soldiers attack the same way, albeit with different force and frequency)
- Powerup system
- Item system
- Upgrade system
- Audio
- Level loading
- Event system

5. Game-specific features
- Enemy models and stats
- Soldier models and stats
- Items
- Powerups
- Upgrades
- Level design

6. Non-goals for milestone 1
- 3D model editor
- 
- 

7. Data-driven elements
- same as 5.?
- 
- 

8. Chosen libraries and tools
3D: LibGDX
Physics: Bullet Phyics
Audio: LibGDX
Build: LibGDX (gradle)
Other: Ashley (ESC), 

9. Engineering rules
License: MIT
Code style: TBD
Folder structure: TBD
Branch / commit convention:
Once structure has been defined and template code setup: Branch into `feat/` or `fix/` -> Create pull request -> Wait for PR review -> Merge into main -> Delete branch. Commit often, small commits -> easier reviews. Create small pull requests. No 100K changes reviews.