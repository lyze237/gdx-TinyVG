package dev.lyze.gdxtinyvg.commands;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.commands.headers.OutlinePathHeader;
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
        header = new OutlinePathHeader(getTinyVG());
        header.read(stream, primaryStyleType);
    }

    @Override
    public void draw(TinyVGShapeDrawer drawer, Viewport viewport) {
        drawer.setStyle(header.getPrimaryStyle(), viewport);

        for (var segment : header.getSegments())
            segment.getCache().path(drawer, segment.getPoints().get(0).getWidth(),
                    !(segment.getLastCommand() instanceof UnitPathCloseCommand));
    }

    @Override
    public void onPropertiesChanged() {
        header.recalculateSegments();
    }
}
