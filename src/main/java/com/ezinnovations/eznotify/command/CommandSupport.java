package com.ezinnovations.eznotify.command;

import com.ezinnovations.eznotify.EzNotifyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Shared command helper methods.
 */
public final class CommandSupport {

    private CommandSupport() {
    }

    public static boolean hasPermission(CommandSender sender, String permission) {
        return !(sender instanceof Player player) || player.hasPermission(permission);
    }

    public static Player findOnlinePlayer(EzNotifyPlugin plugin, CommandSender sender, String playerName) {
        Player player = Bukkit.getPlayerExact(playerName);
        if (player == null) {
            sender.sendMessage(plugin.getMessageManager().getMessage("player-not-found", Map.of("%player%", playerName)));
            return null;
        }
        if (!player.isOnline()) {
            sender.sendMessage(plugin.getMessageManager().getMessage("player-offline", Map.of("%player%", playerName)));
            return null;
        }
        return player;
    }
}
