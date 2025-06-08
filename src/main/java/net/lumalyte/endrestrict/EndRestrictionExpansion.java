package net.lumalyte.endrestrict;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class EndRestrictionExpansion extends PlaceholderExpansion {
    private final EndRestrictionPlugin plugin;

    public EndRestrictionExpansion(EndRestrictionPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean register() {
        return super.register();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "endrestrict";
    }

    @Override
    public @NotNull String getAuthor() {
        return "lumalyte";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getName() + "-" + plugin.getPluginMeta().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params.equalsIgnoreCase("time")) {
            return plugin.getFormattedUnlockTime();
        }

        if (params.equalsIgnoreCase("status")) {
            return plugin.isEndLocked() ? "Locked" : "Unlocked";
        }

        if (params.equalsIgnoreCase("remaining")) {
            if (!plugin.isEndLocked()) {
                return "Now!";
            }
            Duration remaining = plugin.getTimeUntilUnlock();
            long days = remaining.toDays();
            long hours = remaining.toHoursPart();
            long minutes = remaining.toMinutesPart();
            long seconds = remaining.toSecondsPart();

            if (days > 0) {
                return String.format("%dd %02dh %02dm %02ds", days, hours, minutes, seconds);
            } else if (hours > 0) {
                return String.format("%dh %02dm %02ds", hours, minutes, seconds);
            } else if (minutes > 0) {
                return String.format("%dm %02ds", minutes, seconds);
            } else {
                return String.format("%ds", seconds);
            }
        }

        return null;
    }
} 