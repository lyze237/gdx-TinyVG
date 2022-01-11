package dev.lyze.gdxtinyvg.styles;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.drawers.GradientShapeDrawer;
import dev.lyze.gdxtinyvg.enums.Range;
import dev.lyze.gdxtinyvg.enums.StyleType;
import dev.lyze.gdxtinyvg.types.UnitPoint;
import dev.lyze.gdxtinyvg.utils.StreamUtils;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString
public abstract class GradientStyle extends Style {
    private final StyleType type;

    @Getter private UnitPoint point1, point2;
    @Getter private int colorIndex1, colorIndex2;

    public GradientStyle(TinyVG tinyVG, StyleType type) {
        super(tinyVG);
        this.type = type;
    }

    @Override
    public void read(LittleEndianInputStream stream, Range range, int fractionBits) throws IOException {
        point1 = new UnitPoint(stream, range, fractionBits);
        point2 = new UnitPoint(stream, range, fractionBits);

        colorIndex1 = StreamUtils.readVarUInt(stream);
        colorIndex2 = StreamUtils.readVarUInt(stream);
    }

    @Override
    public void start(GradientShapeDrawer drawer) {
        drawer.setGradientStyle(type);
        drawer.setGradientColors(getTinyVG().getColorTable()[colorIndex1], getTinyVG().getColorTable()[colorIndex2]);

        drawer.setPositions(point1.getX().convert(), point1.getY().convert(), point2.getX().convert(),
                point2.getY().convert());

        drawer.applyShaderValues();
    }
}
