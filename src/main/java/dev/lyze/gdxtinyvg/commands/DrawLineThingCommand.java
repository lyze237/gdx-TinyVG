package dev.lyze.gdxtinyvg.commands;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.commands.headers.OutlineHeader;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;
import dev.lyze.gdxtinyvg.enums.CommandType;
import dev.lyze.gdxtinyvg.enums.StyleType;
import dev.lyze.gdxtinyvg.types.UnitPoint;
import java.io.IOException;

public abstract class DrawLineThingCommand extends Command {
    private final boolean open;
    private OutlineHeader<UnitPoint> header;
    private float[] vertices;

    public DrawLineThingCommand(TinyVG tinyVG, CommandType type, boolean open) {
        super(type, tinyVG);
        this.open = open;
    }

    @Override
    public void read(LittleEndianInputStream stream, StyleType primaryStyleType) throws IOException {
        this.header = new OutlineHeader<>(UnitPoint.class).read(stream, primaryStyleType, getTinyVG());

        vertices = new float[header.getData().size * 2];
    }

    @Override
    public void draw(TinyVGShapeDrawer drawer, Viewport viewport) {
        header.getPrimaryStyle().start(drawer, viewport);
        drawer.path(header.getData(), vertices, header.getLineWidth(), open, getTinyVG());
        header.getPrimaryStyle().end(drawer, viewport);
    }
}
