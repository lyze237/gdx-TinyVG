package dev.lyze.gdxtinyvg.enums;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.LittleEndianInputStream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.var;

import java.io.IOException;

@AllArgsConstructor
public enum ColorEncoding {
    RGBA8888(0),
    RGB565(1),
    RGBAF32(2),
    CUSTOM(3);

    @Getter
    private final int value;

    public static ColorEncoding valueOf(int value) {
        for (ColorEncoding encoding : values())
            if (encoding.value == value)
                return encoding;

        throw new IllegalArgumentException(String.valueOf(value));
    }

    public Color read(LittleEndianInputStream stream) throws IOException {
        var r = 0f;
        var g = 0f;
        var b = 0f;
        var a = 0f;

        switch (this) {
            case RGBA8888:
                r = stream.readUnsignedByte() / 255f;
                g = stream.readUnsignedByte() / 255f;
                b = stream.readUnsignedByte() / 255f;
                a = stream.readUnsignedByte() / 255f;
                break;

            case RGB565:
                var num = stream.readShort();

                r = (num & 0b0000_0000_0001_1111) / 31f;
                g = ((num & 0b0000_0111_1110_0000) >> 5) / 63f;
                b = ((num & 0b1111_1000_0000_0000) >> 11) / 31f;
                break;

            case RGBAF32:
                r = stream.readFloat();
                g = stream.readFloat();
                b = stream.readFloat();
                a = stream.readFloat();
                break;

            case CUSTOM:
                throw new IllegalArgumentException("CUSTOM Color Format not supported");
            default:
                throw new IllegalArgumentException("Unknown Color Format enum");
        }

        return new Color(r, g, b, a);
    }
}
