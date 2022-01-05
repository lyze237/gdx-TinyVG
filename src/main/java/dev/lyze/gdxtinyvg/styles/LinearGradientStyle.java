package dev.lyze.gdxtinyvg.styles;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;
import dev.lyze.gdxtinyvg.enums.Range;
import dev.lyze.gdxtinyvg.enums.StyleType;
import dev.lyze.gdxtinyvg.types.UnitPoint;
import dev.lyze.gdxtinyvg.utils.StreamUtils;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.var;

/**
 * @see StyleType
 */
@ToString
@EqualsAndHashCode(callSuper = false)
public class LinearGradientStyle extends Style {
    @Getter private UnitPoint point1, point2;
    @Getter private int colorIndex1, colorIndex2;

    public LinearGradientStyle(TinyVG tinyVG) {
        super(tinyVG);
    }

    @Override
    public void read(LittleEndianInputStream stream, Range range, int fractionBits) throws IOException {
        point1 = new UnitPoint(stream, range, fractionBits);
        point2 = new UnitPoint(stream, range, fractionBits);

        colorIndex1 = StreamUtils.readVarUInt(stream);
        colorIndex2 = StreamUtils.readVarUInt(stream);
    }

    @Override
    public void start(TinyVGShapeDrawer drawer, Viewport viewport) {
        drawer.setGradientStyle(StyleType.LINEAR);
        drawer.setGradientColors(getTinyVG().getColorTable()[colorIndex1], getTinyVG().getColorTable()[colorIndex2]);

        var position = getTinyVG().getPosition();
        var scale = getTinyVG().getScale();

        drawer.setPositions(point1.getX().convert() * scale.x + position.x,
                getTinyVG().getHeight() - point1.getY().convert() * scale.y + position.y,
                point2.getX().convert() * scale.x + position.x,
                getTinyVG().getHeight() - point2.getY().convert() * scale.y + position.y, viewport);

        drawer.applyShaderValues();
    }
}
