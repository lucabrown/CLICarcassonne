# Artificial Intelligence for Multiplayer Carcassonne
The following instruction are for `Unix shell`.

This project will allow you to simulate multiple Carcassonne games.

To set-up a game, go to the `Game` class and add the agents to the `players` `ArrayList` ( default is two `RandomAgent`s). 
In the `ThreadManager` class you can specify the number of threads and games you want to run (default is `1` for both).

Then you can build and run the project.

## Prerequisites

Before you can build and run this project, you need:

1. Java (JDK) 16: Download and install from [Oracle JDK](https://www.oracle.com/java/technologies/javase-jdk14-downloads.html)
2. Maven: Download and install from [Maven's official website](https://maven.apache.org/download.cgi)

This project also depends on the following libraries:

1. jgrapht: A graph library for Java
2. javatuples: A library for working with tuples in Java

The dependencies will be automatically managed by Maven when you build the project.

## Building the Project

To build the project, follow these steps:

1. Open a terminal at the project's root directory (where the `pom.xml` file is located).
2. Run the following command. This will generate `carcassonne-1.0-SNAPSHOT.jar` in the target directory.

```sh
mvn clean package
```


3. Navigate to the target directory:
```sh
cd target
``` 
4. Run
```sh
java -jar carcassonne-1.0-SNAPSHOT
```
If you want to change the game settings (i.e. test different agents or run more games), the project needs to be rebuilt from step `1`.
