package dev.lyze.gdxtinyvg.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.LittleEndianInputStream;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.commands.headers.OutlineFillPathHeader;
import dev.lyze.gdxtinyvg.commands.paths.UnitPathCloseCommand;
import dev.lyze.gdxtinyvg.commands.paths.UnitPathSegment;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;
import dev.lyze.gdxtinyvg.drawers.chaches.TinyVGDrawerCache;
import dev.lyze.gdxtinyvg.enums.CommandType;
import dev.lyze.gdxtinyvg.enums.StyleType;
import dev.lyze.gdxtinyvg.styles.Style;
import dev.lyze.gdxtinyvg.types.ParsedPathSegment;
import dev.lyze.gdxtinyvg.types.Unit;
import dev.lyze.gdxtinyvg.types.Vector2WithWidth;
import dev.lyze.gdxtinyvg.utils.StreamUtils;
import java.io.IOException;
import java.util.Arrays;
import lombok.var;

public class OutlineFillPathCommand extends Command {
    private OutlineFillPathHeader header;

    public OutlineFillPathCommand(TinyVG tinyVG) {
        super(CommandType.FILL_PATH, tinyVG);
    }

    @Override
    public void read(LittleEndianInputStream stream, StyleType primaryStyleType) throws IOException {
        header = new OutlineFillPathHeader(getTinyVG());
        header.read(stream, primaryStyleType);
    }

    @Override
    public void draw(TinyVGShapeDrawer drawer, Viewport viewport) {
        header.getPrimaryStyle().start(drawer, viewport);

        Gdx.gl.glEnable(GL20.GL_STENCIL_TEST);
        Gdx.gl.glClear(GL20.GL_STENCIL_BUFFER_BIT);
        Gdx.gl.glColorMask(false, false, false, false);
        Gdx.gl.glStencilFunc(GL20.GL_ALWAYS, 0, -1);
        Gdx.gl.glStencilOp(GL20.GL_KEEP, GL20.GL_KEEP, GL20.GL_INCR);

        for (ParsedPathSegment segment : header.getSegments())
            segment.getCache().filledPolygon(drawer);

        drawer.getBatch().flush();

        Gdx.gl.glColorMask(true, true, true, true);
        Gdx.gl.glStencilFunc(GL20.GL_EQUAL, 1, -1); // was GL_NOTEQUAL, 2, -1
        Gdx.gl.glStencilOp(GL20.GL_KEEP, GL20.GL_KEEP, GL20.GL_KEEP);

        for (ParsedPathSegment segment : header.getSegments())
            segment.getCache().filledPolygon(drawer);

        header.getPrimaryStyle().end(drawer, viewport);
        Gdx.gl.glDisable(GL20.GL_STENCIL_TEST);

        header.getSecondaryStyle().start(drawer, viewport);
        for (var segment : header.getSegments())
            segment.getCache().path(drawer, segment.getPoints().get(0).getWidth(),
                    !(segment.getLastCommand() instanceof UnitPathCloseCommand));
        header.getSecondaryStyle().end(drawer, viewport);
    }

    @Override
    public void onPropertiesChanged() {
        header.recalculateSegments();
    }
}
