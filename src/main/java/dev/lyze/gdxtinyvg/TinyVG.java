package dev.lyze.gdxtinyvg;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import dev.lyze.gdxtinyvg.commands.Command;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;
import lombok.Getter;
import lombok.var;
import space.earlygrey.shapedrawer.ShapeDrawer;

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
    @Getter private float positionX, positionY;

    /**
     * Global scale value.
     */
    @Getter private float scaleX = 1, scaleY = 1;

    /***
     * Global rotation value.
     */
    @Getter private float rotation;
    /***
     * Global origin value.
     */
    @Getter private float originX, originY;

    /***
     * Global shear value.
     */
    @Getter private float shearX, shearY;

    /**
     * Amount of points every curve generates.
     */
    @Getter private int curvePoints = 24;

    /**
     * Next time render gets called and the tvg is dirty, it recalculates all point
     * positions in paths. (Slow)
     */
    @Getter private boolean dirtyCurves;
    /**
     * Next time render gets called and the tvg is dirty, it recalculates the
     * transformation matrix. (Fast)
     */
    @Getter private boolean dirtyTransformationMatrix;

    private final Matrix4 backupBatchTransform = new Matrix4();
    private final Matrix4 computedTransform = new Matrix4();
    @Getter private final Affine2 affine = new Affine2();

    public TinyVG(TinyVGHeader header, Color[] colorTable) {
        this.header = header;
        this.colorTable = colorTable;
    }

    /**
     * Draws the tvg to the screen.
     */
    public void draw(TinyVGShapeDrawer drawer) {
        backupBatchTransform.set(drawer.getBatch().getTransformMatrix());

        if (dirtyTransformationMatrix) {
            updateTransformationMatrix();
            dirtyTransformationMatrix = false;
        }

        drawer.getBatch().setTransformMatrix(computedTransform);

        drawer.beginShader();

        for (Command command : commands) {
            if (dirtyCurves)
                command.onPropertiesChanged();
            command.draw(drawer);
        }
        dirtyCurves = false;
        drawer.endShader();

        drawer.getBatch().setTransformMatrix(backupBatchTransform);
    }

    /**
     * Draws the bounding box of the shape drawer
     */
    public void drawBoundingBox(TinyVGShapeDrawer drawer, Color color) {
        backupBatchTransform.set(drawer.getBatch().getTransformMatrix());

        if (dirtyTransformationMatrix) {
            updateTransformationMatrix();
            dirtyTransformationMatrix = false;
        }

        drawer.getBatch().setTransformMatrix(computedTransform);

        drawer.rectangle(0, 0, getUnscaledWidth(), getUnscaledHeight(), color);

        drawer.getBatch().setTransformMatrix(backupBatchTransform);
    }

    /**
     * Checks if the given point is inside the bounding box.
     */
    public boolean isInBoundingBox(Vector2 point) {
        if (dirtyTransformationMatrix) {
            updateTransformationMatrix();
            dirtyTransformationMatrix = false;
        }

        var pointX = point.x;
        var pointY = point.y;

        point.set(0, 0);
        affine.applyTo(point);

        var topLeftX = point.x;
        var topLeftY = point.y;

        point.set(getUnscaledWidth(), 0);
        affine.applyTo(point);

        var topRightX = point.x;
        var topRightY = point.y;

        point.set(0, getUnscaledHeight());
        affine.applyTo(point);

        var bottomLeftX = point.x;
        var bottomLeftY = point.y;

        point.set(getUnscaledWidth(), getUnscaledHeight());
        affine.applyTo(point);

        var bottomRightX = point.x;
        var bottomRightY = point.y;

        point.set(pointX, pointY);

        return Intersector.isPointInTriangle(pointX, pointY, topLeftX, topLeftY, topRightX, topRightY, bottomRightX,
                bottomRightY)
                || Intersector.isPointInTriangle(pointX, pointY, topLeftX, topLeftY, bottomLeftX, bottomLeftY,
                        bottomRightX, bottomRightY);
    }

    private void updateTransformationMatrix() {
        affine.idt();
        affine.shear(shearX, shearY);
        affine.translate(positionX, positionY);
        affine.translate(originX, originY);
        affine.scale(scaleX, scaleY);
        affine.rotate(rotation);
        affine.translate(-originX, -originY);

        computedTransform.set(affine);
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

        dirtyTransformationMatrix = true;
    }

    /**
     * Sets the scale of the TVG based on the specified float value.
     */
    public void setScale(float scale) {
        setScale(scale, scale);

        dirtyTransformationMatrix = true;
    }

    /**
     * Sets the scale of the TVG based on the specified float value.
     */
    public void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;

        dirtyTransformationMatrix = true;
    }

    /**
     * Sets the position of the TVG based on the specified x and y values.
     */
    public void setPosition(float x, float y) {
        this.positionX = x;
        this.positionY = y;

        dirtyTransformationMatrix = true;
    }

    /**
     * Sets the position where the TVG will be drawn, relative to tits current
     * origin.
     */
    public void setOriginBasedPosition(float x, float y) {
        setPosition(x - this.originX, y - this.originY);
    }

    /**
     * Sets the origin of the TVG based on the specified x and y values.
     */
    public void setOrigin(float x, float y) {
        this.originX = x;
        this.originY = y;

        dirtyTransformationMatrix = true;
    }

    /**
     * Sets the shear of the TVG based on the specified x and y values.
     */
    public void setShear(float x, float y) {
        this.shearX = x;
        this.shearY = y;

        dirtyTransformationMatrix = true;
    }

    /**
     * Sets the amount of curve points used to generate curves and triggers an
     * update to recalculate commands.
     */
    public void setCurvePoints(int curvePoints) {
        this.curvePoints = curvePoints;

        dirtyCurves = true;
        dirtyTransformationMatrix = true;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation % 360;

        dirtyTransformationMatrix = true;
    }

    public void setShearY(float shearY) {
        this.shearY = shearY;

        dirtyTransformationMatrix = true;
    }

    public void setShearX(float shearX) {
        this.shearX = shearX;

        dirtyTransformationMatrix = true;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;

        dirtyTransformationMatrix = true;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;

        dirtyTransformationMatrix = true;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;

        dirtyTransformationMatrix = true;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;

        dirtyTransformationMatrix = true;
    }

    public void setOriginX(float originX) {
        this.originX = originX;

        dirtyTransformationMatrix = true;
    }

    public void setOriginY(float originY) {
        this.originY = originY;

        dirtyTransformationMatrix = true;
    }

    /**
     * @return Returns the unscaled width of the tvg.
     */
    public float getUnscaledWidth() {
        return header.getWidth();
    }

    /**
     * @return Returns the unscaled width of the tvg.
     */
    public float getUnscaledHeight() {
        return header.getHeight();
    }

    /**
     * @return Returns the scaled width of the tvg.
     */
    public float getScaledWidth() {
        return header.getWidth() * scaleX;
    }

    /**
     * @return Returns the scaled height of the tvg.
     */
    public float getScaledHeight() {
        return header.getHeight() * scaleY;
    }

    public void centerOrigin() {
        setOrigin(getUnscaledWidth() / 2f, getUnscaledHeight() / 2f);
    }
}
