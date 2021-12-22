package dev.lyze.gdxtinyvg.commands;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.GradientShapeDrawer;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.enums.CommandType;
import dev.lyze.gdxtinyvg.enums.StyleType;

public class EndOfDocumentCommand extends Command {
    public EndOfDocumentCommand(TinyVG tinyVG) {
        super(CommandType.END_OF_DOCUMENT, tinyVG);
    }

    @Override
    public void read(LittleEndianInputStream stream, StyleType styleType) {
    }

    @Override
    public void draw(GradientShapeDrawer drawer, Viewport viewport) {
    }
}
