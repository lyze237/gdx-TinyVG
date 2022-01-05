package dev.lyze.gdxtinyvg.commands;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.commands.headers.FillHeader;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;
import dev.lyze.gdxtinyvg.enums.CommandType;
import dev.lyze.gdxtinyvg.enums.StyleType;
import dev.lyze.gdxtinyvg.types.UnitPoint;
import java.io.IOException;

/**
 * Fills a polygon with N points
 */
public class FillPolygonCommand extends Command {
    private FillHeader<UnitPoint> header;
    private float[] vertices;

    public FillPolygonCommand(TinyVG tinyVG) {
        super(CommandType.FILL_POLYGON, tinyVG);
    }

    @Override
    public void read(LittleEndianInputStream stream, StyleType primaryStyleType) throws IOException {
        header = new FillHeader<>(UnitPoint.class).read(stream, primaryStyleType, getTinyVG());

        vertices = new float[header.getData().size * 2];
    }

    @Override
    public void draw(TinyVGShapeDrawer drawer, Viewport viewport) {
        header.getPrimaryStyle().start(drawer, viewport);
        drawer.filledPolygon(header.getData(), vertices, getTinyVG());
        header.getPrimaryStyle().end(drawer, viewport);
    }
}
