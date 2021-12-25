package dev.lyze.gdxtinyvg.commands;

import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.enums.CommandType;

/**
 * Draws a list of consecutive lines
 */
public class DrawLineStripCommand extends DrawLineThingCommand {
    public DrawLineStripCommand(TinyVG tinyVG) {
        super(tinyVG, CommandType.DRAW_LINE_STRIP, true);
    }
}
