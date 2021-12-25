package dev.lyze.gdxtinyvg.commands;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.GradientShapeDrawer;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.commands.headers.FillHeader;
import dev.lyze.gdxtinyvg.enums.CommandType;
import dev.lyze.gdxtinyvg.enums.StyleType;
import dev.lyze.gdxtinyvg.types.UnitPoint;
import java.io.IOException;
import lombok.var;

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
    public void draw(GradientShapeDrawer drawer, Viewport viewport) {
        var position = getTinyVG().getPosition();
        var scale = getTinyVG().getScale();

        header.getPrimaryStyle().start(drawer, viewport);

        for (int p = 0, v = 0; p < header.getData().size; p++, v += 2) {
            vertices[v] = header.getData().get(p).getX().convert() * scale.x + position.x;
            vertices[v + 1] = getTinyVG().getHeight() - header.getData().get(p).getY().convert() * scale.y + position.y;
        }

        drawer.filledPolygon(vertices);

        header.getPrimaryStyle().end(drawer, viewport);
    }
}