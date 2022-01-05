package dev.lyze.gdxtinyvg.commands;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.commands.headers.PathHeader;
import dev.lyze.gdxtinyvg.commands.paths.UnitPathCloseCommand;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;
import dev.lyze.gdxtinyvg.enums.CommandType;
import dev.lyze.gdxtinyvg.enums.StyleType;
import java.io.IOException;
import lombok.var;

public class DrawLinePathCommand extends Command {
    private PathHeader header;

    public DrawLinePathCommand(TinyVG tinyVG) {
        super(CommandType.FILL_PATH, tinyVG);
    }

    @Override
    public void read(LittleEndianInputStream stream, StyleType primaryStyleType) throws IOException {
        header = new PathHeader(getTinyVG());
        header.read(stream, primaryStyleType, true);
    }

    @Override
    public void draw(TinyVGShapeDrawer drawer, Viewport viewport) {
        header.getPrimaryStyle().start(drawer, viewport);

        for (var segment : header.getSegments()) {
            drawer.path(segment, !(segment.getLastCommand() instanceof UnitPathCloseCommand), getTinyVG());
        }

        header.getPrimaryStyle().end(drawer, viewport);
    }
}
