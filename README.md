[![Build Status](https://travis-ci.org/yeputons/roguelike-fall-2016.svg?branch=master)](https://travis-ci.org/yeputons/roguelike-fall-2016)

# roguelike-fall-2016
Educational project from Fall 2016.


# Building with Maven

First, you should install <a href="https://maven.apache.org/">Maven</a>.
Note that this project is written in Kotlin, so Maven will have to download a lot other dependencies.
Available Maven targets:

* `mvn build` - build the project.
* `mvn test` - build and run all tests.
* `mvn exec:java` - start app right away after building. You should build the project first.
* `mvn package` - build, run all tests, create JAR files in `target/` subdirectory (one without dependencies, one with full set of dependencies).
You can run the latter with `java -jar target/roguelike-1.0-SNAPSHOT-jar-with-dependencies.jar`.
* `mvn clean` - remove all generated files.

# Building with IntelliJ IDEA

Alternatively you should be able to seamlessly import Maven project into IDEA and run all classes/tests out of the box.

# Gameplay

## Control
* `s` - print current status (items and stats)
* `d` - move all items weared to inventory
* `0` - `9` - pick item by number from inventory to weared
* Arrows - move
* Space - stay

## Chars
* `#` - occupied cell.
* `$` - locked cell, you should find a key, wear it, enter the cell, and it will become permanently unblocked.
* `k` - key from some locked cell
* `G` - goal cell
* `o` - stats changer (extra attack, extra defense)
* `z` - zombie, your enemy, goes along a cycle

## Attacks
If you are going coincide with a zombie in a cell, both of you don't move and attack each other (zombie goes first).
Health is decrease by attach stats of the attacker minus defense stats of the attacked.
