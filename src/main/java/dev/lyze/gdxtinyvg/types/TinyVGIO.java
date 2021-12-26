package dev.lyze.gdxtinyvg.types;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import dev.lyze.gdxtinyvg.enums.Range;
import java.io.IOException;

/**
 * Utility class to read and write TVG Types.
 */
public class TinyVGIO {
    public static <TData> TData read(Class<TData> clazz, LittleEndianInputStream stream, Range range, int fractionBits)
            throws IOException {
        if (clazz == Unit.class) {
            return (TData) new Unit(stream, range, fractionBits);
        } else if (clazz == UnitPoint.class) {
            return (TData) new UnitPoint(stream, range, fractionBits);
        } else if (clazz == UnitLine.class) {
            return (TData) new UnitLine(stream, range, fractionBits);
        } else if (clazz == UnitRectangle.class) {
            return (TData) new UnitRectangle(stream, range, fractionBits);
        }

        throw new IllegalArgumentException("Unknown type");
    }
}
