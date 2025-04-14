package org.atrimilan.paperplugintemplate;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.atrimilan.paperplugintemplate.commands.FlySpeedCommand;
import org.atrimilan.paperplugintemplate.commands.ReadConfigCommand;
import org.atrimilan.paperplugintemplate.eventlisteners.PlayerActionsListener;
import org.atrimilan.paperplugintemplate.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class PaperPluginTemplate extends JavaPlugin {

    @Override
    public void onEnable() {
        // Create config.yml with default values, or update missing fields based on resources/config.yml
        getConfig().options().copyDefaults(true);
        saveConfig();

        ConfigManager.init(this);

        this.registerPluginCommands();
        this.registerPluginEvents();
    }

    private void registerPluginCommands() {
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(FlySpeedCommand.createCommand(), FlySpeedCommand.ALIASES);
            commands.registrar().register(ReadConfigCommand.createCommand(), ReadConfigCommand.ALIASES);
        });
    }

    private void registerPluginEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerActionsListener(), this);
    }
}
