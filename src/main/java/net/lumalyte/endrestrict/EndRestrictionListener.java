package net.lumalyte.endrestrict;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class EndRestrictionListener implements Listener {
    private final EndRestrictionPlugin plugin;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public EndRestrictionListener(EndRestrictionPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        
        // Check if the player is using an Eye of Ender
        if (event.getItem().getType() != Material.ENDER_EYE) return;

        // If the end is locked, prevent both throwing and placing
        if (plugin.isEndLocked()) {
            event.setCancelled(true);
            String message = plugin.getConfig().getString("messages.end_locked", "<red>The End is currently locked!</red>");
            message = message.replace("%unlock_time%", plugin.getFormattedUnlockTime());
            event.getPlayer().sendMessage(mm.deserialize(message));
            return;
        }

        // If the end is unlocked, allow normal behavior
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            // Check if they're trying to place it in an End Portal Frame
            if (event.getClickedBlock() != null && 
                event.getClickedBlock().getType() == Material.END_PORTAL_FRAME) {
                // End is unlocked, so we don't need to do anything special
                return;
            }
        }
    }
} 