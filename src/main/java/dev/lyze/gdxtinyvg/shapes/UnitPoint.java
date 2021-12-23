package dev.lyze.gdxtinyvg.shapes;

import com.badlogic.gdx.math.Vector2;
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
public class UnitPoint {
    private Unit x, y;

    public void read(LittleEndianInputStream stream, Range range, int scale) throws IOException {
        x = new Unit(range, scale);
        x.read(stream);

        y = new Unit(range, scale);
        y.read(stream);
    }

    public Vector2 convertPoint(TinyVGHeader header) {
        return new Vector2(x.getFloatValue(), header.getHeight() - y.getFloatValue());
    }

    public float convertPointX() {
        return x.getFloatValue();
    }

    public float convertPointY(TinyVGHeader header) {
        return header.getHeight() - y.getFloatValue();
    }
}
