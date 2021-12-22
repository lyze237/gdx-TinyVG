package dev.lyze.gdxtinyvg.enums;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.styles.FlatColoredStyle;
import dev.lyze.gdxtinyvg.styles.LinearGradientStyle;
import dev.lyze.gdxtinyvg.styles.RadialGradientStyle;
import dev.lyze.gdxtinyvg.styles.Style;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;

@AllArgsConstructor
public enum StyleType {
    FLAT(0),
    LINEAR(1),
    RADIAL(2);

    @Getter
    private final int value;

    public static StyleType valueOf(int value) {
        for (StyleType styleType : values())
            if (styleType.value == value)
                return styleType;

        throw new IllegalArgumentException(String.valueOf(value));
    }

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

        style.read(stream, tinyVG.getHeader().getCoordinateRange(), tinyVG.getHeader().getScale());

        return style;
    }
}
