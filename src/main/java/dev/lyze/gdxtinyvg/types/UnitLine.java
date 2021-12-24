package dev.lyze.gdxtinyvg.types;

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
}
