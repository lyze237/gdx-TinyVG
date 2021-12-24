package dev.lyze.gdxtinyvg.types;

import lombok.*;

/**
 * Points are a X and Y coordinate pair.
 */
@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UnitPoint {
    /**
     * Horizontal distance of the point to the origin.
     */
    private Unit x;
    /**
     * Vertical distance of the point to the origin.
     */
    private Unit y;
}
