package dev.lyze.gdxtinyvg.commands;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.GradientShapeDrawer;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.commands.headers.OutlineHeader;
import dev.lyze.gdxtinyvg.enums.CommandType;
import dev.lyze.gdxtinyvg.enums.StyleType;
import dev.lyze.gdxtinyvg.types.UnitPoint;
import java.io.IOException;
import lombok.var;

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
    public void draw(GradientShapeDrawer drawer, Viewport viewport) {
        var position = getTinyVG().getPosition();
        var scale = getTinyVG().getScale();

        header.getPrimaryStyle().start(drawer, viewport);

        for (int p = 0, v = 0; p < header.getData().size; p++, v += 2) {
            vertices[v] = header.getData().get(p).getX().convert() * scale.x + position.x;
            vertices[v + 1] = getTinyVG().getHeight() - header.getData().get(p).getY().convert() * scale.y + position.y;
        }

        drawer.path(vertices, header.getLineWidth() * getTinyVG().getLineWidthScale(), open);

        header.getPrimaryStyle().end(drawer, viewport);
    }
}
