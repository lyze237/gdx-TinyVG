package dev.lyze.gdxtinyvg.types;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import dev.lyze.gdxtinyvg.enums.Range;
import java.io.IOException;

public class TinyVGIO {
    public static class Units {
        public static Unit read(LittleEndianInputStream stream, Range range, int scale) throws IOException {
            return new Unit(range.read(stream), range, scale);
        }
    }

    public static class Points {
        public static UnitPoint read(LittleEndianInputStream stream, Range range, int scale) throws IOException {
            return new UnitPoint(Units.read(stream, range, scale), Units.read(stream, range, scale));
        }
    }

    public static class Lines {
        public static UnitLine read(LittleEndianInputStream stream, Range range, int scale) throws IOException {
            return new UnitLine(Points.read(stream, range, scale), Points.read(stream, range, scale));
        }
    }

    public static class Rectangles {
        public static UnitRectangle read(LittleEndianInputStream stream, Range range, int scale) throws IOException {
            return new UnitRectangle(Units.read(stream, range, scale), Units.read(stream, range, scale),
                    Units.read(stream, range, scale), Units.read(stream, range, scale));
        }
    }
}
