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

@EqualsAndHashCode
public class TinyVGHeader {
    @Getter private int version;

    @Getter private int scale;
    @Getter private ColorEncoding colorEncoding;
    @Getter private Range coordinateRange;

    @Getter private int width;
    @Getter private int height;

    @Getter private int colorTableCount;

    public void read(LittleEndianInputStream stream) throws IOException {
        var magic = StreamUtils.readNBytes(stream, 2);

        if (magic[0] != 0x72 && magic[1] != 0x56)
            throw new IllegalStateException("Bad magic numbers: " + Arrays.toString(magic) + ", expected 0x72, 0x56");

        version = stream.readUnsignedByte();

        if (version != 1)
            throw new IllegalStateException("Bad version: " + version + ", expected 1");

        // u4, u2, u2
        var scaleColorCoordinateRange = stream.readUnsignedByte();

        scale = (scaleColorCoordinateRange & 0b0000_1111);
        colorEncoding = ColorEncoding.valueOf((scaleColorCoordinateRange & 0b0011_0000) >> 4);
        coordinateRange = Range.valueOf((scaleColorCoordinateRange & 0b1100_0000) >> 6);

        width = coordinateRange.read(stream);
        height = coordinateRange.read(stream);

        colorTableCount = StreamUtils.readVarUInt(stream);
    }
}
