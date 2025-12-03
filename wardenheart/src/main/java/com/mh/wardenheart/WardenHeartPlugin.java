package com.mh.wardenheart;

import org.bukkit.plugin.java.JavaPlugin;

public class WardenHeartPlugin extends JavaPlugin {
    private HeartRewardManager rewardManager;
    private ConfigManager configManager;
    
    @Override
    public void onEnable() {
        // Save default config
        saveDefaultConfig();
        
        // Initialize managers
        configManager = new ConfigManager(this);
        rewardManager = new HeartRewardManager(this);
        
        // Register event listener
        getServer().getPluginManager().registerEvents(
            new WardenDeathListener(this, rewardManager, configManager), this);
        
        getLogger().info("Warden Heart Plugin enabled! ♥");
    }
    
    @Override
    public void onDisable() {
        getLogger().info("Warden Heart Plugin disabled!");
    }
    
    public HeartRewardManager getRewardManager() {
        return rewardManager;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
}