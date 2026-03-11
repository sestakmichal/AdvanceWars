#!/bin/bash
JUNIT="junit-platform-console-standalone-1.11.4.jar"

rm -rf build
mkdir build

# Kompilace s vyuzitim JUnit:
javac -cp ".;$JUNIT" -d build src/ija/ija2025/homework2/Homework2Test.java

# Spusteni testu s vyuzitim JUnit:
java -ea -jar ./${JUNIT} execute --cp build --scan-class-path