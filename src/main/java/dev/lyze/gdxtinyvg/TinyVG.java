package dev.lyze.gdxtinyvg;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.commands.Command;
import lombok.Getter;

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
    @Getter private final Vector2 position = new Vector2();
    /**
     * Global scale value.
     */
    @Getter private final Vector2 scale = new Vector2(1, 1);

    public TinyVG(TinyVGHeader header, Color[] colorTable) {
        this.header = header;
        this.colorTable = colorTable;
    }

    public void draw(GradientShapeDrawer drawer, Viewport viewport) {
        drawer.setColor(Color.WHITE);
        drawer.beginShader();
        for (Command command : commands) {
            if (command != null)
                command.draw(drawer, viewport);
        }
        drawer.endShader();
    }

    public void addCommand(Command command) {
        this.commands.add(command);
    }

    /**
     * Sets the scale of the TVG based on a specified width and height.
     */
    public void setSize(float width, float height) {
        scale.set(width / header.getWidth(), height / header.getHeight());
    }

    /**
     * @return Returns the actual width of the tvg including scale.
     */
    public float getWidth() {
        return header.getWidth() * scale.x;
    }

    /**
     * @return Returns the actual height of the tvg including scale.
     */
    public float getHeight() {
        return header.getHeight() * scale.y;
    }
}
