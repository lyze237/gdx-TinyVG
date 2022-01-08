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

        var positionX = getTinyVG().getPositionX();
        var positionY = getTinyVG().getPositionY();

        var scaleX = getTinyVG().getScaleX();
        var scaleY = getTinyVG().getScaleY();

        drawer.setPositions(point1.getX().convert() * scaleX + positionX,
                getTinyVG().getHeight() - point1.getY().convert() * scaleY + positionY,
                point2.getX().convert() * scaleX + positionX,
                getTinyVG().getHeight() - point2.getY().convert() * scaleY + positionY, viewport);

        drawer.applyShaderValues();
    }
}
