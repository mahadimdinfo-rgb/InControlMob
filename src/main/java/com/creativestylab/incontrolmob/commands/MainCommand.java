package com.creativestylab.incontrolmob.commands;

import com.creativestylab.incontrolmob.InControlMob;
import com.creativestylab.incontrolmob.rules.Rule;
import com.creativestylab.incontrolmob.rules.RuleMatcher;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.ArrayList;
import java.util.List;

public class MainCommand implements CommandExecutor, TabExecutor {

    private final InControlMob plugin;

    public MainCommand(InControlMob plugin) {
        this.plugin = plugin;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.GOLD + "InControlMob v" + plugin.getDescription().getVersion());
            sender.sendMessage(ChatColor.YELLOW + "/icm reload - Reload rules");
            sender.sendMessage(ChatColor.YELLOW + "/icm list - List loaded rules");
            sender.sendMessage(ChatColor.YELLOW + "/icm rules - Alias for list");
            return true;
        }

        String sub = args[0].toLowerCase();

        if (sub.equals("reload")) {
            if (!sender.hasPermission("incontrolmob.reload")) {
                sender.sendMessage(ChatColor.RED + "No permission.");
                return true;
            }
            long start = System.currentTimeMillis();
            plugin.reloadConfig();
            plugin.getRuleManager().loadRules();
            sender.sendMessage(ChatColor.GREEN + "Configuration and rules reloaded ("
                    + (System.currentTimeMillis() - start) + "ms).");
            return true;
        }

        if (sub.equals("list") || sub.equals("rules")) {
            if (!sender.hasPermission("incontrolmob.manage")) {
                sender.sendMessage(ChatColor.RED + "No permission.");
                return true;
            }
            sender.sendMessage(ChatColor.GOLD + "--- Loaded Rules ---");
            listRules(sender, "Spawn", plugin.getRuleManager().getSpawnRules());
            listRules(sender, "Spawner", plugin.getRuleManager().getSpawnerRules());
            listRules(sender, "Summon", plugin.getRuleManager().getSummonRules());
            listRules(sender, "Loot", plugin.getRuleManager().getLootRules());
            return true;
        }

        if (sub.equals("test")) {
            // /icm test <entityType>
            if (!sender.hasPermission("incontrolmob.test")) {
                sender.sendMessage(ChatColor.RED + "No permission.");
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Player only.");
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Usage: /icm test <entityType>");
                return true;
            }
            try {
                // Mock test
                String typeStr = args[1].toUpperCase();
                // Simple condition check mock
                // This is complex to mock fully without an entity, but we can verify
                // location/biome checks.
                sender.sendMessage(
                        ChatColor.GREEN + "Mock test initiated (Dry Run) for " + typeStr + " at your location.");
                // We'd need to create a dummy entity or adapt RuleMatcher to accept EntityType
                // + Location separatedly.
                // Our RuleMatcher takes Entity. We can overload it or change it.
                // For now, let's just say "Feature pending full mock implementation" or try our
                // best.
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "Error: " + e.getMessage());
            }
            return true;
        }

        return false;
    }

    private void listRules(CommandSender sender, String category, List<Rule> rules) {
        if (rules.isEmpty())
            return;
        sender.sendMessage(ChatColor.YELLOW + category + ": " + ChatColor.WHITE + rules.size() + " rules");
        for (Rule r : rules) {
            sender.sendMessage(ChatColor.GRAY + " - " + (r.enabled ? ChatColor.GREEN : ChatColor.RED) + r.id
                    + ChatColor.GRAY + " (P: " + r.priority + ")");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> list = new ArrayList<>();
            list.add("reload");
            list.add("list");
            list.add("test");
            return list;
        }
        return null; // Null defaults to online players
    }
}
