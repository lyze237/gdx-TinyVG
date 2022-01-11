package dev.lyze.gdxtinyvg.enums;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The coordinate range defines how many bits a Unit value uses.
 */
@AllArgsConstructor
public enum Range {
    /**
     * Each Unit takes up 16 bit.
     */
    DEFAULT(0),
    /**
     * Each Unit takes up 8 bit.
     */
    REDUCED(1),
    /**
     * Each Unit takes up 32 bit.
     */
    ENHANCED(2);

    @Getter private final int value;

    /**
     * Converts the stored int index to the enum.
     * 
     * @param value The index.
     * @return The enum according to the index.
     */
    public static Range valueOf(int value) {
        for (Range range : values())
            if (range.value == value)
                return range;

        throw new IllegalArgumentException(String.valueOf(value));
    }

    /**
     * Reads a range object from a tvg file stream.
     * 
     * @param stream The appropriately positioned input stream.
     * @return The int value of the Range unit.
     */
    public int read(LittleEndianInputStream stream) throws IOException {
        switch (this) {
            case DEFAULT:
                return stream.readShort();
            case REDUCED:
                return stream.readByte();
            case ENHANCED:
                return stream.readInt();
            default:
                throw new IllegalArgumentException("Unknown enum value");
        }
    }
}
