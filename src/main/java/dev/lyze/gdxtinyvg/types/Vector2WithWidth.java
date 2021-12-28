package dev.lyze.gdxtinyvg.types;

import com.badlogic.gdx.math.Vector2;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Vector2WithWidth {
    private Vector2 point;
    private float width;
}
