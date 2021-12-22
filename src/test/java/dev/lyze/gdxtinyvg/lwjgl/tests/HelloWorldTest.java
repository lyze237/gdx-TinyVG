package dev.lyze.gdxtinyvg.lwjgl.tests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import dev.lyze.gdxtinyvg.lwjgl.LibgdxLwjglUnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class HelloWorldTest extends LibgdxLwjglUnitTest {
    @Test
    @Tag("lwjgl")
    public void helloTest() {
        Assertions.assertEquals("Hello", "Hello");
    }

    @Test
    @Tag("lwjgl")
    public void worldTest() {
        Assertions.assertEquals("World", "World");
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);
    }
}
