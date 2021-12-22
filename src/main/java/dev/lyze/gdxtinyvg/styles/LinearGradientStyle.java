package dev.lyze.gdxtinyvg.styles;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.GradientShapeDrawer;
import dev.lyze.gdxtinyvg.StreamUtils;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.enums.Range;
import dev.lyze.gdxtinyvg.enums.StyleType;
import dev.lyze.gdxtinyvg.shapes.UnitPoint;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

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
        point1 = new UnitPoint();
        point1.read(stream, range, scale);

        point2 = new UnitPoint();
        point2.read(stream, range, scale);

        colorIndex1 = StreamUtils.readVarUInt(stream);
        colorIndex2 = StreamUtils.readVarUInt(stream);
    }

    @Override
    public void start(GradientShapeDrawer drawer, Viewport viewport) {
        drawer.setGradientStyle(StyleType.LINEAR);
        drawer.setGradientColors(getTinyVG().getColorTable()[colorIndex1], getTinyVG().getColorTable()[colorIndex2]);
        drawer.setPositions(point1.getFloatX(), getTinyVG().getHeader().getHeight() - point1.getFloatY(),
                point2.getFloatX(), getTinyVG().getHeader().getHeight() - point2.getFloatY(), viewport);

        drawer.applyShaderValues();
    }
}
