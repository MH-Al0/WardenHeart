package com.mh.wardenheart;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private final WardenHeartPlugin plugin;
    
    public ConfigManager(WardenHeartPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
    }
    
    public void reloadConfig() {
        plugin.reloadConfig();
    }
    
    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }
}