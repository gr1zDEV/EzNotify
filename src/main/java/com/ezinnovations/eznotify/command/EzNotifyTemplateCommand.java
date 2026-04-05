package com.ezinnovations.eznotify.command;

import com.ezinnovations.eznotify.EzNotifyPlugin;
import com.ezinnovations.eznotify.model.NotificationTemplate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;

/**
 * /eznotifytemplate <template> <player>
 */
public class EzNotifyTemplateCommand implements CommandExecutor {

    private final EzNotifyPlugin plugin;

    public EzNotifyTemplateCommand(EzNotifyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!CommandSupport.hasPermission(sender, "eznotify.template")) {
            sender.sendMessage(plugin.getMessageManager().getMessage("no-permission"));
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage(plugin.getMessageManager().getMessage("usage-eznotifytemplate"));
            return true;
        }

        String templateId = args[0];
        String targetName = args[1];

        Optional<NotificationTemplate> template = plugin.getTemplateManager().getTemplate(templateId);
        if (template.isEmpty()) {
            sender.sendMessage(plugin.getMessageManager().getMessage("template-not-found", Map.of("%template%", templateId)));
            return true;
        }

        Player target = CommandSupport.findOnlinePlayer(plugin, sender, targetName);
        if (target == null) {
            return true;
        }

        plugin.getNotificationSender().sendTemplate(target, template.get());
        sender.sendMessage(plugin.getMessageManager().getMessage("template-sent", Map.of(
                "%template%", templateId,
                "%player%", target.getName()
        )));
        return true;
    }
}
