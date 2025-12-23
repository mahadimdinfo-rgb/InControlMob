package com.creativestylab.incontrolmob.listeners;

import com.creativestylab.incontrolmob.InControlMob;
import com.creativestylab.incontrolmob.rules.Rule;
import com.creativestylab.incontrolmob.rules.RuleActions;
import com.creativestylab.incontrolmob.rules.RuleMatcher;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class LootListener implements Listener {

    private final InControlMob plugin;
    private final Random random = new Random();

    public LootListener(InControlMob plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

        // LOOT RULES
        for (Rule rule : plugin.getRuleManager().getLootRules()) {
            if (!rule.enabled)
                continue;
            if (RuleMatcher.matches(rule.conditions, entity, entity.getLocation(), null)) { // Reason null for death
                applyLootActions(rule.actions, event);
                if (!rule.continueOnMatch)
                    break;
            }
        }

        // XP RULES
        for (Rule rule : plugin.getRuleManager().getExperienceRules()) {
            if (!rule.enabled)
                continue;
            if (RuleMatcher.matches(rule.conditions, entity, entity.getLocation(), null)) {
                if (rule.actions != null && rule.actions.setExperience != null) {
                    event.setDroppedExp(rule.actions.setExperience);
                }
                if (!rule.continueOnMatch)
                    break;
            }
        }
    }

    private void applyLootActions(RuleActions actions, EntityDeathEvent event) {
        if (actions == null)
            return;

        if (Boolean.TRUE.equals(actions.clearDrops)) {
            event.getDrops().clear();
        }

        if (actions.addDrops != null) {
            for (RuleActions.ItemData item : actions.addDrops) {
                if (random.nextDouble() <= item.chance) {
                    Material mat = Material.matchMaterial(item.item);
                    if (mat != null) {
                        event.getDrops().add(new ItemStack(mat, item.count));
                    }
                }
            }
        }

        // Simple replace drops support (clear + add)
        if (actions.replaceDrops != null && !actions.replaceDrops.isEmpty()) {
            event.getDrops().clear();
            for (RuleActions.ItemData item : actions.replaceDrops) {
                if (random.nextDouble() <= item.chance) {
                    Material mat = Material.matchMaterial(item.item);
                    if (mat != null) {
                        event.getDrops().add(new ItemStack(mat, item.count));
                    }
                }
            }
        }
    }
}
