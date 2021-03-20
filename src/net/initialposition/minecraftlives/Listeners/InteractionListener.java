package net.initialposition.minecraftlives.Listeners;

import net.initialposition.minecraftlives.ItemManager;
import net.initialposition.minecraftlives.util.ConfigKeys;
import net.initialposition.minecraftlives.util.ConsoleLog;
import net.initialposition.minecraftlives.util.LifeListEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class InteractionListener implements Listener {

    private final ConsoleLog logger;
    private final JavaPlugin plugin;

    private ArrayList<LifeListEntry> lifeList = new ArrayList<>();

    public int getHealthForPlayer(UUID id) {

        for (LifeListEntry entry : this.lifeList) {
            if (entry.getUUID().equals(id)) {
                return entry.getLives();
            }
        }

        return -1;
    }

    public InteractionListener(JavaPlugin plugin, ConsoleLog logger, @Nullable ArrayList<LifeListEntry> list) {
        this.plugin = plugin;
        this.logger = logger;

        if (list != null) {
            this.lifeList = list;
        }
    }

    @EventHandler
    public void hungerCheat(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // only go further if the player is holding a custom stew in main hand
        if (player.getInventory().getItemInMainHand().getType() == ItemManager.STEW_OF_LIFE.getType() &&
                !player.getInventory().getItemInMainHand().getEnchantments().isEmpty()) {

            Action action = event.getAction();
            // only check for right click interactions
            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {

                // if the player has full hunger, he can't eat. we cheat a smol hunger in here.
                if (player.getFoodLevel() == 20) {
                    player.setFoodLevel(19);
                }
            }
        }

        // if stew is in off hand, don't allow consumption
        if (player.getInventory().getItemInOffHand().getType() == ItemManager.STEW_OF_LIFE.getType() &&
                !player.getInventory().getItemInOffHand().getEnchantments().isEmpty()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void consumeExtraLifeSoup(PlayerItemConsumeEvent event) {
        // get the player instance
        Player player = event.getPlayer();

        // if the event was triggered by anything but stew, we handle it like normal food because it is
        if (!(player.getInventory().getItemInMainHand().getType() == ItemManager.STEW_OF_LIFE.getType() &&
                !player.getInventory().getItemInMainHand().getEnchantments().isEmpty())) {
            return;
        }

        // cancel event to handle here
        event.setCancelled(true);

        // give player extra life
        LifeListEntry playerData = null;
        for (LifeListEntry entry : this.lifeList) {
            if (entry.getUUID().equals(player.getUniqueId())) {

                // if player has max amount of lives, cancel
                int maxLives = this.plugin.getConfig().getInt(ConfigKeys.CONF_MAX_LIVES.name());
                if (entry.getLives() == maxLives) {
                    player.sendMessage(ChatColor.RED + "LIVES ALREADY MAXED OUT! ♥x" + entry.getLives());
                    return;
                }

                entry.setLives(entry.getLives() + 1);
                player.sendMessage(ChatColor.RED + "EXTRA LIFE! ♥x" + entry.getLives());
            }
        }

        // remove the bowl from the players inventory and give him an empty diamond bowl
        PlayerInventory inventory = player.getInventory();
        inventory.setItem(inventory.getHeldItemSlot(), ItemManager.DIAMOND_BOWL);

        // top off the player hunger wise
        player.setFoodLevel(20);
    }

    @EventHandler
    public void handleDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        logger.log(player.getDisplayName() + " died", ConsoleLog.LogLevel.DEBG);

        // remove a life
        LifeListEntry playerData = null;
        for (LifeListEntry entry : this.lifeList) {
            if (entry.getUUID().equals(player.getUniqueId())) {
                entry.setLives(entry.getLives() - 1);

                if (entry.getLives() == 0) {
                    // player is out of lives, ban and restock
                    entry.setLives(5);

                    // set ban time and reason
                    Calendar banTime = Calendar.getInstance();
                    banTime.add(Calendar.MINUTE, this.plugin.getConfig().getInt(ConfigKeys.CONF_ON_DEATH_BAN_TIME.name()));
                    String banReason = this.plugin.getConfig().getString(ConfigKeys.CONF_BAN_MESSAGE.name());

                    // execute the ban
                    BanList banList = this.plugin.getServer().getBanList(BanList.Type.NAME);
                    banList.addBan(player.getDisplayName(), ChatColor.RED + banReason, banTime.getTime(), null);

                    // kick the player to enforce ban
                    player.kickPlayer(ChatColor.RED + banReason);

                    // broadcast explanation to server
                    int banTimeInMinutes = this.plugin.getConfig().getInt(ConfigKeys.CONF_ON_DEATH_BAN_TIME.name());
                    int banTimeInHours = 0;
                    while (banTimeInMinutes > 60) {
                        banTimeInHours++;
                        banTimeInMinutes -= 60;
                    }
                    String banTimeStr = banTimeInHours > 0 ? banTimeInHours + "h " + banTimeInMinutes + "m" : banTimeInMinutes + "m";
                    Bukkit.getServer().broadcastMessage(ChatColor.RED + player.getDisplayName() + " DIED! ♥x0, Respawn Time: " + banTimeStr);
                } else {
                    Bukkit.getServer().broadcastMessage(ChatColor.RED + player.getDisplayName() + " DIED! ♥x" + entry.getLives());
                }
            }
        }
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        logger.log(player.getDisplayName() + " logged in", ConsoleLog.LogLevel.DEBG);

        // check if the player has data on record
        LifeListEntry playerData = null;
        for (LifeListEntry entry : this.lifeList) {
            if (entry.getUUID().equals(player.getUniqueId())) {
                // he does, show player lives
                logger.log("Player has life data, no further action needed", ConsoleLog.LogLevel.DEBG);
                player.sendMessage(ChatColor.RED + "WELCOME BACK! ♥x" + entry.getLives());
                return;
            }
        }

        // initialize new value if the player had no data
        logger.log("Player has no life data, initializing new data set", ConsoleLog.LogLevel.DEBG);

        int DEFAULT_LIVES = this.plugin.getConfig().getInt(ConfigKeys.CONF_INITIAL_LIVES.name());

        LifeListEntry newEntry = new LifeListEntry(player.getUniqueId(), DEFAULT_LIVES);
        this.lifeList.add(newEntry);

        player.sendMessage(ChatColor.RED + "WELCOME BACK! ♥x" + DEFAULT_LIVES);
    }

    public ArrayList<LifeListEntry> getLifeList() {
        return this.lifeList;
    }
}
