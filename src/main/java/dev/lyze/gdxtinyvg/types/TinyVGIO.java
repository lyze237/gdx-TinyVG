package dev.lyze.gdxtinyvg.types;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import dev.lyze.gdxtinyvg.enums.Range;
import java.io.IOException;

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
}
