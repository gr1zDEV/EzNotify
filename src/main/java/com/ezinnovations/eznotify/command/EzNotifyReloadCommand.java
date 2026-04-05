package com.ezinnovations.eznotify.command;

import com.ezinnovations.eznotify.EzNotifyPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * /eznotifyreload
 */
public class EzNotifyReloadCommand implements CommandExecutor {

    private final EzNotifyPlugin plugin;

    public EzNotifyReloadCommand(EzNotifyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!CommandSupport.hasPermission(sender, "eznotify.reload")) {
            sender.sendMessage(plugin.getMessageManager().getMessage("no-permission"));
            return true;
        }

        plugin.ensureDefaultFiles();
        plugin.reloadPlugin();
        sender.sendMessage(plugin.getMessageManager().getMessage("reload-success"));
        return true;
    }
}
