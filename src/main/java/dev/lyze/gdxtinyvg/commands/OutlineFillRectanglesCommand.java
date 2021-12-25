package dev.lyze.gdxtinyvg.commands;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.GradientShapeDrawer;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.commands.headers.OutlineFillHeader;
import dev.lyze.gdxtinyvg.enums.CommandType;
import dev.lyze.gdxtinyvg.enums.StyleType;
import dev.lyze.gdxtinyvg.types.UnitRectangle;
import java.io.IOException;
import lombok.var;

/**
 * Fills and outlines a list of rectangles.
 */
public class OutlineFillRectanglesCommand extends Command {
    private OutlineFillHeader<UnitRectangle> header;

    public OutlineFillRectanglesCommand(TinyVG tinyVG) {
        super(CommandType.OUTLINE_FILL_RECTANGLES, tinyVG);
    }

    @Override
    public void read(LittleEndianInputStream stream, StyleType primaryStyleType) throws IOException {
        header = new OutlineFillHeader<>(UnitRectangle.class).read(stream, primaryStyleType, getTinyVG());
    }

    @Override
    public void draw(GradientShapeDrawer drawer, Viewport viewport) {
        for (var rectangle : header.getData()) {
            var position = getTinyVG().getPosition();
            var scale = getTinyVG().getScale();

            header.getPrimaryStyle().start(drawer, viewport);
            drawer.filledRectangle(rectangle.getX().convert() * scale.x + position.x,
                    getTinyVG().getHeight() - rectangle.getHeight().convert() * scale.y
                            - rectangle.getY().convert() * scale.y + position.y,
                    rectangle.getWidth().convert() * scale.x, rectangle.getHeight().convert() * scale.y);
            header.getPrimaryStyle().end(drawer, viewport);

            header.getSecondaryStyle().start(drawer, viewport);
            drawer.rectangle(rectangle.getX().convert() * scale.x + position.x,
                    getTinyVG().getHeight() - rectangle.getHeight().convert() * scale.y
                            - rectangle.getY().convert() * scale.y + position.y,
                    rectangle.getWidth().convert() * scale.x, rectangle.getHeight().convert() * scale.y,
                    header.getLineWidth() * getTinyVG().getLineWidthScale());
            header.getSecondaryStyle().end(drawer, viewport);
        }
    }
}
