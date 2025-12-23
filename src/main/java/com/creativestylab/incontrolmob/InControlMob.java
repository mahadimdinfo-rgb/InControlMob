package com.creativestylab.incontrolmob;

import com.creativestylab.incontrolmob.commands.MainCommand;
import com.creativestylab.incontrolmob.rules.RuleManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class InControlMob extends JavaPlugin {

    private static InControlMob instance;
    private RuleManager ruleManager;

    @Override
    public void onEnable() {
        instance = this;

        // Save default config
        saveDefaultConfig();

        // Initialize Rule Manager
        this.ruleManager = new RuleManager(this);
        this.ruleManager.loadRules();

        // Register commands
        if (getCommand("incontrolmob") != null) {
            getCommand("incontrolmob").setExecutor(new MainCommand(this));
        } else {
            getLogger().warning("Could not find command 'incontrolmob' in plugin.yml!");
        }

        // Register Listeners
        getServer().getPluginManager()
                .registerEvents(new com.creativestylab.incontrolmob.listeners.MobSpawnListener(this), this);
        getServer().getPluginManager().registerEvents(new com.creativestylab.incontrolmob.listeners.LootListener(this),
                this);

        getLogger().info("InControlMob 1.21 enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("InControlMob disabled.");
    }

    public static InControlMob getInstance() {
        return instance;
    }

    public RuleManager getRuleManager() {
        return ruleManager;
    }
}
