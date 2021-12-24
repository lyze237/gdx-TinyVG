package dev.lyze.gdxtinyvg.types;

import dev.lyze.gdxtinyvg.enums.Range;
import dev.lyze.gdxtinyvg.enums.Scale;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Unit {
    private int value;
    private Range range;
    private int scale;

    public float convert() {
        int num = value >> scale;
        int decimals = value << -scale >>> -scale;
        return num + Scale.calculate(decimals, scale);
    }

    public String toString() {
        return "Unit(FloatValue=" + convert() + ", value=" + this.getValue() + ", range=" + this.getRange() + ", scale="
                + this.getScale() + ")";
    }
}
