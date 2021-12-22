package dev.lyze.gdxtinyvg.shapes;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.LittleEndianInputStream;
import dev.lyze.gdxtinyvg.TinyVGHeader;
import dev.lyze.gdxtinyvg.enums.Range;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.IOException;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
public class UnitRectangle {
    private Unit x, y;
    private Unit width, height;

    public UnitRectangle(Unit x, Unit y, Unit width, Unit height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

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
        return new Rectangle(x.getFloatValue(), header.getHeight() - y.getFloatValue() - height.getFloatValue(), width.getFloatValue(), height.getFloatValue());
    }
}
