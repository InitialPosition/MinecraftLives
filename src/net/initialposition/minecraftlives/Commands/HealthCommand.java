package net.initialposition.minecraftlives.Commands;

import net.initialposition.minecraftlives.Listeners.InteractionListener;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealthCommand implements CommandExecutor {

    private InteractionListener listener;

    public HealthCommand(InteractionListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // abort if command was not sent by player
        if (!(sender instanceof Player)) {
            return false;
        }

        // look up calling players health
        Player player = (Player) sender;

        int playerHealth = listener.getHealthForPlayer(player.getUniqueId());
        player.sendMessage(ChatColor.RED + "CURRENT LIVES: ♥x" + playerHealth);

        return true;
    }
}
