package dev.lyze.gdxtinyvg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.enums.StyleType;
import lombok.Getter;
import lombok.Setter;
import lombok.var;
import space.earlygrey.shapedrawer.JoinType;
import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * An extension to the {@link ShapeDrawer} which supports drawing objects with a
 * gradient shader.
 */
public class GradientShapeDrawer extends ShapeDrawer implements Disposable {
    @Getter private final Color startColor = Color.BLACK.cpy(), endColor = Color.WHITE.cpy();
    @Getter @Setter private StyleType gradientStyle = StyleType.FLAT;
    @Getter private final Vector2 startPosition = new Vector2(), endPosition = new Vector2();

    private final ShaderProgram shader;

    public GradientShapeDrawer(Batch batch, TextureRegion region) {
        super(batch, region);

        ShaderProgram.pedantic = false; // todo remove
        shader = new ShaderProgram(Gdx.files.internal("shader/vertex.glsl").readString(),
                Gdx.files.internal("shader/fragment.glsl").readString());

        if (!shader.isCompiled())
            Gdx.app.error("Gradient Shape Drawer", shader.getLog());
    }

    /**
     * Sets the start position of the gradient.
     */
    public void setStartPosition(Vector2 start) {
        this.startPosition.set(start);
    }

    /**
     * Sets the end position of the gradient.
     */
    public void setEndPosition(Vector2 end) {
        this.endPosition.set(end);
    }

    public void setPositions(Vector2 start, Vector2 end) {
        setStartPosition(start);
        setEndPosition(end);
    }

    public void setPositions(Vector2 start, Vector2 end, Viewport viewport) {
        setStartPosition(start);
        setEndPosition(end);

        viewport.project(start);
        viewport.project(end);
    }

    public void setPositions(float startX, float startY, float endX, float endY, Viewport viewport) {
        setStartPosition(startX, startY);
        setEndPosition(endX, endY);

        viewport.project(startPosition);
        viewport.project(endPosition);
    }

    public void setStartPosition(float x, float y) {
        this.startPosition.set(x, y);
    }

    public void setEndPosition(float x, float y) {
        this.endPosition.set(x, y);
    }

    public void setPositions(float startX, float startY, float endX, float endY) {
        setStartPosition(startX, startY);
        setEndPosition(endX, endY);
    }

    public void beginShader() {
        getBatch().setShader(shader);
    }

    /**
     * Updates all uniforms in the shader. Call this method when you update
     * position, color or style.
     */
    public void applyShaderValues() {
        var width = endPosition.x - startPosition.x;
        var height = endPosition.y - startPosition.y;

        shader.setUniformf("u_startColor", startColor);
        shader.setUniformf("u_endColor", endColor);
        shader.setUniformf("u_startPosition", startPosition);
        shader.setUniformf("u_endPosition", endPosition);
        shader.setUniformf("u_resolution", width, height);
        shader.setUniformi("u_style", gradientStyle.getValue());
    }

    public void endShader() {
        getBatch().setShader(null);
    }

    /**
     * Sets the gradients colors.
     */
    public void setGradientColors(Color startColor, Color endColor) {
        this.startColor.set(startColor);
        this.endColor.set(endColor);
    }

    public void polygon(float[] vertices, float lineWidth) {
        polygon(vertices, lineWidth, isJoinNecessary(lineWidth) ? JoinType.POINTY : JoinType.NONE);
    }

    @Override
    public void dispose() {
        shader.dispose();
    }
}
