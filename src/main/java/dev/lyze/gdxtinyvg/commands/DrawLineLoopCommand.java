package dev.lyze.gdxtinyvg.commands;

import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.enums.CommandType;

/**
 * Draws a polygon.
 */
public class DrawLineLoopCommand extends DrawLineThingCommand {
    public DrawLineLoopCommand(TinyVG tinyVG) {
        super(tinyVG, CommandType.DRAW_LINE_LOOP, false);
    }
}
