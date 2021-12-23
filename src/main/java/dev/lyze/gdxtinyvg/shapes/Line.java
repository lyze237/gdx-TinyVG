package dev.lyze.gdxtinyvg.shapes;

import com.badlogic.gdx.math.Vector2;
import lombok.*;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Line {
    @Getter private Vector2 start, end;

    public Line(float startX, float startY, float endX, float endY) {
        start = new Vector2(startX, startY);
        end = new Vector2(endX, endY);
    }
}
