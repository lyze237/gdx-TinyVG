package dev.lyze.gdxtinyvg.commands;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.LittleEndianInputStream;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.GradientShapeDrawer;
import dev.lyze.gdxtinyvg.StreamUtils;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.shapes.Unit;
import dev.lyze.gdxtinyvg.enums.CommandType;
import dev.lyze.gdxtinyvg.enums.StyleType;
import dev.lyze.gdxtinyvg.styles.Style;
import lombok.var;

import java.io.IOException;

public class FillPolygonCommand extends Command {
    private Polygon polygon;
    private Style fillStyle;

    public FillPolygonCommand(TinyVG tinyVG) {
        super(CommandType.FILL_POLYGON, tinyVG);
    }

    @Override
    public void read(LittleEndianInputStream stream, StyleType styleType) throws IOException {
        var header = getTinyVG().getHeader();

        var pointCount = StreamUtils.readVarUInt(stream) + 1;
        fillStyle = styleType.read(stream, getTinyVG());

        var verts = new float[pointCount];

        for (int i = 0; i < verts.length; i++) {
            var unit = new Unit(header.getCoordinateRange(), header.getScale());
            unit.read(stream);

            verts[i] = unit.getFloatValue();
        }

        polygon = new Polygon(verts);
    }

    @Override
    public void draw(GradientShapeDrawer drawer, Viewport viewport) {

    }
}
