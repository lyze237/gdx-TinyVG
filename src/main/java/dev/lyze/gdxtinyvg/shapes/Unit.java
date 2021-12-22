package dev.lyze.gdxtinyvg.shapes;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import dev.lyze.gdxtinyvg.enums.Range;
import dev.lyze.gdxtinyvg.enums.Scale;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.IOException;

@EqualsAndHashCode
@Getter
public class Unit {
    private int value;
    private final Range range;
    private final int scale;

    public Unit(Range range, int scale) {
        this.range = range;
        this.scale = scale;
    }

    public void read(LittleEndianInputStream stream) throws IOException {
        value = range.read(stream);
    }

    public float getFloatValue() {
        int num = value >> scale;
        int decimals = value << -scale >>> -scale;
        return num + Scale.calculate(decimals, scale);
    }


    public String toString() {
        return "Unit(FloatValue=" + getFloatValue() + ", value=" + this.getValue() + ", range=" + this.getRange() + ", scale=" + this.getScale() + ")";
    }
}
