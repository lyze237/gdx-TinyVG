package dev.lyze.gdxtinyvg.commands;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;
import dev.lyze.gdxtinyvg.enums.CommandType;
import dev.lyze.gdxtinyvg.enums.StyleType;

/**
 * If this command is read, the TinyVG file has ended. This command must have
 * prim_style_kind to be set to 0, so the last byte of every TinyVG file is
 * 0x00.
 */
public class EndOfDocumentCommand extends Command {
    public EndOfDocumentCommand(TinyVG tinyVG) {
        super(CommandType.END_OF_DOCUMENT, tinyVG);
    }

    @Override
    public void read(LittleEndianInputStream stream, StyleType styleType) {
    }

    @Override
    public void draw(TinyVGShapeDrawer drawer, Viewport viewport) {
    }
}
