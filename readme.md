# gdx-TinyVG

A libgdx parser and renderer for TinyVG https://www.tinyvg.tech/

[![Build, Test, Publish](https://github.com/lyze237/gdx-TinyVG/workflows/Test/badge.svg?branch=main)](https://github.com/lyze237/gdx-TinyVG/actions?query=workflow%3A%22Test%22)
[![License](https://img.shields.io/github/license/lyze237/gdx-TinyVG)](https://github.com/lyze237/gdx-TinyVG/blob/main/LICENSE)
[![Jitpack](https://jitpack.io/v/lyze237/gdx-TinyVG.svg)](https://jitpack.io/#lyze237/gdx-TinyVG)

# What's this about?

TODO

# Installation

1. Open or create `gradle.properties` in the root folder of your project, add the following line:

```properties
gdxTinyVGVersion=VERSION
```

Check [Jitpack](https://jitpack.io/#lyze237/gdx-TinyVG/) for the latest version and replace `VERSION` with that.

2. Add the jitpack repo to your build file.

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

3. Add that to your core modules dependencies inside your root `build.gradle`

```groovy
project(":core") {
    ...

    dependencies {
        ...
        implementation "com.github.lyze237:gdx-TinyVG:$gdxTinyVGVersion"
    }
}
```

## Html/Gwt project

1. Gradle dependency:

```groovy
implementation "com.github.lyze237:gdx-TinyVG:$gdxTinyVGVersion:sources"
```

2. In your application's `.gwt.xml` file add (Normally `GdxDefinition.gwt.xml`):

```xml

<inherits name="dev.lyze.tinyvg"/>
```

## How to test

By default, if you run `./gradlew test` gradle runs headless tests. If you want to test `lwjgl` tests (so with an actual
gui), then you need to run them with `./gradlew test -Plwjgl=true`

Set environment variable `SLEEPY` to a millisecond number to sleep between each LWJGL test. (For example: SLEEPY=3000 would wait 3 seconds after every test.)
