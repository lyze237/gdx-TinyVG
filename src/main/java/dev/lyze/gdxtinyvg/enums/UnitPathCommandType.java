package dev.lyze.gdxtinyvg.enums;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import dev.lyze.gdxtinyvg.commands.paths.*;
import dev.lyze.gdxtinyvg.types.Unit;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum UnitPathCommandType {
    /**
     * A straight line is drawn from the current point to a new point.
     */
    LINE(0),
    /**
     * A straight horizontal line is drawn from the current point to a new x coor-
     * dinate.
     */
    HORIZONTAL_LINE(1),
    /**
     * A straight vertical line is drawn from the current point to a new y coor-
     * diante
     */
    VERTICAL_LINE(2),
    /**
     * A cubic Bézier curve is drawn from the current point to a new point.
     */
    CUBIC_BEZIER(3),
    /**
     * A circle segment is drawn from current point to a new point.
     */
    ARC_CIRCLE(4),
    /**
     * An ellipse segment is drawn from current point to a new point.
     */
    ARC_ELLIPSE(5),
    /**
     * The path is closed, and a straight line is drawn to the starting point.
     */
    CLOSE_PATH(6),
    /**
     * A quadratic Bézier curve is drawn from the current point to a new point.
     */
    QUADRATIC_BEZIER(7);

    @Getter private final int value;

    /**
     * Converts the stored int index to the enum.
     * 
     * @param value The index.
     * @return The enum according to the index.
     */
    public static UnitPathCommandType valueOf(int value) {
        for (UnitPathCommandType range : values())
            if (range.value == value)
                return range;

        throw new IllegalArgumentException(String.valueOf(value));
    }

    /**
     * Reads a range object from a tvg file stream.
     *
     * @param stream The appropriately positioned input stream.
     * @return The appropriately setup command.
     */
    public UnitPathCommand read(LittleEndianInputStream stream, Unit lineWidth, Range range, int fractionBits)
            throws IOException {
        UnitPathCommand command;

        switch (this) {
            case LINE:
                command = new UnitPathLineCommand(lineWidth);
                break;
            case HORIZONTAL_LINE:
                command = new UnitPathHorizontalLineCommand(lineWidth);
                break;
            case VERTICAL_LINE:
                command = new UnitPathVerticalLineCommand(lineWidth);
                break;
            case CUBIC_BEZIER:
                command = new UnitPathCubicBezierCommand(lineWidth);
                break;
            case ARC_CIRCLE:
                command = new UnitPathArcCircleCommand(lineWidth);
                break;
            case ARC_ELLIPSE:
                command = new UnitPathArcEllipseCommand(lineWidth);
                break;
            case CLOSE_PATH:
                command = new UnitPathCloseCommand(lineWidth);
                break;
            case QUADRATIC_BEZIER:
                command = new UnitPathQuadraticBezierCommand(lineWidth);
                break;
            default:
                throw new IllegalArgumentException("Unknown enum");
        }

        command.read(stream, range, fractionBits);

        return command;
    }
}
