package net.lumalyte.endrestrict;

import org.bukkit.plugin.java.JavaPlugin;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.Duration;

public class EndRestrictionPlugin extends JavaPlugin {
    private LocalDateTime unlockTime;
    private ZoneId configuredTimeZone;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfiguration();
        getServer().getPluginManager().registerEvents(new EndRestrictionListener(this), this);
        
        // Register PlaceholderAPI expansion if PAPI is present
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new EndRestrictionExpansion(this).register();
            getLogger().info("PlaceholderAPI expansion registered!");
        }
        
        getLogger().info("End Restriction Plugin enabled! End will unlock at: " + unlockTime + " " + configuredTimeZone);
    }

    private void loadConfiguration() {
        String unlockTimeStr = getConfig().getString("unlock_time");
        String timezone = getConfig().getString("timezone", "UTC");
        
        try {
            configuredTimeZone = ZoneId.of(timezone);
            unlockTime = LocalDateTime.parse(unlockTimeStr, TIME_FORMATTER);
        } catch (Exception e) {
            getLogger().severe("Error loading configuration! Using default values.");
            getLogger().severe(e.getMessage());
            // Default to 2 weeks from now
            unlockTime = LocalDateTime.now().plusWeeks(2);
            configuredTimeZone = ZoneId.of("UTC");
        }
    }

    public boolean isEndLocked() {
        LocalDateTime now = LocalDateTime.now(configuredTimeZone);
        return now.isBefore(unlockTime);
    }

    public String getFormattedUnlockTime() {
        return unlockTime.format(TIME_FORMATTER) + " " + configuredTimeZone;
    }

    public Duration getTimeUntilUnlock() {
        LocalDateTime now = LocalDateTime.now(configuredTimeZone);
        return Duration.between(now, unlockTime);
    }
} 