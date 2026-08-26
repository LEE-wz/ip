# Remy

Remy is a command-line task manager.

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate `src/main/java/remy/Remy.java`, right-click it, and choose `Run Remy.main()`.

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.

## Building and running a fat JAR

The Shadow plugin packages Remy's compiled classes and runtime dependencies into one executable fat JAR.

1. From the project root, create the JAR:

   ```sh
   ./gradlew shadowJar
   ```

   On Windows, use `gradlew.bat shadowJar` instead. To force a completely fresh build, run `./gradlew clean shadowJar`.

1. Locate the resulting JAR at `build/libs/remy.jar`.

1. Run it with Java 25:

   ```sh
   java -jar build/libs/remy.jar
   ```

`./gradlew build` also creates this JAR while running the project's checks.
