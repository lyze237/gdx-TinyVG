package dev.lyze.gdxtinyvg.headless.tests;

import com.badlogic.gdx.graphics.Color;
import dev.lyze.gdxtinyvg.TinyVGAssetLoader;
import dev.lyze.gdxtinyvg.enums.ColorEncoding;
import dev.lyze.gdxtinyvg.enums.CommandType;
import dev.lyze.gdxtinyvg.headless.LibgdxHeadlessUnitTest;
import lombok.var;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HeaderTest extends LibgdxHeadlessUnitTest {
    @Test
    public void SquareHeaderTest() {
        var tvg = new TinyVGAssetLoader().load("twoSquaresWithGradients.tvg");

        Assertions.assertEquals(ColorEncoding.RGBA8888, tvg.getHeader().getColorEncoding());

        Assertions.assertEquals(200, tvg.getHeader().getHeight());
        Assertions.assertEquals(200, tvg.getHeader().getWidth());

        Assertions.assertEquals(2, tvg.getHeader().getColorTableCount());
        Assertions.assertEquals(2, tvg.getColorTable().length);
        Assertions.assertEquals(new Color(0, 0, 1, 1), tvg.getColorTable()[0]);
        Assertions.assertEquals(new Color(1, 0, 0, 1), tvg.getColorTable()[1]);

        Assertions.assertEquals(CommandType.OUTLINE_FILL_RECTANGLES, tvg.getCommands().get(0).getType());
        Assertions.assertEquals(CommandType.END_OF_DOCUMENT, tvg.getCommands().get(1).getType());
    }
}
