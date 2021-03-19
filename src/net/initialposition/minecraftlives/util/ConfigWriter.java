package net.initialposition.minecraftlives.util;

import org.bukkit.plugin.java.JavaPlugin;

public class ConfigWriter {

    public static void initializeNewConfig(JavaPlugin plugin) {

        // ADD DEFAULTS HERE
        plugin.getConfig().set(ConfigKeys.CONF_INITIAL_LIVES.name(), 10);
        plugin.getConfig().set(ConfigKeys.CONF_MAX_LIVES.name(), 20);
        plugin.getConfig().set(ConfigKeys.CONF_BAN_MESSAGE.name(), "YOU DIED! Respawning...");
        plugin.getConfig().set(ConfigKeys.CONF_ON_DEATH_BAN_TIME.name(), 60);
        plugin.getConfig().set(ConfigKeys.PLAYER_DATA.name(), "");

        plugin.saveConfig();
    }

    public static void saveConfig(JavaPlugin plugin) {
        plugin.saveConfig();
    }
}
