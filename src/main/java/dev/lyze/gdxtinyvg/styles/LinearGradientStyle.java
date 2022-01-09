package dev.lyze.gdxtinyvg.styles;

import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.enums.StyleType;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @see StyleType
 */
@ToString
@EqualsAndHashCode(callSuper = true)
public class LinearGradientStyle extends GradientStyle {
    public LinearGradientStyle(TinyVG tinyVG) {
        super(tinyVG, StyleType.LINEAR);
    }
}
