package dev.lyze.gdxtinyvg.enums;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.LittleEndianInputStream;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.var;

/**
 * The color encoding defines which format the colors in the color table will
 * have.
 */
@AllArgsConstructor
public enum ColorEncoding {
    /**
     * Each color is a 4-tuple (red, green, blue, alpha) of bytes with the color
     * channels encoded in sRGB and the alpha as linear alpha.
     */
    RGBA8888(0),
    /**
     * Each color is encoded as a 3-tuple (red, green, blue) with 16 bit per color.
     * While red and blue both use 5 bit, the green channel uses 6 bit. red uses bit
     * range 0...4, green bits 5...10 and blue bits 11...15.
     */
    RGB565(1),
    /**
     * Each color is a 4-tuple (red, green ,blue, alpha) of binary32 IEEE 754
     * floating point value with the color channels encoded in sRGB and the alpha as
     * linear alpha. A color value of 1.0 is full intensity, while a value of 0.0 is
     * zero intensity.
     */
    RGBAF32(2),
    /**
     * The custom color encoding is defined undefined. The information how these
     * colors are encoded must be implemented via external means.
     */
    CUSTOM(3);

    @Getter private final int value;

    /**
     * Converts the stored int index to the enum.
     * 
     * @param value The index.
     * @return The enum according to the index.
     */
    public static ColorEncoding valueOf(int value) {
        for (ColorEncoding encoding : values())
            if (encoding.value == value)
                return encoding;

        throw new IllegalArgumentException(String.valueOf(value));
    }

    /**
     * Reads a color from a tvg file stream according to the specified encoding.
     * 
     * @param stream The appropriately positioned input stream.
     * @return The color.
     */
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
