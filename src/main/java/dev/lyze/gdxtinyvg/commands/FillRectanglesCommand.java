package dev.lyze.gdxtinyvg.commands;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.GradientShapeDrawer;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.enums.CommandType;
import dev.lyze.gdxtinyvg.enums.StyleType;
import dev.lyze.gdxtinyvg.styles.Style;
import dev.lyze.gdxtinyvg.types.TinyVGIO;
import dev.lyze.gdxtinyvg.types.UnitRectangle;
import dev.lyze.gdxtinyvg.utils.StreamUtils;
import java.io.IOException;
import lombok.var;

/**
 * Fills a list of rectangles.
 */
public class FillRectanglesCommand extends Command {
    private Style fillStyle;
    private UnitRectangle[] rectangles;

    public FillRectanglesCommand(TinyVG tinyVG) {
        super(CommandType.OUTLINE_FILL_RECTANGLES, tinyVG);
    }

    @Override
    public void read(LittleEndianInputStream stream, StyleType primaryStyleType) throws IOException {
        var header = getTinyVG().getHeader();

        var rectangleCounts = StreamUtils.readVarUInt(stream) + 1;

        fillStyle = primaryStyleType.read(stream, getTinyVG());

        rectangles = new UnitRectangle[rectangleCounts];
        for (int i = 0; i < rectangles.length; i++)
            rectangles[i] = TinyVGIO.Rectangles.read(stream, header.getCoordinateRange(), header.getFractionBits());
    }

    @Override
    public void draw(GradientShapeDrawer drawer, Viewport viewport) {
        for (var rectangle : rectangles) {
            var position = getTinyVG().getPosition();
            var scale = getTinyVG().getScale();

            fillStyle.start(drawer, viewport);

            drawer.filledRectangle(rectangle.getX().convert() * scale.x + position.x * scale.x,
                    getTinyVG().getHeight() - rectangle.getHeight().convert() * scale.y
                            - rectangle.getY().convert() * scale.y + position.y * scale.y,
                    rectangle.getWidth().convert() * scale.x, rectangle.getHeight().convert() * scale.y);

            fillStyle.end(drawer, viewport);
        }
    }
}
