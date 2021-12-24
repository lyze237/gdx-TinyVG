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
            rectangles[i] = TinyVGIO.Rectangles.read(stream, header.getCoordinateRange(), header.getScale());
    }

    @Override
    public void draw(GradientShapeDrawer drawer, Viewport viewport) {
        for (var rectangle : rectangles) {
            fillStyle.start(drawer, viewport);

            var header = getTinyVG().getHeader();
            var position = getTinyVG().getPosition();
            var scale = getTinyVG().getScale();

            drawer.filledRectangle(rectangle.getX().convert() + position.x,
                    header.getHeight() - rectangle.getHeight().convert() - rectangle.getY().convert() + position.y,
                    rectangle.getWidth().convert(), rectangle.getHeight().convert());

            fillStyle.end(drawer, viewport);
        }
    }
}
