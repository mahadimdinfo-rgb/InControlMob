package com.creativestylab.incontrolmob.rules;

public class RuleConditions {
    // Core checks
    // We use Object to allow "entityType": "ZOMBIE" or "entityType": ["ZOMBIE",
    // "SKELETON"]
    public Object entityType;
    public Object biome;
    public Object world;
    public Object spawnReason;

    public IntRange lightLevel;
    public IntRange timeOfDay;
    public IntRange difficulty; // 0-3 usually (Peaceful=0)

    public int nearbyPlayer; // Radius

    public String region; // WorldGuard region ID

    /**
     * Helper to check string list match
     */
    public boolean matchesList(Object conditionValue, String actualValue) {
        if (conditionValue == null)
            return true;
        if (conditionValue instanceof String) {
            return ((String) conditionValue).equalsIgnoreCase(actualValue);
        }
        if (conditionValue instanceof java.util.List) {
            for (Object o : (java.util.List<?>) conditionValue) {
                if (o.toString().equalsIgnoreCase(actualValue))
                    return true;
            }
        }
        return false;
    }
}
