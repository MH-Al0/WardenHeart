package com.mh.wardenheart;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import java.io.File;
import java.io.IOException;

public class HeartRewardManager {
    private final WardenHeartPlugin plugin;
    private FileConfiguration playerData;
    private File playerDataFile;
    
    public HeartRewardManager(WardenHeartPlugin plugin) {
        this.plugin = plugin;
        // will setup data file if needed later
    }
    
    // Simple method to track kills 
    public void onWardenKill(Player player) {
        plugin.getLogger().info(player.getName() + " defeated a Warden!");
    }
}