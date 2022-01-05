# gdx-TinyVG

A libgdx parser and renderer for TinyVG https://www.tinyvg.tech/

[![Build, Test, Publish](https://github.com/lyze237/gdx-TinyVG/workflows/Test/badge.svg?branch=main)](https://github.com/lyze237/gdx-TinyVG/actions?query=workflow%3A%22Test%22)
[![License](https://img.shields.io/github/license/lyze237/gdx-TinyVG)](https://github.com/lyze237/gdx-TinyVG/blob/main/LICENSE)
[![Jitpack](https://jitpack.io/v/lyze237/gdx-TinyVG.svg)](https://jitpack.io/#lyze237/gdx-TinyVG)

# What is TinyVG?

SVG is a horribly complex format and an overkill for most projects. The specification includes way too many edge cases so implementing a new SVG renderer will always have drawbacks or missing pieces.

TinyVG tries to be simpler. Fewer features, but powerful enough to cover 90% of use cases. ~ https://tinyvg.tech

# Convert SVG into TVG

There's a converter on the [homepage ("TinyVG SDK")](https://tinyvg.tech/), but it sadly doesn't work with advanced features like gradients.

Luckily the file format is relatively simple, and after you convert a svg you get a text representation of the TinyVG file format.

Therefore, you are able to manually edit the file and add gradients that way if you want that.

# Example

1. Download the sdk from https://tinyvg.tech
2. Convert your SVG into a TVG
```shell
./svg2tvg file.svg -o file.tvgt # converts the file into an editable text tvg
./tvg-text file.tvgt -o file.tvg # converts the text tvg into a binary tvg
```
3. Set it up in java
```java
public class Lwjgl3Launcher {
  public static void main(String[] args) {
    createApplication();
  }

  private static Lwjgl3Application createApplication() {
    return new Lwjgl3Application(new Main(), getDefaultConfiguration());
  }

  private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
    Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
    // By default, stencils are disabled, we need those though. So let's enable them here (It's the 6th value. Change that to >= 2).
    configuration.setBackBufferConfig(8, 8, 8, 8, 16, 2, 0);
    // ...

    // In a legacy desktop project it's:
    config.stencils = 2; // >= 2
    return configuration;
  }
}

public class Example extends ApplicationAdapter {
  private TinyVG tvg;
  private TinyVGShapeDrawer drawer;
  private Viewport viewport = new XXXViewport(xxx, xxx);

  public void create() {
    var assetLoader = new TinyVGAssetLoader();
    tvg = assetLoader.load("file.tvg"); // only works with the binary file format

    // TinyVGShapeDrawer requires a 1x1 white pixel file,
    // so create one in your favourite program or
    // download one from the test examples in the repo.
    // https://github.com/lyze237/gdx-TinyVG/blob/main/src/test/resources/pixel.png
    drawer = new TinyVGShapeDrawer(new SpriteBatch(), new TextureRegion(new Texture("pixel.png")));
  }

  public void render() {
    Gdx.gl.glClearColor(0.25f, 0.25f, 0.25f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT); // GL20.GL_STENCIL_BUFFER_BIT is very important

    viewport.apply();

    drawer.getBatch().setProjectionMatrix(viewport.getCamera().combined);

    drawer.getBatch().begin();
    tvg.draw(drawer, viewport);
    drawer.getBatch().end();
  }

  @Override
  public void resize(int width, int height) {
    viewport.update(width, height, true);
  }
}
```

Alternatively you can load it via an AssetManager
```java
public class Example extends ApplicationAdapter {
  private TinyVG tvg;
  private TinyVGShapeDrawer drawer;
  private Viewport viewport = new XXXViewport(xxx, xxx);

  public void create() {
    var assetManager = new AssetManager();
    assetManager.setLoader(TinyVG.class, new TinyVGAssetLoader());

    assetManager.load("square.tvg", TinyVG.class);

    assetManager.finishLoading();

    var tvg = assetManager.get("square.tvg", TinyVG.class);
  }
}
```



# Specification implementation status

https://tinyvg.tech/download/specification.pdf

- [x] Header
- [x] Color table
- [x] Scaling
- [x] Positioning
- Types
  - [x] Unit
  - [x] VarUInt
  - [x] Point
  - [x] Rectangle
  - [x] Path
  - [x] Line
  - [x] Horizontal Line
  - [x] Vertical Line
  - [x] Cubic Bezier
  - [x] Arc Circle
  - [x] Arc Ellipse
  - [x] Close Path
  - [x] Quadratic Bezier
- Commands
  - [x] End of Document
  - [x] Fill Polygon
  - [x] Fill Rectangles
  - [x] Fill Path
  - [x] Draw Lines
  - [x] Draw Line Loop
  - [x] Draw Line Strip
  - [x] [Partially] Draw Line Path (Help wanted for variable line width)
  - [x] Outline Fill Polygon
  - [x] Outline Fill Rectangles
  - [x] [Partially] Outline Fill Path (Help wanted for variable line width)
- Styles
  - [x] Flat
  - [x] Linear Gradient
  - [x] Radial Gradient

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
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```

3. Add that to your core modules dependencies inside your root `build.gradle`

```groovy
project(":core") {
    // ...

    dependencies {
        // ...
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

## Help

* Fill paths are displayed garbled
  * Make sure to set `| GL20.GL_STENCIL_BUFFER_BIT` in your clear method. See [example](#example).
  * Make sure to have `config.stencil` enabled and set to >= 2 in your launcher configuration.

## How to test

By default, if you run `./gradlew test` gradle runs headless tests. If you want to test `lwjgl` tests (so with an actual
gui), then you need to run them with `./gradlew test -Plwjgl=true`

Set environment variable `SLEEPY` to a millisecond number to sleep between each LWJGL test. (For example: SLEEPY=3000 would wait 3 seconds after every test.)
