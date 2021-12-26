package dev.lyze.gdxtinyvg.types;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import dev.lyze.gdxtinyvg.enums.Range;
import java.io.IOException;
import lombok.*;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UnitLine {
    /**
     * Start point of the line.
     */
    private UnitPoint start;
    /**
     * End point of the line.
     */
    private UnitPoint end;

    public UnitLine(LittleEndianInputStream stream, Range range, int fractionBits) throws IOException {
        this(new UnitPoint(stream, range, fractionBits), new UnitPoint(stream, range, fractionBits));
    }
}
