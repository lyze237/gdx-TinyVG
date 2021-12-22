package dev.lyze.gdxtinyvg.enums;

public class Scale {
    public static float calculate(int value, int scale) {
        return value / (float) Math.pow(2, scale);
    }
}
