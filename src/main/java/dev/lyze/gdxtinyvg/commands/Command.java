package dev.lyze.gdxtinyvg.commands;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;
import dev.lyze.gdxtinyvg.enums.CommandType;
import dev.lyze.gdxtinyvg.enums.StyleType;
import java.io.IOException;
import lombok.Getter;

public abstract class Command {
    @Getter private final CommandType type;
    @Getter private final TinyVG tinyVG;

    public Command(CommandType type, TinyVG tinyVG) {
        this.type = type;
        this.tinyVG = tinyVG;
    }

    public abstract void read(LittleEndianInputStream stream, StyleType primaryStyleType) throws IOException;

    public abstract void draw(TinyVGShapeDrawer drawer, Viewport viewport);

    public void onCurveSegmentsChanged() {

    }
}
