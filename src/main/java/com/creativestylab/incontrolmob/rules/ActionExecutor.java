package com.creativestylab.incontrolmob.rules;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class ActionExecutor {

    private static final Random random = new Random();

    public static void execute(RuleActions actions, LivingEntity entity) {
        if (actions == null || entity == null)
            return;

        // Health
        if (actions.maxHealth != null) {
            var attr = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (attr != null)
                attr.setBaseValue(actions.maxHealth);
        }
        if (actions.health != null) {
            entity.setHealth(Math.min(actions.health, entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
        }

        // Attributes
        if (actions.setNoGravity != null)
            entity.setGravity(!actions.setNoGravity);
        if (actions.setSilent != null)
            entity.setSilent(actions.setSilent);
        if (actions.setInvulnerable != null)
            entity.setInvulnerable(actions.setInvulnerable);
        if (actions.setGlowing != null)
            entity.setGlowing(actions.setGlowing);
        if (actions.setAI != null)
            entity.setAI(actions.setAI);
        if (actions.setCustomName != null) {
            entity.setCustomName(actions.setCustomName);
            entity.setCustomNameVisible(true);
        }

        // Potions
        if (actions.addPotionEffects != null) {
            for (RuleActions.PotionEffectData data : actions.addPotionEffects) {
                PotionEffectType type = PotionEffectType.getByName(data.type);
                if (type != null) {
                    entity.addPotionEffect(new PotionEffect(type, data.duration, data.amplifier));
                }
            }
        }

        // Equipment
        if (actions.setEquipment != null) {
            EntityEquipment eq = entity.getEquipment();
            if (eq != null) {
                if (actions.setEquipment.mainHand != null)
                    eq.setItemInMainHand(createItem(actions.setEquipment.mainHand));
                if (actions.setEquipment.offHand != null)
                    eq.setItemInOffHand(createItem(actions.setEquipment.offHand));
                if (actions.setEquipment.helmet != null)
                    eq.setHelmet(createItem(actions.setEquipment.helmet));
                if (actions.setEquipment.chestplate != null)
                    eq.setChestplate(createItem(actions.setEquipment.chestplate));
                if (actions.setEquipment.leggings != null)
                    eq.setLeggings(createItem(actions.setEquipment.leggings));
                if (actions.setEquipment.boots != null)
                    eq.setBoots(createItem(actions.setEquipment.boots));

                // Drop chances
                if (actions.setEquipment.mainHandDropChance != null)
                    eq.setItemInMainHandDropChance(actions.setEquipment.mainHandDropChance);
                // ... others omitted for brevity but should be added
            }
        }
    }

    private static ItemStack createItem(RuleActions.ItemData data) {
        if (data.item == null)
            return null;
        if (random.nextDouble() > data.chance)
            return null; // Chance check

        Material mat = Material.matchMaterial(data.item);
        if (mat == null)
            return null;

        return new ItemStack(mat, data.count);
    }
}
