package dev.lyze.gdxtinyvg.types;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import dev.lyze.gdxtinyvg.enums.Range;
import java.io.IOException;

/**
 * Utility class to read and write TVG Types.
 */
public class TinyVGIO {
    public static class Units {
        public static Unit read(LittleEndianInputStream stream, Range range, int fractionBits) throws IOException {
            return new Unit(range.read(stream), range, fractionBits);
        }
    }

    public static class Points {
        public static UnitPoint read(LittleEndianInputStream stream, Range range, int fractionBits) throws IOException {
            return new UnitPoint(Units.read(stream, range, fractionBits), Units.read(stream, range, fractionBits));
        }
    }

    public static class Lines {
        public static UnitLine read(LittleEndianInputStream stream, Range range, int fractionBits) throws IOException {
            return new UnitLine(Points.read(stream, range, fractionBits), Points.read(stream, range, fractionBits));
        }
    }

    public static class Rectangles {
        public static UnitRectangle read(LittleEndianInputStream stream, Range range, int fractionBits)
                throws IOException {
            return new UnitRectangle(Units.read(stream, range, fractionBits), Units.read(stream, range, fractionBits),
                    Units.read(stream, range, fractionBits), Units.read(stream, range, fractionBits));
        }
    }

    public static <TData> TData read(Class<TData> clazz, LittleEndianInputStream stream, Range range, int fractionBits)
            throws IOException {
        if (clazz == Unit.class) {
            return (TData) Units.read(stream, range, fractionBits);
        } else if (clazz == UnitPoint.class) {
            return (TData) Points.read(stream, range, fractionBits);
        } else if (clazz == UnitLine.class) {
            return (TData) Lines.read(stream, range, fractionBits);
        } else if (clazz == UnitRectangle.class) {
            return (TData) Rectangles.read(stream, range, fractionBits);
        }

        throw new IllegalArgumentException("Unknown type");
    }
}
