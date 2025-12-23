package com.creativestylab.incontrolmob.rules;

import com.creativestylab.incontrolmob.InControlMob;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.logging.Level;

public class RuleManager {

    private final InControlMob plugin;
    private final Gson gson;

    // Categorized rules
    private final List<Rule> spawnRules = new ArrayList<>();
    private final List<Rule> spawnerRules = new ArrayList<>();
    private final List<Rule> summonRules = new ArrayList<>();
    private final List<Rule> lootRules = new ArrayList<>();
    private final List<Rule> experienceRules = new ArrayList<>();

    public RuleManager(InControlMob plugin) {
        this.plugin = plugin;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void loadRules() {
        spawnRules.clear();
        spawnerRules.clear();
        summonRules.clear();
        lootRules.clear();
        experienceRules.clear();

        File rulesDir = new File(plugin.getDataFolder(), ""); // config/incontrolmob/ maps to plugin data folder root if
                                                              // configured?
        // User requested: config/incontrolmob/spawn.json
        // Usually plugin data folder IS plugins/InControlMob/ (which acts as config
        // root)

        if (!rulesDir.exists()) {
            rulesDir.mkdirs();
        }

        spawnRules.addAll(loadParams("spawn.json"));
        spawnerRules.addAll(loadParams("spawner.json"));
        summonRules.addAll(loadParams("summon.json"));
        lootRules.addAll(loadParams("loot.json"));
        experienceRules.addAll(loadParams("experience.json"));

        // Sort by priority (Higher first)
        Comparator<Rule> comparator = (r1, r2) -> Integer.compare(r2.priority, r1.priority);
        spawnRules.sort(comparator);
        spawnerRules.sort(comparator);
        summonRules.sort(comparator);
        lootRules.sort(comparator);
        experienceRules.sort(comparator);

        plugin.getLogger().info("Loaded rules: " +
                "Spawn: " + spawnRules.size() + ", " +
                "Spawner: " + spawnerRules.size() + ", " +
                "Summon: " + summonRules.size() + ", " +
                "Loot: " + lootRules.size());
    }

    private List<Rule> loadParams(String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            plugin.saveResource(fileName, false); // Save default if missing
        }

        try (FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<Rule>>() {
            }.getType();
            List<Rule> rules = gson.fromJson(reader, listType);
            if (rules == null)
                return new ArrayList<>();
            return rules;
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load rules from " + fileName, e);
            return new ArrayList<>();
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Error parsing JSON in " + fileName, e);
            return new ArrayList<>();
        }
    }

    public List<Rule> getSpawnRules() {
        return spawnRules;
    }

    public List<Rule> getSpawnerRules() {
        return spawnerRules;
    }

    public List<Rule> getSummonRules() {
        return summonRules;
    }

    public List<Rule> getLootRules() {
        return lootRules;
    }

    public List<Rule> getExperienceRules() {
        return experienceRules;
    }
}
