package dev.lyze.gdxtinyvg.commands;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.GradientShapeDrawer;
import dev.lyze.gdxtinyvg.StreamUtils;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.enums.CommandType;
import dev.lyze.gdxtinyvg.enums.StyleType;
import dev.lyze.gdxtinyvg.shapes.Line;
import dev.lyze.gdxtinyvg.shapes.Unit;
import dev.lyze.gdxtinyvg.shapes.UnitLine;
import dev.lyze.gdxtinyvg.styles.Style;
import java.io.IOException;
import lombok.var;

public class DrawLinesCommand extends Command {
    private Style lineStyle;
    private float lineWidth;
    private Line[] lines;

    public DrawLinesCommand(TinyVG tinyVG) {
        super(CommandType.DRAW_LINES, tinyVG);
    }

    @Override
    public void read(LittleEndianInputStream stream, StyleType primaryStyleType) throws IOException {
        var header = getTinyVG().getHeader();

        var lineCount = StreamUtils.readVarUInt(stream) + 1;

        lineStyle = primaryStyleType.read(stream, getTinyVG());

        var lineWidthUnit = new Unit(header.getCoordinateRange(), header.getScale());
        lineWidthUnit.read(stream);
        lineWidth = lineWidthUnit.getFloatValue();

        lines = new Line[lineCount];
        for (int i = 0; i < lines.length; i++) {
            var line = new UnitLine();
            line.read(stream, header.getCoordinateRange(), header.getScale());

            lines[i] = line.convertLine(header);
        }
    }

    @Override
    public void draw(GradientShapeDrawer drawer, Viewport viewport) {
        for (Line line : lines) {
            lineStyle.start(drawer, viewport);
            drawer.line(line.getStart(), line.getEnd(), lineWidth);
            lineStyle.end(drawer, viewport);
        }
    }
}
