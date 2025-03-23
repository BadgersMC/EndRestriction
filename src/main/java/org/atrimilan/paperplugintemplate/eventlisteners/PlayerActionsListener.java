package org.atrimilan.paperplugintemplate.eventlisteners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerActionsListener implements Listener {

    /**
     * Send a welcome message to the player joining the server
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage(
                Component.text("Welcome back, " + event.getPlayer().getName() + "!").color(NamedTextColor.GOLD));
    }
}
