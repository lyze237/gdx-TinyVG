package dev.lyze.gdxtinyvg.enums;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.styles.FlatColoredStyle;
import dev.lyze.gdxtinyvg.styles.LinearGradientStyle;
import dev.lyze.gdxtinyvg.styles.RadialGradientStyle;
import dev.lyze.gdxtinyvg.styles.Style;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Color style of the drawn shape.
 */
@AllArgsConstructor
public enum StyleType {
    /**
     * The shape is uniformly colored with a single color.
     * <p>
     * The shape is uniformly colored with the color at color_index in the color
     * table.
     * </p>
     */
    FLAT(0),
    /**
     * The shape is colored with a linear gradient.
     * <p>
     * The gradient is formed by a mental line between point_0 and point_1. The
     * color at point_0 is the color at color_index_0 in the color table, the color
     * at point_1 is the color at color_index_1 in the color table. On the line, the
     * color is linearly interpolated between the two points. Each point that is not
     * on the line is orthogonally projected to the line and the color at that point
     * is sampled. Points that are not projectable onto the line have either the
     * color at point_0 if they are closed to point_0 or vice versa for point_1.
     * </p>
     */
    LINEAR(1),
    /**
     * The shape is colored with a radial gradient.
     * <p>
     * The gradient is formed by a mental circle with the center at point_0 and
     * point_1 being somewhere on the circle outline. Thus, the radius of said
     * circle is the distance between point_0 and point_1. The color at point_0 is
     * the color at color_index_0 in the color table, the color on the circle
     * outline is the color at color_index_1 in the color table. If a sampled point
     * is inside the circle, a linear color interpolation is done based on the
     * distance to the center and the radius. If the point is not in the circle
     * itself, the color at color_index_1 is always taken.
     * </p>
     */
    RADIAL(2);

    @Getter private final int value;

    /**
     * Converts the stored int index to the enum.
     * 
     * @param value The index.
     * @return The enum according to the index.
     */
    public static StyleType valueOf(int value) {
        for (StyleType styleType : values())
            if (styleType.value == value)
                return styleType;

        throw new IllegalArgumentException(String.valueOf(value));
    }

    /**
     * Reads a enum from a tvg file stream.
     * 
     * @param stream The appropriately positioned input stream.
     * @param tinyVG A reference to the tvg file.
     * @return The style.
     */
    public Style read(LittleEndianInputStream stream, TinyVG tinyVG) throws IOException {
        Style style;

        switch (this) {
            case FLAT:
                style = new FlatColoredStyle(tinyVG);
                break;
            case LINEAR:
                style = new LinearGradientStyle(tinyVG);
                break;
            case RADIAL:
                style = new RadialGradientStyle(tinyVG);
                break;
            default:
                throw new IllegalArgumentException("Unknown enum");
        }

        style.read(stream, tinyVG.getHeader().getCoordinateRange(), tinyVG.getHeader().getFractionBits());

        return style;
    }
}
