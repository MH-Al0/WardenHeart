package com.mh.wardenheart;

import org.bukkit.entity.Player;
import org.bukkit.entity.Warden;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import java.util.Arrays;
import java.util.Objects;

public class WardenDeathListener implements Listener {
    private final WardenHeartPlugin plugin;
    private final HeartRewardManager rewardManager;
    private final ConfigManager configManager;
    
    public WardenDeathListener(WardenHeartPlugin plugin, HeartRewardManager rewardManager, ConfigManager configManager) {
        this.plugin = Objects.requireNonNull(plugin);
        this.rewardManager = Objects.requireNonNull(rewardManager);
        this.configManager = Objects.requireNonNull(configManager);
    }
    
    @EventHandler
    public void onWardenDeath(EntityDeathEvent event) {
        // Check if entity is a Warden
        if (!(event.getEntity() instanceof Warden)) {
            return;
        }
        
        Warden warden = (Warden) event.getEntity();
        Player killer = warden.getKiller();
        
        if (killer == null) {
            return; // No player killer
        }
        
        // Track the kill
        rewardManager.onWardenKill(killer);
        
        // Give reward based on configuration
        String rewardType = configManager.getConfig().getString("reward-type", "HEART_ITEM");
        
        if (rewardType != null) {
            switch (rewardType.toUpperCase()) {
                case "PERMANENT_HEALTH" -> givePermanentHealth(killer);
                case "HEART_ITEM" -> giveHeartItem(killer);
                case "TEMPORARY_BUFF" -> giveTemporaryBuff(killer);
                default -> {} // Do nothing
            }
        }
        
        // Send message
        String message = configManager.getConfig().getString("reward-message", 
            "&a&lYou have been granted an extra heart for defeating the Warden!");
        if (message != null) {
            killer.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }
    
    private void givePermanentHealth(Player player) {
        AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);  // POUR 1.21.1
        if (maxHealth != null) {
            double currentMax = maxHealth.getBaseValue();
            double bonus = configManager.getConfig().getDouble("health-bonus", 2.0);
            maxHealth.setBaseValue(currentMax + bonus);
        }
    }
    
    private void giveHeartItem(Player player) {
        String materialName = configManager.getConfig().getString("heart-item-material", "RED_DYE");
        Material heartMaterial = materialName != null ? Material.matchMaterial(materialName) : null;
        
        if (heartMaterial == null) {
            heartMaterial = Material.RED_DYE;
        }
        
        ItemStack heartItem = new ItemStack(heartMaterial);
        ItemMeta meta = heartItem.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Warden's Heart");
            meta.setLore(Arrays.asList(
                ChatColor.GRAY + "Right-click to gain",
                ChatColor.GRAY + "+1 Max Heart"
            ));
            
            // Store plugin data for identification
            meta.getPersistentDataContainer().set(
                new NamespacedKey(plugin, "warden_heart"),
                org.bukkit.persistence.PersistentDataType.BOOLEAN,
                true
            );
            
            heartItem.setItemMeta(meta);
        }
        
        player.getInventory().addItem(heartItem);
    }
    
    private void giveTemporaryBuff(Player player) {
        int duration = configManager.getConfig().getInt("buff-duration", 300) * 20; // Convert to ticks
        
        // Apply absorption effect (golden hearts)
        player.addPotionEffect(new org.bukkit.potion.PotionEffect(
            org.bukkit.potion.PotionEffectType.ABSORPTION,
            duration,
            configManager.getConfig().getInt("buff-amplifier", 0),
            true,  // ambient
            true,  // particles
            true   // icon
        ));
    }
}