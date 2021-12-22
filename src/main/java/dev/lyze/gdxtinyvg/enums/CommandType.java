package dev.lyze.gdxtinyvg.enums;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import dev.lyze.gdxtinyvg.commands.Command;
import dev.lyze.gdxtinyvg.commands.EndOfDocumentCommand;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.commands.OutlineFillRectanglesCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;

@AllArgsConstructor
public enum CommandType {
    END_OF_DOCUMENT(0),

    FILL_POLYGON(1),
    FILL_RECTANGLE(2),
    FILL_PATH(3),

    DRAW_LINES(4),
    DRAW_LINE_LOOP(5),
    DRAW_LINE_STRIP(6),
    DRAW_LINE_PATH(7),

    OUTLINE_FILL_POLYGON(8),
    OUTLINE_FILL_RECTANGLES(9),
    OUTLINE_FILL_PATH(10);

    @Getter
    private final int value;

    public static CommandType valueOf(int value) {
        for (CommandType command : values())
            if (command.value == value)
                return command;

        throw new IllegalArgumentException(String.valueOf(value));
    }

    public Command read(LittleEndianInputStream stream, StyleType styleType, TinyVG tinyVG) throws IOException {
        Command command = null;

        switch (this) {
            case END_OF_DOCUMENT:
                command = new EndOfDocumentCommand(tinyVG);
                break;
            case FILL_POLYGON:
                break;
            case FILL_RECTANGLE:
                break;
            case FILL_PATH:
                break;
            case DRAW_LINES:
                break;
            case DRAW_LINE_LOOP:
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

        if (command != null) // TODO remove check
            command.read(stream, styleType);

        return command;
    }
}
