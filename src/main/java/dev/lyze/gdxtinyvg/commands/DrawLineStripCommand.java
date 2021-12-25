package dev.lyze.gdxtinyvg.commands;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.GradientShapeDrawer;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.enums.CommandType;
import dev.lyze.gdxtinyvg.enums.StyleType;
import dev.lyze.gdxtinyvg.styles.Style;
import dev.lyze.gdxtinyvg.types.TinyVGIO;
import dev.lyze.gdxtinyvg.types.UnitPoint;
import dev.lyze.gdxtinyvg.utils.StreamUtils;
import java.io.IOException;
import lombok.var;

/**
 * Draws a set of lines.
 */
public class DrawLineStripCommand extends Command {
    private Style lineStyle;
    private float lineWidth;
    private UnitPoint[] points;

    public DrawLineStripCommand(TinyVG tinyVG) {
        super(CommandType.DRAW_LINE_LOOP, tinyVG);
    }

    @Override
    public void read(LittleEndianInputStream stream, StyleType primaryStyleType) throws IOException {
        var header = getTinyVG().getHeader();

        var pointCount = StreamUtils.readVarUInt(stream) + 1;

        lineStyle = primaryStyleType.read(stream, getTinyVG());

        lineWidth = TinyVGIO.Units.read(stream, header.getCoordinateRange(), header.getFractionBits()).convert();

        points = new UnitPoint[pointCount];
        for (int i = 0; i < points.length; i++)
            points[i] = TinyVGIO.Points.read(stream, header.getCoordinateRange(), header.getFractionBits());
    }

    @Override
    public void draw(GradientShapeDrawer drawer, Viewport viewport) {
        var position = getTinyVG().getPosition();
        var scale = getTinyVG().getScale();

        lineStyle.start(drawer, viewport);

        float[] vertices = new float[points.length * 2];
        for (int p = 0, v = 0; p < points.length; p++, v += 2) {
            vertices[v] = points[p].getX().convert() * scale.x + position.x;
            vertices[v + 1] = getTinyVG().getHeight() - points[p].getY().convert() * scale.y + position.y;
        }

        drawer.path(vertices, lineWidth * getTinyVG().getLineWidthScale(), true);

        lineStyle.end(drawer, viewport);
    }
}
