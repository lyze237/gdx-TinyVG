package dev.lyze.gdxtinyvg.commands;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.commands.headers.OutlineFillHeader;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;
import dev.lyze.gdxtinyvg.enums.CommandType;
import dev.lyze.gdxtinyvg.enums.StyleType;
import dev.lyze.gdxtinyvg.types.UnitRectangle;
import java.io.IOException;
import lombok.var;

/**
 * Fills and outlines a list of rectangles.
 */
public class OutlineFillRectanglesCommand extends Command {
    private OutlineFillHeader<UnitRectangle> header;

    public OutlineFillRectanglesCommand(TinyVG tinyVG) {
        super(CommandType.OUTLINE_FILL_RECTANGLES, tinyVG);
    }

    @Override
    public void read(LittleEndianInputStream stream, StyleType primaryStyleType) throws IOException {
        header = new OutlineFillHeader<>(UnitRectangle.class).read(stream, primaryStyleType, getTinyVG());
    }

    @Override
    public void draw(TinyVGShapeDrawer drawer, Viewport viewport) {
        for (var rectangle : header.getData()) {
            drawer.setStyle(header.getPrimaryStyle(), viewport);
            drawer.filledRectangle(rectangle, getTinyVG());

            drawer.setStyle(header.getSecondaryStyle(), viewport);
            drawer.rectangle(rectangle, header.getLineWidth(), getTinyVG());
        }
    }
}
