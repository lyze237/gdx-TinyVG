package dev.lyze.gdxtinyvg.commands;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.LittleEndianInputStream;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.GradientShapeDrawer;
import dev.lyze.gdxtinyvg.StreamUtils;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.enums.CommandType;
import dev.lyze.gdxtinyvg.enums.StyleType;
import dev.lyze.gdxtinyvg.shapes.UnitRectangle;
import dev.lyze.gdxtinyvg.styles.Style;
import java.io.IOException;
import lombok.var;

public class FillRectanglesCommand extends Command {
    private Style fillStyle;
    private Rectangle[] rectangles;

    public FillRectanglesCommand(TinyVG tinyVG) {
        super(CommandType.OUTLINE_FILL_RECTANGLES, tinyVG);
    }

    @Override
    public void read(LittleEndianInputStream stream, StyleType primaryStyleType) throws IOException {
        var header = getTinyVG().getHeader();

        var rectangleCounts = StreamUtils.readVarUInt(stream) + 1;

        fillStyle = primaryStyleType.read(stream, getTinyVG());

        rectangles = new Rectangle[rectangleCounts];
        for (int i = 0; i < rectangles.length; i++) {
            var rect = new UnitRectangle();
            rect.read(stream, header.getCoordinateRange(), header.getScale());

            rectangles[i] = rect.convertRectangle(header);
        }
    }

    @Override
    public void draw(GradientShapeDrawer drawer, Viewport viewport) {
        for (Rectangle rectangle : rectangles) {
            fillStyle.start(drawer, viewport);
            drawer.filledRectangle(rectangle);
            fillStyle.end(drawer, viewport);
        }
    }
}
