# Remy

Remy is a JavaFX chatbot that helps you create, track, search, and complete tasks.

## Running Remy

Remy requires JDK 25. From the project root, run:

```sh
./gradlew run
```

On Windows, use `gradlew.bat run` instead. Enter a command in the field at the bottom of the window, then press
<kbd>Enter</kbd> or select **Send**.

### Commands

| Purpose | Command | Example |
| --- | --- | --- |
| Add a to-do | `todo DESCRIPTION` | `todo buy ingredients` |
| Add a deadline | `deadline DESCRIPTION /by DATE` | `deadline submit report /by 23/9/2026 1800` |
| Add an event | `event DESCRIPTION /from DATE /to DATE` | `event tutorial /from 24/9/2026 1400 /to 24/9/2026 1500` |
| Show all tasks | `list` | `list` |
| Find tasks | `find KEYWORD` | `find report` |
| Mark a task done | `mark NUMBER` | `mark 1` |
| Mark a task undone | `unmark NUMBER` | `unmark 1` |
| Delete a task | `delete NUMBER` | `delete 1` |
| End the chat | `bye` | `bye` |

Dates can use formats such as `23/9/2026`, `2026-09-23`, or `23 Sep 2026`. Add a 24-hour time such as `1800` or
`18:00` when needed.

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate `src/main/java/remy/gui/Launcher.java`, right-click it, and choose `Run Launcher.main()`.

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
