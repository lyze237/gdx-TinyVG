package dev.lyze.gdxtinyvg.shapes;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import dev.lyze.gdxtinyvg.enums.Range;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.var;

import java.io.IOException;

@EqualsAndHashCode
@ToString
@Getter
public class UnitPoint {
    private Unit x, y;

    public UnitPoint() {

    }

    public UnitPoint(Unit x, Unit y) {
        this.x = x;
        this.y = y;
    }

    public void read(LittleEndianInputStream stream, Range range, int scale) throws IOException {
        x = new Unit(range, scale);
        x.read(stream);

        y = new Unit(range, scale);
        y.read(stream);
    }

    public float getFloatX() {
        return x.getFloatValue();
    }

    public float getFloatY() {
        return y.getFloatValue();
    }
}
