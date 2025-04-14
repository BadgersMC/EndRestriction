package org.atrimilan.paperplugintemplate.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {

    private ConfigManager() {}

    private static FileConfiguration config;

    public static void init(JavaPlugin plugin) {
        config = plugin.getConfig();
    }

    public static FileConfiguration getConfig() {
        return config;
    }
}
