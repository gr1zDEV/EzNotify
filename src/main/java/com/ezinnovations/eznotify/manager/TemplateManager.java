package com.ezinnovations.eznotify.manager;

import com.ezinnovations.eznotify.EzNotifyPlugin;
import com.ezinnovations.eznotify.config.PluginSettings;
import com.ezinnovations.eznotify.model.NotificationTemplate;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Loads all template files from the templates directory and keeps a registry.
 */
public class TemplateManager {

    private final EzNotifyPlugin plugin;
    private final PluginSettings settings;
    private final Map<String, NotificationTemplate> templateById = new HashMap<>();

    public TemplateManager(EzNotifyPlugin plugin, PluginSettings settings) {
        this.plugin = plugin;
        this.settings = settings;
    }

    public void reload() {
        templateById.clear();
        plugin.ensureDefaultFiles();

        File templateDirectory = new File(plugin.getDataFolder(), settings.templatesFolder());
        if (!templateDirectory.exists() && !templateDirectory.mkdirs()) {
            plugin.getLogger().warning("Could not create templates directory: " + templateDirectory.getAbsolutePath());
            return;
        }

        File[] files = templateDirectory.listFiles((dir, name) -> name.toLowerCase(Locale.ROOT).endsWith(".yml"));
        if (files == null) {
            plugin.getLogger().warning("Unable to read templates directory: " + templateDirectory.getAbsolutePath());
            return;
        }

        for (File file : files) {
            String templateId = file.getName().substring(0, file.getName().length() - 4);
            try {
                YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
                NotificationTemplate template = NotificationTemplate.fromSection(yaml, templateId);
                templateById.put(templateId.toLowerCase(Locale.ROOT), template);
                if (settings.debug()) {
                    plugin.getLogger().info("Loaded template: " + templateId);
                }
            } catch (Exception ex) {
                plugin.getLogger().warning("Failed to load template file '" + file.getName() + "': " + ex.getMessage());
            }
        }

        plugin.getLogger().info("Loaded " + templateById.size() + " notification templates.");
    }

    public Optional<NotificationTemplate> getTemplate(String id) {
        return Optional.ofNullable(templateById.get(id.toLowerCase(Locale.ROOT)));
    }

    public Map<String, NotificationTemplate> getAll() {
        return Collections.unmodifiableMap(templateById);
    }
}
