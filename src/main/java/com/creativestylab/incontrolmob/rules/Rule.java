package com.creativestylab.incontrolmob.rules;

import com.google.gson.annotations.SerializedName;

public class Rule {
    public String id;
    public int priority = 0;
    public boolean enabled = true;
    public boolean continueOnMatch = false; // If true, continue checking other rules

    @SerializedName("conditions")
    public RuleConditions conditions;

    @SerializedName("actions")
    public RuleActions actions;

    // Getters/Setters not strictly needed if fields are public for internal access,
    // but good practice. For now keeping public for brevity.
}
