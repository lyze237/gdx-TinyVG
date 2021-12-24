package dev.lyze.gdxtinyvg;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import dev.lyze.gdxtinyvg.enums.ColorEncoding;
import dev.lyze.gdxtinyvg.enums.Range;
import dev.lyze.gdxtinyvg.utils.StreamUtils;
import java.io.IOException;
import java.util.Arrays;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.var;

/**
 * Each TVG file starts with a header defining some global values for the file
 * like scale and image size.
 */
@EqualsAndHashCode
public class TinyVGHeader {
    /**
     * Must be 1. For future versions, this field might decide how the rest of the
     * format looks like.
     */
    @Getter private int version;

    /**
     * Defines the number of fraction bits in a Unit value.
     */
    @Getter private int fractionBits;
    /**
     * Defines the type of color information that is used in the color table.
     */
    @Getter private ColorEncoding colorEncoding;
    /**
     * Defines the number of total bits in a Unit value and thus the overall
     * precision of the file.
     */
    @Getter private Range coordinateRange;

    /**
     * Encodes the maximum width of the output file in pixels.
     */
    @Getter private int width;
    /**
     * Encodes the maximum height of the output file in pixels.
     */
    @Getter private int height;

    /**
     * The number of colors in the color table.
     */
    @Getter private int colorTableCount;

    public void read(LittleEndianInputStream stream) throws IOException {
        var magic = StreamUtils.readNBytes(stream, 2);

        if (magic[0] != 0x72 && magic[1] != 0x56)
            throw new IllegalStateException("Bad magic numbers: " + Arrays.toString(magic) + ", expected 0x72, 0x56");

        version = stream.readUnsignedByte();

        if (version != 1)
            throw new IllegalStateException("Bad version: " + version + ", expected 1");

        // u4, u2, u2
        var fractionBitsColorCoordinateRange = stream.readUnsignedByte();

        fractionBits = (fractionBitsColorCoordinateRange & 0b0000_1111);
        colorEncoding = ColorEncoding.valueOf((fractionBitsColorCoordinateRange & 0b0011_0000) >> 4);
        coordinateRange = Range.valueOf((fractionBitsColorCoordinateRange & 0b1100_0000) >> 6);

        width = coordinateRange.read(stream);
        height = coordinateRange.read(stream);

        colorTableCount = StreamUtils.readVarUInt(stream);
    }
}
