package dev.lyze.gdxtinyvg.shapes;

import com.badlogic.gdx.math.Rectangle;
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
public class UnitRectangle {
    private Unit x, y;
    private Unit width, height;

    public void read(LittleEndianInputStream stream, Range range, int scale) throws IOException {
        x = new Unit(range, scale);
        x.read(stream);

        y = new Unit(range, scale);
        y.read(stream);

        width = new Unit(range, scale);
        width.read(stream);

        height = new Unit(range, scale);
        height.read(stream);
    }

    public Rectangle convertRectangle(TinyVGHeader header) {
        return new Rectangle(x.getFloatValue(), header.getHeight() - y.getFloatValue() - height.getFloatValue(),
                width.getFloatValue(), height.getFloatValue());
    }
}
