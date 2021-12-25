package dev.lyze.gdxtinyvg.commands;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.GradientShapeDrawer;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.commands.headers.OutlineHeader;
import dev.lyze.gdxtinyvg.enums.CommandType;
import dev.lyze.gdxtinyvg.enums.StyleType;
import dev.lyze.gdxtinyvg.types.UnitLine;
import java.io.IOException;
import lombok.var;

/**
 * Draws a set of lines.
 */
public class DrawLinesCommand extends Command {
    private OutlineHeader<UnitLine> header;

    public DrawLinesCommand(TinyVG tinyVG) {
        super(CommandType.DRAW_LINES, tinyVG);
    }

    @Override
    public void read(LittleEndianInputStream stream, StyleType primaryStyleType) throws IOException {
        this.header = new OutlineHeader<>(UnitLine.class).read(stream, primaryStyleType, getTinyVG());
    }

    @Override
    public void draw(GradientShapeDrawer drawer, Viewport viewport) {
        for (var line : header.getData()) {
            var position = getTinyVG().getPosition();
            var scale = getTinyVG().getScale();

            var start = line.getStart();
            var end = line.getEnd();

            header.getPrimaryStyle().start(drawer, viewport);

            drawer.line(start.getX().convert() * scale.x + position.x,
                    getTinyVG().getHeight() - start.getY().convert() * scale.y + position.y,
                    end.getX().convert() * scale.x + position.x,
                    getTinyVG().getHeight() - end.getY().convert() * scale.y + position.y,
                    header.getLineWidth() * getTinyVG().getLineWidthScale());

            header.getPrimaryStyle().end(drawer, viewport);
        }
    }
}
