package net.initialposition.minecraftlives;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.initialposition.minecraftlives.Commands.HealthCommand;
import net.initialposition.minecraftlives.Listeners.InteractionListener;
import net.initialposition.minecraftlives.util.ConfigKeys;
import net.initialposition.minecraftlives.util.ConfigWriter;
import net.initialposition.minecraftlives.util.ConsoleLog;
import net.initialposition.minecraftlives.util.LifeListEntry;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class MinecraftLives extends JavaPlugin {

    private ConsoleLog logger;
    private final Gson gsonConverter = new Gson();

    private InteractionListener interactionListener;

    @Override
    public void onEnable() {

        // generate logger
        logger = new ConsoleLog(ConsoleLog.LogLevel.INFO);
        logger.log("Starting MinecraftLives...", ConsoleLog.LogLevel.INFO);

        // initialize loaded life list
        ArrayList<LifeListEntry> loadedList = null;

        // check if we have saved data
        if (!this.getConfig().contains(ConfigKeys.CONF_INITIAL_LIVES.name())) {
            logger.log("No config found, initializing new one", ConsoleLog.LogLevel.DEBG);
            ConfigWriter.initializeNewConfig(this);
        } else {
            logger.log("Config found, loading data", ConsoleLog.LogLevel.DEBG);

            String loadedJson = this.getConfig().getString(ConfigKeys.PLAYER_DATA.name());
            loadedList = gsonConverter.fromJson(loadedJson, new TypeToken<ArrayList<LifeListEntry>>() {
            }.getType());
        }

        // initialize life regeneration recipes
        ItemManager.init_items(this);
        logger.log("Recipes added to server.", ConsoleLog.LogLevel.DEBG);

        // register listener
        interactionListener = new InteractionListener(this, logger, loadedList);
        InteractionListener listener = interactionListener;
        getServer().getPluginManager().registerEvents(listener, this);
        logger.log("Listener registered.", ConsoleLog.LogLevel.VERB);

        // register health command
        HealthCommand healthCommand = new HealthCommand(interactionListener);
        this.getCommand("health").setExecutor(healthCommand);
    }

    @Override
    public void onDisable() {
        logger.log("Shutting down MinecraftLives...", ConsoleLog.LogLevel.INFO);

        // unregister listener
        HandlerList.unregisterAll(this);
        logger.log("Listener unregistered.", ConsoleLog.LogLevel.VERB);

        // save life list
        ArrayList<LifeListEntry> currentList = interactionListener.getLifeList();
        String jsonList = gsonConverter.toJson(currentList);

        this.getConfig().set(ConfigKeys.PLAYER_DATA.name(), jsonList);
        ConfigWriter.saveConfig(this);

        logger.log("Life list saved.", ConsoleLog.LogLevel.VERB);
    }
}
