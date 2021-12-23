package dev.lyze.gdxtinyvg.shapes;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import dev.lyze.gdxtinyvg.TinyVGHeader;
import dev.lyze.gdxtinyvg.enums.Range;
import java.io.IOException;
import lombok.*;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UnitLine {
    private UnitPoint start, end;

    public void read(LittleEndianInputStream stream, Range range, int scale) throws IOException {
        start = new UnitPoint();
        start.read(stream, range, scale);

        end = new UnitPoint();
        end.read(stream, range, scale);
    }

    public Line convertLine(TinyVGHeader header) {
        return new Line(start.convertPointX(), start.convertPointY(header), end.convertPointX(),
                end.convertPointY(header));
    }
}
