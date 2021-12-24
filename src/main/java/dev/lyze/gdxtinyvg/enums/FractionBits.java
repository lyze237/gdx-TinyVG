package dev.lyze.gdxtinyvg.enums;

public class FractionBits {
    public static float calculate(int value, int fractionBits) {
        return value / (float) Math.pow(2, fractionBits);
    }
}
