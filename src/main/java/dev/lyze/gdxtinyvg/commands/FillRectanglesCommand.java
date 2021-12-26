package dev.lyze.gdxtinyvg.commands;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.commands.headers.FillHeader;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;
import dev.lyze.gdxtinyvg.enums.CommandType;
import dev.lyze.gdxtinyvg.enums.StyleType;
import dev.lyze.gdxtinyvg.types.UnitRectangle;
import java.io.IOException;
import lombok.var;

/**
 * Fills a list of rectangles.
 */
public class FillRectanglesCommand extends Command {
    private FillHeader<UnitRectangle> header;

    public FillRectanglesCommand(TinyVG tinyVG) {
        super(CommandType.OUTLINE_FILL_RECTANGLES, tinyVG);
    }

    @Override
    public void read(LittleEndianInputStream stream, StyleType primaryStyleType) throws IOException {
        header = new FillHeader<>(UnitRectangle.class).read(stream, primaryStyleType, getTinyVG());
    }

    @Override
    public void draw(TinyVGShapeDrawer drawer, Viewport viewport) {
        for (var rectangle : header.getData()) {
            header.getPrimaryStyle().start(drawer, viewport);
            drawer.filledRectangle(rectangle, getTinyVG());
            header.getPrimaryStyle().end(drawer, viewport);
        }
    }
}
