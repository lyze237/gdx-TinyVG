package dev.lyze.gdxtinyvg.types;

import com.badlogic.gdx.utils.Array;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.commands.paths.UnitPathCommand;
import dev.lyze.gdxtinyvg.commands.paths.UnitPathSegment;
import dev.lyze.gdxtinyvg.drawers.chaches.TinyVGDrawerCache;
import lombok.Data;

@Data
public class ParsedPathSegment {
    private Array<Vector2WithWidth> points;
    private UnitPathSegment source;
    private TinyVGDrawerCache cache;

    public ParsedPathSegment(UnitPathSegment segment, Array<Vector2WithWidth> points, TinyVG tinyVG,
            boolean calculateTriangles) {
        this.source = segment;
        this.points = points;
        this.cache = new TinyVGDrawerCache(tinyVG).calculateVertices(this);
        if (calculateTriangles)
            cache.calculateTriangles();
    }

    public UnitPathCommand getLastCommand() {
        return source.getCommands()[source.getCommands().length - 1];
    }
}
