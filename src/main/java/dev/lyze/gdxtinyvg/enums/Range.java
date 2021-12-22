package dev.lyze.gdxtinyvg.enums;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Range {
    DEFAULT(0), REDUCED(1), ENHANCED(2);

    @Getter private final int value;

    public static Range valueOf(int value) {
        for (Range range : values())
            if (range.value == value)
                return range;

        throw new IllegalArgumentException(String.valueOf(value));
    }

    public int read(LittleEndianInputStream stream) throws IOException {
        switch (this) {
            case DEFAULT:
                return stream.readUnsignedShort();
            case REDUCED:
                return stream.readUnsignedByte();
            case ENHANCED:
                return stream.readInt();
            default:
                throw new IllegalArgumentException("Unknown enum value");
        }
    }
}
