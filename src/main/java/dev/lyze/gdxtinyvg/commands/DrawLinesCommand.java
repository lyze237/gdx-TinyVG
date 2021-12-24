package dev.lyze.gdxtinyvg.commands;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.GradientShapeDrawer;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.enums.CommandType;
import dev.lyze.gdxtinyvg.enums.StyleType;
import dev.lyze.gdxtinyvg.styles.Style;
import dev.lyze.gdxtinyvg.types.TinyVGIO;
import dev.lyze.gdxtinyvg.types.UnitLine;
import dev.lyze.gdxtinyvg.utils.StreamUtils;
import java.io.IOException;
import lombok.var;

public class DrawLinesCommand extends Command {
    private Style lineStyle;
    private float lineWidth;
    private UnitLine[] lines;

    public DrawLinesCommand(TinyVG tinyVG) {
        super(CommandType.DRAW_LINES, tinyVG);
    }

    @Override
    public void read(LittleEndianInputStream stream, StyleType primaryStyleType) throws IOException {
        var header = getTinyVG().getHeader();

        var lineCount = StreamUtils.readVarUInt(stream) + 1;

        lineStyle = primaryStyleType.read(stream, getTinyVG());

        lineWidth = TinyVGIO.Units.read(stream, header.getCoordinateRange(), header.getScale()).convert();

        lines = new UnitLine[lineCount];
        for (int i = 0; i < lines.length; i++)
            lines[i] = TinyVGIO.Lines.read(stream, header.getCoordinateRange(), header.getScale());
    }

    @Override
    public void draw(GradientShapeDrawer drawer, Viewport viewport) {
        for (var line : lines) {
            var position = getTinyVG().getPosition();
            var scale = getTinyVG().getScale();

            var header = getTinyVG().getHeader();
            var start = line.getStart();
            var end = line.getEnd();

            lineStyle.start(drawer, viewport);

            drawer.line(start.getX().convert() + position.x, header.getHeight() - start.getY().convert() + position.y,
                    end.getX().convert() + position.x, header.getHeight() - end.getY().convert() + position.y, lineWidth);

            lineStyle.end(drawer, viewport);
        }
    }
}
