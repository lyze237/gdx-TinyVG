package dev.lyze.gdxtinyvg.types;

import dev.lyze.gdxtinyvg.enums.FractionBits;
import dev.lyze.gdxtinyvg.enums.Range;
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
    private int fractionBits;

    public float convert() {
        int num = value >> fractionBits;
        int decimals = value << -fractionBits >>> -fractionBits;
        return num + FractionBits.calculate(decimals, fractionBits);
    }

    public String toString() {
        return "Unit(FloatValue=" + convert() + ", value=" + this.getValue() + ", range=" + this.getRange()
                + ", fractionBits=" + this.getFractionBits() + ")";
    }
}
