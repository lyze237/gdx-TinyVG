package dev.lyze.gdxtinyvg.styles;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.GradientShapeDrawer;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.enums.Range;
import dev.lyze.gdxtinyvg.enums.StyleType;
import dev.lyze.gdxtinyvg.types.TinyVGIO;
import dev.lyze.gdxtinyvg.types.UnitPoint;
import dev.lyze.gdxtinyvg.utils.StreamUtils;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.var;

@ToString
@EqualsAndHashCode(callSuper = false)
public class LinearGradientStyle extends Style {
    @Getter private UnitPoint point1, point2;
    @Getter private int colorIndex1, colorIndex2;

    public LinearGradientStyle(TinyVG tinyVG) {
        super(tinyVG);
    }

    @Override
    public void read(LittleEndianInputStream stream, Range range, int scale) throws IOException {
        point1 = TinyVGIO.Points.read(stream, range, scale);
        point2 = TinyVGIO.Points.read(stream, range, scale);

        colorIndex1 = StreamUtils.readVarUInt(stream);
        colorIndex2 = StreamUtils.readVarUInt(stream);
    }

    @Override
    public void start(GradientShapeDrawer drawer, Viewport viewport) {
        drawer.setGradientStyle(StyleType.LINEAR);
        drawer.setGradientColors(getTinyVG().getColorTable()[colorIndex1], getTinyVG().getColorTable()[colorIndex2]);

        var header = getTinyVG().getHeader();
        var position = getTinyVG().getPosition();
        var scale = getTinyVG().getScale();

        drawer.setPositions(point1.getX().convert() + position.x,
                header.getHeight() - point1.getY().convert() + position.y, point2.getX().convert() + position.x,
                header.getHeight() - point2.getY().convert() + position.y, viewport);

        drawer.applyShaderValues();
    }
}
