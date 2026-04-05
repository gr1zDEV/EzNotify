package com.ezinnovations.eznotify.command;

import com.ezinnovations.eznotify.EzNotifyPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Map;

/**
 * /eznotify <player> <message>
 */
public class EzNotifyCommand implements CommandExecutor {

    private final EzNotifyPlugin plugin;

    public EzNotifyCommand(EzNotifyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!CommandSupport.hasPermission(sender, "eznotify.use")) {
            sender.sendMessage(plugin.getMessageManager().getMessage("no-permission"));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(plugin.getMessageManager().getMessage("usage-eznotify"));
            return true;
        }

        String targetName = args[0];
        Player target = CommandSupport.findOnlinePlayer(plugin, sender, targetName);
        if (target == null) {
            return true;
        }

        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        plugin.getNotificationSender().sendOneOff(target, message);

        sender.sendMessage(plugin.getMessageManager().getMessage("notify-sent", Map.of(
                "%player%", target.getName(),
                "%message%", message
        )));
        return true;
    }
}
