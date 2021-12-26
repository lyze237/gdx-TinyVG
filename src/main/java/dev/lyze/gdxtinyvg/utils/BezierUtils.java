package dev.lyze.gdxtinyvg.utils;

import com.badlogic.gdx.math.Vector;

public class BezierUtils {
    /**
     * Simple linear interpolation
     * 
     * @param out The {@link Vector} to set to the result.
     * @param t   The location (ranging 0..1) on the line.
     * @param p0  The start point.
     * @param p1  The end point.
     * @param tmp A temporary vector to be used by the calculation.
     * @return The value specified by out for chaining
     */
    public static <T extends Vector<T>> T linear(final T out, final float t, final T p0, final T p1, final T tmp) {
        // B1(t) = p0 + (p1-p0)*t
        return out.set(p0).scl(1f - t).add(tmp.set(p1).scl(t)); // Could just use lerp...
    }

    /**
     * Simple linear interpolation derivative
     * 
     * @param out The {@link Vector} to set to the result.
     * @param t   The location (ranging 0..1) on the line.
     * @param p0  The start point.
     * @param p1  The end point.
     * @param tmp A temporary vector to be used by the calculation.
     * @return The value specified by out for chaining
     */
    public static <T extends Vector<T>> T linear_derivative(final T out, final float t, final T p0, final T p1,
            final T tmp) {
        // B1'(t) = p1-p0
        return out.set(p1).sub(p0);
    }

    /**
     * Quadratic Bezier curve
     * 
     * @param out The {@link Vector} to set to the result.
     * @param t   The location (ranging 0..1) on the curve.
     * @param p0  The first bezier point.
     * @param p1  The second bezier point.
     * @param p2  The third bezier point.
     * @param tmp A temporary vector to be used by the calculation.
     * @return The value specified by out for chaining
     */
    public static <T extends Vector<T>> T quadratic(final T out, final float t, final T p0, final T p1, final T p2,
            final T tmp) {
        // B2(t) = (1 - t) * (1 - t) * p0 + 2 * (1-t) * t * p1 + t*t*p2
        final float dt = 1f - t;
        return out.set(p0).scl(dt * dt).add(tmp.set(p1).scl(2 * dt * t)).add(tmp.set(p2).scl(t * t));
    }

    /**
     * Quadratic Bezier curve derivative
     * 
     * @param out The {@link Vector} to set to the result.
     * @param t   The location (ranging 0..1) on the curve.
     * @param p0  The first bezier point.
     * @param p1  The second bezier point.
     * @param p2  The third bezier point.
     * @param tmp A temporary vector to be used by the calculation.
     * @return The value specified by out for chaining
     */
    public static <T extends Vector<T>> T quadratic_derivative(final T out, final float t, final T p0, final T p1,
            final T p2, final T tmp) {
        // B2'(t) = 2 * (1 - t) * (p1 - p0) + 2 * t * (p2 - p1)
        final float dt = 1f - t;
        return out.set(p1).sub(p0).scl(2).scl(1 - t).add(tmp.set(p2).sub(p1).scl(t).scl(2));
    }

    /**
     * Cubic Bezier curve
     * 
     * @param out The {@link Vector} to set to the result.
     * @param t   The location (ranging 0..1) on the curve.
     * @param p0  The first bezier point.
     * @param p1  The second bezier point.
     * @param p2  The third bezier point.
     * @param p3  The fourth bezier point.
     * @param tmp A temporary vector to be used by the calculation.
     * @return The value specified by out for chaining
     */
    public static <T extends Vector<T>> T cubic(final T out, final float t, final T p0, final T p1, final T p2,
            final T p3, final T tmp) {
        // B3(t) = (1-t) * (1-t) * (1-t) * p0 + 3 * (1-t) * (1-t) * t * p1 + 3 * (1-t) *
        // t * t * p2 + t * t * t * p3
        final float dt = 1f - t;
        final float dt2 = dt * dt;
        final float t2 = t * t;
        return out.set(p0).scl(dt2 * dt).add(tmp.set(p1).scl(3 * dt2 * t)).add(tmp.set(p2).scl(3 * dt * t2))
                .add(tmp.set(p3).scl(t2 * t));
    }

    /**
     * Cubic Bezier curve derivative
     * 
     * @param out The {@link Vector} to set to the result.
     * @param t   The location (ranging 0..1) on the curve.
     * @param p0  The first bezier point.
     * @param p1  The second bezier point.
     * @param p2  The third bezier point.
     * @param p3  The fourth bezier point.
     * @param tmp A temporary vector to be used by the calculation.
     * @return The value specified by out for chaining
     */
    public static <T extends Vector<T>> T cubic_derivative(final T out, final float t, final T p0, final T p1,
            final T p2, final T p3, final T tmp) {
        // B3'(t) = 3 * (1-t) * (1-t) * (p1 - p0) + 6 * (1 - t) * t * (p2 - p1) + 3 * t
        // * t * (p3 - p2)
        final float dt = 1f - t;
        final float dt2 = dt * dt;
        final float t2 = t * t;
        return out.set(p1).sub(p0).scl(dt2 * 3).add(tmp.set(p2).sub(p1).scl(dt * t * 6))
                .add(tmp.set(p3).sub(p2).scl(t2 * 3));
    }
}
