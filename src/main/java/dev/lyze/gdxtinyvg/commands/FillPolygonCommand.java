package dev.lyze.gdxtinyvg.commands;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.commands.headers.FillHeader;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;
import dev.lyze.gdxtinyvg.drawers.chaches.TinyVGDrawerCache;
import dev.lyze.gdxtinyvg.enums.CommandType;
import dev.lyze.gdxtinyvg.enums.StyleType;
import dev.lyze.gdxtinyvg.types.UnitPoint;
import java.io.IOException;

/**
 * Fills a polygon with N points
 */
public class FillPolygonCommand extends Command {
    private final TinyVGDrawerCache cache;
    private FillHeader<UnitPoint> header;

    public FillPolygonCommand(TinyVG tinyVG) {
        super(CommandType.FILL_POLYGON, tinyVG);

        cache = new TinyVGDrawerCache(getTinyVG());
    }

    @Override
    public void read(LittleEndianInputStream stream, StyleType primaryStyleType) throws IOException {
        header = new FillHeader<>(UnitPoint.class).read(stream, primaryStyleType, getTinyVG());

        onPropertiesChanged();
    }

    @Override
    public void draw(TinyVGShapeDrawer drawer, Viewport viewport) {
        drawer.setStyle(header.getPrimaryStyle(), viewport);
        cache.filledPolygon(drawer);
    }

    @Override
    public void onPropertiesChanged() {
        cache.calculateVertices(header.getData());
        cache.calculateTriangles();
    }
}
