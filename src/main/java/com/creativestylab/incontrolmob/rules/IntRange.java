package com.creativestylab.incontrolmob.rules;

public class IntRange {
    private int min = Integer.MIN_VALUE;
    private int max = Integer.MAX_VALUE;
    private int from = Integer.MIN_VALUE; // Alias for min
    private int to = Integer.MAX_VALUE; // Alias for max

    /**
     * Checks if value is within range (inclusive).
     */
    public boolean contains(int value) {
        int actualMin = (from != Integer.MIN_VALUE) ? from : min;
        int actualMax = (to != Integer.MAX_VALUE) ? to : max;
        return value >= actualMin && value <= actualMax;
    }

    // Getters/Setters if needed
}
