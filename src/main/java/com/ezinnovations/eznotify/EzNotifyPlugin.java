package com.ezinnovations.eznotify;

import com.ezinnovations.eznotify.command.EzNotifyCommand;
import com.ezinnovations.eznotify.command.EzNotifyReloadCommand;
import com.ezinnovations.eznotify.command.EzNotifyTemplateCommand;
import com.ezinnovations.eznotify.config.PluginSettings;
import com.ezinnovations.eznotify.manager.MessageManager;
import com.ezinnovations.eznotify.manager.TemplateManager;
import com.ezinnovations.eznotify.service.NotificationSender;
import com.ezinnovations.eznotify.util.PlaceholderUtil;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * Main plugin bootstrap class.
 */
public final class EzNotifyPlugin extends JavaPlugin {

    private PluginSettings settings;
    private MessageManager messageManager;
    private TemplateManager templateManager;
    private NotificationSender notificationSender;
    private PlaceholderUtil placeholderUtil;

    @Override
    public void onEnable() {
        ensureDefaultFiles();
        reloadPlugin();
        registerCommands();
        getLogger().info("EzNotify enabled.");
    }

    private void registerCommands() {
        PluginCommand notifyCommand = Objects.requireNonNull(getCommand("eznotify"), "eznotify command missing");
        notifyCommand.setExecutor(new EzNotifyCommand(this));

        PluginCommand templateCommand = Objects.requireNonNull(getCommand("eznotifytemplate"), "eznotifytemplate command missing");
        templateCommand.setExecutor(new EzNotifyTemplateCommand(this));

        PluginCommand reloadCommand = Objects.requireNonNull(getCommand("eznotifyreload"), "eznotifyreload command missing");
        reloadCommand.setExecutor(new EzNotifyReloadCommand(this));
    }

    /**
     * Reloads all runtime state from configuration and template files.
     */
    public void reloadPlugin() {
        reloadConfig();

        this.settings = PluginSettings.fromConfig(getConfig());
        this.placeholderUtil = new PlaceholderUtil(this, settings.placeholderApiEnabled());

        this.messageManager = new MessageManager(this);
        messageManager.reload();

        this.templateManager = new TemplateManager(this, settings);
        templateManager.reload();

        this.notificationSender = new NotificationSender(this, settings, placeholderUtil);
    }

    /**
     * Ensures default file/folder layout exists.
     */
    public void ensureDefaultFiles() {
        saveDefaultConfig();
        saveResourceIfMissing("messages.yml");

        String templatesFolderName = getConfig().getString("templates.folder", "templates");
        if (templatesFolderName == null || templatesFolderName.isBlank()) {
            templatesFolderName = "templates";
        }

        java.io.File templatesFolder = new java.io.File(getDataFolder(), templatesFolderName);
        if (!templatesFolder.exists() && !templatesFolder.mkdirs()) {
            getLogger().warning("Could not create templates folder: " + templatesFolder.getAbsolutePath());
        }

        saveResourceIfMissing(templatesFolderName + "/afk_gems.yml");
        saveResourceIfMissing(templatesFolderName + "/reward_claim.yml");
    }

    private void saveResourceIfMissing(String path) {
        java.io.File target = new java.io.File(getDataFolder(), path);
        if (!target.exists()) {
            saveResource(path, false);
        }
    }

    public PluginSettings getSettings() {
        return settings;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public TemplateManager getTemplateManager() {
        return templateManager;
    }

    public NotificationSender getNotificationSender() {
        return notificationSender;
    }

    public PlaceholderUtil getPlaceholderUtil() {
        return placeholderUtil;
    }
}
