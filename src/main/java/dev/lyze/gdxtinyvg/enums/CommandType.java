package dev.lyze.gdxtinyvg.enums;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.commands.*;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TinyVG files contain a sequence of draw commands that must be executed in the
 * defined order to get the final result. Each draw command adds a new 2D
 * primitive to the graphic.
 */
@AllArgsConstructor
public enum CommandType {
    /**
     * This command determines the end of file.
     */
    END_OF_DOCUMENT(0),

    /**
     * This command fills an N-gon.
     */
    FILL_POLYGON(1),
    /**
     * This command fills a set of rectangles.
     */
    FILL_RECTANGLE(2),
    /**
     * This command fills a free-form path.
     */
    FILL_PATH(3),

    /**
     * This command draws a set of lines.
     */
    DRAW_LINES(4),
    /**
     * This command draws the outline of a polygon.
     */
    DRAW_LINE_LOOP(5),
    /**
     * This command draws a list of end-to-end lines.
     */
    DRAW_LINE_STRIP(6),
    /**
     * This command draws a free-form path.
     */
    DRAW_LINE_PATH(7),

    /**
     * This command draws a filled polygon with an outline.
     */
    OUTLINE_FILL_POLYGON(8),
    /**
     * This command draws several filled rectangles with an outline.
     */
    OUTLINE_FILL_RECTANGLES(9),
    /**
     * This command combines the fill and draw path command into one.
     */
    OUTLINE_FILL_PATH(10);

    @Getter private final int value;

    /**
     * Converts the stored int index to the enum.
     * 
     * @param value The index.
     * @return The enum according to the index.
     */
    public static CommandType valueOf(int value) {
        for (CommandType command : values()) {
            if (command.value == value) {
                return command;
            }
        }

        throw new IllegalArgumentException(String.valueOf(value));
    }

    /**
     * Reads a command from a tvg file stream.
     * 
     * @param stream    The appropriately positioned input stream.
     * @param styleType The primary style used for the command.
     * @param tinyVG    A reference to the tvg file.
     * @return The appropriately setup command.
     */
    public Command read(LittleEndianInputStream stream, StyleType styleType, TinyVG tinyVG) throws IOException {
        Command command = null;

        switch (this) {
            case END_OF_DOCUMENT:
                command = new EndOfDocumentCommand(tinyVG);
                break;
            case FILL_POLYGON:
                break;
            case FILL_RECTANGLE:
                command = new FillRectanglesCommand(tinyVG);
                break;
            case FILL_PATH:
                break;
            case DRAW_LINES:
                command = new DrawLinesCommand(tinyVG);
                break;
            case DRAW_LINE_LOOP:
                command = new DrawLineLoopCommand(tinyVG);
                break;
            case DRAW_LINE_STRIP:
                break;
            case DRAW_LINE_PATH:
                break;
            case OUTLINE_FILL_POLYGON:
                break;
            case OUTLINE_FILL_RECTANGLES:
                command = new OutlineFillRectanglesCommand(tinyVG);
                break;
            case OUTLINE_FILL_PATH:
                break;
            default:
                throw new IllegalArgumentException("Unknown enum");
        }

        if (command != null)
            command.read(stream, styleType);

        return command;
    }
}
