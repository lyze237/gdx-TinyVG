package dev.lyze.gdxtinyvg.commands;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.commands.headers.OutlineHeader;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;
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
    public void draw(TinyVGShapeDrawer drawer) {
        drawer.setStyle(header.getPrimaryStyle());
        for (var line : header.getData())
            drawer.line(line, header.getLineWidth(), getTinyVG());
    }
}
