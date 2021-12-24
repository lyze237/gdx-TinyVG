package dev.lyze.gdxtinyvg.types;

import lombok.*;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UnitRectangle {
    private Unit x, y;
    private Unit width, height;
}
