# Duke project template

This is a project template for a greenfield Java project. It's named after the Java mascot _Duke_. Given below are instructions on how to use it.

## Command aliases

Bob accepts the following lowercase aliases wherever a command keyword is used.
Full command names still work, and aliases take the same arguments.

| Command | Alias | Example |
| --- | --- | --- |
| `todo` | `t` | `t read book` |
| `deadline` | `d` | `d return book /by 2/12/2019` |
| `event` | `e` | `e meeting /from 2/12/2019 1400 /to 2/12/2019 1600` |
| `list` | `l` | `l` |
| `find` | `f` | `f book` |
| `mark` | `m` | `m 1` |
| `unmark` | `u` | `u 1` |
| `delete` | `del` | `del 1` |
| `bye` | `b` | `b` |

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate the `src/main/java/Duke.java` file, right-click it, and choose `Run Duke.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, you should see something like the below as the output:
   ```
    ____        _        
   |  _ \ _   _| | _____ 
   | | | | | | | |/ / _ \
   | |_| | |_| |   <  __/
   |____/ \__,_|_|\_\___|
   ```

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.
