package com.micle.totemofreviving.utils;

public class Utils {
    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    public static int randomIntRange(int min, int max) {
        return (int) randomDoubleRange(min, max);
    }

    public static float randomFloatRange(float min, float max) {
        return (float) randomDoubleRange(min, max);
    }

    public static double randomDoubleRange(double min, double max) {
        return ((Math.random() * (max - min)) + min);
    }
}
