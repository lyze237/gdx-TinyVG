package dev.lyze.gdxtinyvg.styles;

import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.enums.StyleType;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @see StyleType
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RadialGradientStyle extends GradientStyle {
    public RadialGradientStyle(TinyVG tinyVG) {
        super(tinyVG, StyleType.RADIAL);
    }
}
