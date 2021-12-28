package dev.lyze.gdxtinyvg.types;

import com.badlogic.gdx.utils.Array;
import dev.lyze.gdxtinyvg.commands.paths.UnitPathCommand;
import dev.lyze.gdxtinyvg.commands.paths.UnitPathSegment;
import lombok.Data;

@Data
public class ParsedPathSegment {
    private float[] vertices;
    private Array<Vector2WithWidth> points;
    private UnitPathSegment source;

    public ParsedPathSegment(UnitPathSegment segment, Array<Vector2WithWidth> points) {
        this.source = segment;
        this.points = points;
        this.vertices = new float[points.size * 2];
    }

    public UnitPathCommand getLastCommand() {
        return source.getCommands()[source.getCommands().length - 1];
    }
}
