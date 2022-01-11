package dev.lyze.gdxtinyvg.styles;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.drawers.GradientShapeDrawer;
import dev.lyze.gdxtinyvg.enums.Range;
import dev.lyze.gdxtinyvg.enums.StyleType;
import dev.lyze.gdxtinyvg.utils.StreamUtils;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @see StyleType
 */
@EqualsAndHashCode(callSuper = false)
@ToString
public class FlatColoredStyle extends Style {
    @Getter private int colorIndex;

    public FlatColoredStyle(TinyVG tinyVG) {
        super(tinyVG);
    }

    @Override
    public void read(LittleEndianInputStream stream, Range range, int fractionBits) throws IOException {
        colorIndex = StreamUtils.readVarUInt(stream);
    }

    @Override
    public void start(GradientShapeDrawer drawer) {
        drawer.setGradientColors(getTinyVG().getColorTable()[colorIndex], getTinyVG().getColorTable()[colorIndex]);
        drawer.setGradientStyle(StyleType.FLAT);
        drawer.applyShaderValues();
    }
}
