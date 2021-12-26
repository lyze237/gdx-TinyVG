package dev.lyze.gdxtinyvg.types;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import dev.lyze.gdxtinyvg.enums.FractionBits;
import dev.lyze.gdxtinyvg.enums.Range;
import java.io.IOException;
import lombok.*;

/**
 * The unit is the common type for both positions and sizes in the vector
 * graphic. It is encoded as a signed integer with a configurable amount of bits
 * (see Coordinate Range) and fractional bits.
 */
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Unit {
    private int value;
    private Range range;
    private int fractionBits;

    public Unit(LittleEndianInputStream stream, Range range, int fractionBits) throws IOException {
        this(range.read(stream), range, fractionBits);
    }

    public Unit(Unit unit) {
        this.value = unit.value;
        this.range = unit.range;
        this.fractionBits = unit.fractionBits;
    }

    /**
     * @return Returns the actual float value.
     */
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
