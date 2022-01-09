package dev.lyze.gdxtinyvg;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.commands.Command;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;
import lombok.Getter;
import lombok.Setter;

public class TinyVG {
    /**
     * Each TVG file starts with a header defining some global values for the file
     * like scale and image size.
     */
    @Getter private final TinyVGHeader header;
    /**
     * The color table encodes the palette for this file. It’s binary content is
     * defined by the color_encoding field in the header. For the three defined
     * color encodings, each will yield a list of color_count RGBA tuples.
     */
    @Getter private final Color[] colorTable;

    /**
     * TinyVG files contain a sequence of draw commands that must be executed in the
     * defined order to get the final result. Each draw command adds a new 2D
     * primitive to the graphic.
     */
    @Getter private final Array<Command> commands = new Array<>();

    /**
     * Global position offset value.
     */
    @Getter private float positionX;
    @Getter private float positionY;
    /**
     * Global scale value.
     */
    @Getter private float scaleX = 1;
    @Getter private float scaleY = 1;
    /**
     * Global scale value for all line widths. (Independent from scale)
     */
    @Getter @Setter private float lineWidthScale = 1;

    /**
     * Amount of points every curve generates.
     */
    @Getter private int curvePoints = 24;

    /**
     * Next time render gets called and the tvg is dirty, it recalculates all point
     * positions in paths.
     */
    @Getter private boolean dirty;

    public TinyVG(TinyVGHeader header, Color[] colorTable) {
        this.header = header;
        this.colorTable = colorTable;
    }

    /**
     * Draws the tvg to the screen based on the viewport (Used to calculate position
     * as tvg is y down instead of up.
     */
    public void draw(TinyVGShapeDrawer drawer, Viewport viewport) {
        drawer.setColor(Color.WHITE);
        drawer.beginShader();
        for (Command command : commands) {
            if (dirty)
                command.onPropertiesChanged();
            command.draw(drawer, viewport);
        }
        dirty = false;
        drawer.endShader();
    }

    public void addCommand(Command command) {
        this.commands.add(command);
    }

    /**
     * Sets the size of the TVG based on a specified width and height and triggers
     * an update to recalculate commands.
     */
    public void setSize(float width, float height) {
        scaleX = width / header.getWidth();
        scaleY = height / header.getHeight();

        dirty = true;
    }

    /**
     * Sets the scale of the TVG based on the specified float value and triggers an
     * update to recalculate commands.
     */
    public void setScale(float scale) {
        setScale(scale, scale);
    }

    /**
     * Sets the scale of the TVG based on the specified float value and triggers an
     * update to recalculate commands.
     */
    public void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;

        dirty = true;
    }

    /**
     * Sets the position of the TVG based on the specified x and y value and
     * triggers an update to recalculate commands.
     */
    public void setPosition(float x, float y) {
        this.positionX = x;
        this.positionY = y;

        dirty = true;
    }

    /**
     * Sets the x position of the TVG based on the specified value and triggers an
     * update to recalculate commands.
     */
    public void setX(float x) {
        positionX = x;

        dirty = true;
    }

    /**
     * Sets the y position of the TVG based on the specified value and triggers an
     * update to recalculate commands.
     */
    public void setY(float y) {
        positionY = y;

        dirty = true;
    }

    /**
     * Sets the amount of curve points used to generate curves and triggers an
     * update to recalculate commands.
     */
    public void setCurvePoints(int curvePoints) {
        this.curvePoints = curvePoints;

        dirty = true;
    }

    /**
     * @return Returns the actual width of the tvg including scale.
     */
    public float getWidth() {
        return header.getWidth() * scaleX;
    }

    /**
     * @return Returns the actual height of the tvg including scale.
     */
    public float getHeight() {
        return header.getHeight() * scaleY;
    }
}
