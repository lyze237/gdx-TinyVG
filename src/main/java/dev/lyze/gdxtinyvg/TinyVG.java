package dev.lyze.gdxtinyvg;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
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
    @Getter @Setter private float positionX, positionY;

    /**
     * Global scale value.
     */
    @Getter @Setter private float scaleX = 1, scaleY = 1;

    /***
     * Global rotation value.
     */
    @Getter @Setter private float rotation;
    /***
     * Global origin value.
     */
    @Getter @Setter private float originX, originY;

    /***
     * Global shear value.
     */
    @Getter @Setter private float shearX, shearY;

    /**
     * Amount of points every curve generates.
     */
    @Getter private int curvePoints = 24;

    /**
     * Next time render gets called and the tvg is dirty, it recalculates all point
     * positions in paths.
     */
    @Getter private boolean dirty;

    private final Matrix4 backupBatchTransform = new Matrix4();
    private final Matrix4 computedTransform = new Matrix4();
    @Getter private final Affine2 affine = new Affine2();
    private final Affine2 emptyAffine = new Affine2();

    public TinyVG(TinyVGHeader header, Color[] colorTable) {
        this.header = header;
        this.colorTable = colorTable;
    }

    /**
     * Draws the tvg to the screen based on the viewport (Used to calculate position
     * as tvg is y down instead of up.
     */
    public void draw(TinyVGShapeDrawer drawer) {
        backupBatchTransform.set(drawer.getBatch().getTransformMatrix());

        affine.set(emptyAffine);
        affine.shear(shearX, shearY);
        affine.translate(positionX, positionY);
        affine.scale(scaleX, scaleY);
        affine.translate(originX, originY);
        affine.rotate(rotation);
        affine.translate(-originX, -originY);

        computedTransform.set(affine);
        drawer.getBatch().setTransformMatrix(computedTransform);

        drawer.beginShader();

        for (Command command : commands) {
            if (dirty)
                command.onPropertiesChanged();
            command.draw(drawer);
        }
        dirty = false;
        drawer.endShader();

        drawer.getBatch().setTransformMatrix(backupBatchTransform);
    }

    public void addCommand(Command command) {
        this.commands.add(command);
    }

    /**
     * Sets the size of the TVG based on a specified width and height.
     */
    public void setSize(float width, float height) {
        scaleX = width / header.getWidth();
        scaleY = height / header.getHeight();
    }

    /**
     * Sets the scale of the TVG based on the specified float value.
     */
    public void setScale(float scale) {
        setScale(scale, scale);
    }

    /**
     * Sets the scale of the TVG based on the specified float value.
     */
    public void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    /**
     * Sets the position of the TVG based on the specified x and y values.
     */
    public void setPosition(float x, float y) {
        this.positionX = x;
        this.positionY = y;
    }

    /**
     * Sets the origin of the TVG based on the specified x and y values.
     */
    public void setOrigin(float x, float y) {
        this.originX = x;
        this.originY = y;
    }

    /**
     * Sets the shear of the TVG based on the specified x and y values.
     */
    public void setShear(float x, float y) {
        this.shearX = x;
        this.shearY = y;
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
