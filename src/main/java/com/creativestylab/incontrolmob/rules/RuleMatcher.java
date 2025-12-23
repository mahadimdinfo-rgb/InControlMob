package com.creativestylab.incontrolmob.rules;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import com.creativestylab.incontrolmob.InControlMob;
import org.bukkit.plugin.Plugin;
// import com.sk89q.worldguard... (Reflection or wrapper usage to avoid hard crash if missing)

import java.util.List;

public class RuleMatcher {

    public static boolean matches(RuleConditions conditions, Entity entity, Location loc,
            CreatureSpawnEvent.SpawnReason reason) {
        if (conditions == null)
            return true;

        // Entity Type
        if (entity != null) {
            String type = entity.getType().name();
            if (!conditions.matchesList(conditions.entityType, type))
                return false;
        }

        // Spawn Reason
        if (reason != null) {
            if (!conditions.matchesList(conditions.spawnReason, reason.name()))
                return false;
        }

        // Biome
        if (loc != null) {
            String biome = loc.getBlock().getBiome().name();
            if (!conditions.matchesList(conditions.biome, biome))
                return false;

            // World
            String world = loc.getWorld().getName();
            if (!conditions.matchesList(conditions.world, world))
                return false;

            // Light Level
            if (conditions.lightLevel != null) {
                int light = loc.getBlock().getLightLevel();
                if (!conditions.lightLevel.contains(light))
                    return false;
            }

            // Time
            if (conditions.timeOfDay != null) {
                long time = loc.getWorld().getTime();
                if (!conditions.timeOfDay.contains((int) time))
                    return false;
            }

            // Difficulty
            if (conditions.difficulty != null) {
                int diff = loc.getWorld().getDifficulty().ordinal(); // 0=PEACEFUL, 1=EASY...
                if (!conditions.difficulty.contains(diff))
                    return false;
            }
        }

        // Nearby Player
        if (conditions.nearbyPlayer > 0 && loc != null) {
            boolean playerFound = false;
            for (Player p : loc.getWorld().getPlayers()) {
                if (p.getLocation().distanceSquared(loc) <= (conditions.nearbyPlayer * conditions.nearbyPlayer)) {
                    playerFound = true;
                    break;
                }
            }
            if (!playerFound)
                return false;
        }

        // Region (Weak check placeholder till integration)
        if (conditions.region != null && loc != null) {
            if (!checkRegion(loc, conditions.region))
                return false;
        }

        return true;
    }

    private static boolean checkRegion(Location loc, String regionName) {
        // TODO: Implement WorldGuard check
        // For now, fail safe or ignore?
        // If user configured a region condition, they expect it to work or fail if WG
        // is missing.
        // Returning false (not matching) is safer if WG is missing but rule expects it.
        if (InControlMob.getInstance().getServer().getPluginManager().getPlugin("WorldGuard") == null) {
            return false;
        }
        // Access WorldGuard safely (maybe move to separate class)
        return WorldGuardIntegration.isInRegion(loc, regionName);
    }
}
