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
import java.io.IOException;
import lombok.var;

public class OutlineFillRectanglesCommand extends Command {
    private float lineWidth;
    private Style fillStyle, lineStyle;
    private UnitRectangle[] rectangles;

    public OutlineFillRectanglesCommand(TinyVG tinyVG) {
        super(CommandType.OUTLINE_FILL_RECTANGLES, tinyVG);
    }

    @Override
    public void read(LittleEndianInputStream stream, StyleType primaryStyleType) throws IOException {
        var header = getTinyVG().getHeader();

        var rectangleStyleByte = stream.readUnsignedByte();

        var rectangleCounts = (rectangleStyleByte & 0b0011_1111) + 1;
        var secondaryStyleType = StyleType.valueOf((rectangleStyleByte & 0b1100_0000) >> 6);

        fillStyle = primaryStyleType.read(stream, getTinyVG());
        lineStyle = secondaryStyleType.read(stream, getTinyVG());

        lineWidth = TinyVGIO.Units.read(stream, header.getCoordinateRange(), header.getFractionBits()).convert();

        rectangles = new UnitRectangle[rectangleCounts];
        for (int i = 0; i < rectangles.length; i++)
            rectangles[i] = TinyVGIO.Rectangles.read(stream, header.getCoordinateRange(), header.getFractionBits());
    }

    @Override
    public void draw(GradientShapeDrawer drawer, Viewport viewport) {
        for (var rectangle : rectangles) {
            var header = getTinyVG().getHeader();

            var position = getTinyVG().getPosition();
            var scale = getTinyVG().getScale();

            fillStyle.start(drawer, viewport);
            drawer.filledRectangle(rectangle.getX().convert() + position.x,
                    header.getHeight() - rectangle.getHeight().convert() - rectangle.getY().convert() + position.y,
                    rectangle.getWidth().convert(), rectangle.getHeight().convert());
            fillStyle.end(drawer, viewport);

            lineStyle.start(drawer, viewport);
            drawer.rectangle(rectangle.getX().convert() + position.x,
                    header.getHeight() - rectangle.getHeight().convert() - rectangle.getY().convert() + position.y,
                    rectangle.getWidth().convert(), rectangle.getHeight().convert(), lineWidth);
            lineStyle.end(drawer, viewport);
        }
    }
}
