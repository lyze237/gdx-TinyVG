package dev.lyze.gdxtinyvg;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.commands.Command;
import lombok.Getter;

public class TinyVG {
    @Getter private final TinyVGHeader header;
    @Getter private final Color[] colorTable;

    @Getter private final Array<Command> commands = new Array<>();

    @Getter private final Vector2 position = new Vector2();
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

    public void setSize(float width, float height) {
        scale.set(width / header.getWidth(), height / header.getHeight());
    }

    public float getWidth() {
        return header.getWidth() * scale.x;
    }

    public float getHeight() {
        return header.getHeight() * scale.y;
    }
}
