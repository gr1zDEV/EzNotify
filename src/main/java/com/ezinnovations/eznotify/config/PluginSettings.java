package com.ezinnovations.eznotify.config;

import com.ezinnovations.eznotify.model.NotificationTemplate;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Typed representation of config.yml to avoid scattered raw config reads.
 */
public record PluginSettings(
        boolean placeholderApiEnabled,
        boolean parseLegacyColors,
        boolean parseHexColors,
        String templatesFolder,
        boolean debug,
        NotificationTemplate oneOffDefaults
) {

    public static PluginSettings fromConfig(FileConfiguration config) {
        boolean placeholderApiEnabled = config.getBoolean("hooks.placeholderapi-enabled", true);
        boolean parseLegacy = config.getBoolean("colors.parse-legacy-ampersand", true);
        boolean parseHex = config.getBoolean("colors.parse-hex", true);
        String templatesFolder = config.getString("templates.folder", "templates");
        boolean debug = config.getBoolean("debug", false);

        NotificationTemplate defaults = NotificationTemplate.fromSection(config.getConfigurationSection("one-off-defaults"), "one-off-defaults");

        return new PluginSettings(
                placeholderApiEnabled,
                parseLegacy,
                parseHex,
                templatesFolder == null || templatesFolder.isBlank() ? "templates" : templatesFolder,
                debug,
                defaults
        );
    }
}
