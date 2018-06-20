# Pando

Truly decentralized cryptocurrency.  Written in Kotlin.

Here's an [Abstract about Pando](doc/abstract.md).





# Dev Environment Tips for IDEA

- Make sure you have version 10 of the Java JDK installed and point IDEA to they proper JDK version
  * http://www.oracle.com/technetwork/java/javase/downloads/jdk10-downloads-4416644.html
- Import Project the first time you open Pando (depending on your version of Gradle either the wrapper or the local distribution will work)
- Make sure your project SDK is pointing to your local Java SDK 
- Install the Spek plugin so you can run the tests from IDEA
- If you cannot see the Kotlin folders inside of the main and test directories right click on the Project tab in the upper left hand corner and uncheck "Hide Empty Middle Packages"