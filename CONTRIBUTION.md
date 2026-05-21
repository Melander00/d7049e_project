# Game Engine
Open the `vie` folder as a new project in IntelliJ. It will automatically download libraries and setup the gradle environment.

The gradle action `:engine:lwjgl:run` is used to start the game. Do note that it requires the project files generated when exporting from the editor to run. 

Java 21 is used during development.

# Editor
Ensure you have NodeJS version 22 or higher. Navigate to `editor` folder. Run
```bash
npm install
```
to download dependencies and
```bash
npm run dev
```
to run the app.

Use File->Open (Ctrl+O) to open a folder as a project. Use an empty folder as a new project.
Use the provided `demo` folder as an initial project.

Use File->Export (Ctrl+E) to generate the necessary files for the game engine.
Make sure you have a CameraComponent enabled on any entity, otherwise it will crash on start.
To run the game, run the following command from inside the `vie` folder:
```bash
./gradlew :engine:lwjgl:run -PworkDir=/path/to/project/folder

# Windows CMD
gradlew.bat :engine:lwjgl:run -PworkDir=C:\path\to\project\folder
```