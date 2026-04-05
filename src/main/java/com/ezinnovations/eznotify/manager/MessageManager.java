package com.ezinnovations.eznotify.manager;

import com.ezinnovations.eznotify.EzNotifyPlugin;
import com.ezinnovations.eznotify.util.ColorUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Map;

/**
 * Handles messages.yml reads and formatting.
 */
public class MessageManager {

    private final EzNotifyPlugin plugin;
    private FileConfiguration messages;

    public MessageManager(EzNotifyPlugin plugin) {
        this.plugin = plugin;
    }

    public void reload() {
        File file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        this.messages = YamlConfiguration.loadConfiguration(file);
    }

    public String getMessage(String key, Map<String, String> replacements) {
        String raw = messages.getString(key, "&cMissing message key: " + key);
        if (raw == null) {
            raw = "&cMissing message key: " + key;
        }

        String prefix = messages.getString("prefix", "");
        raw = raw.replace("%prefix%", prefix == null ? "" : prefix);

        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            raw = raw.replace(entry.getKey(), entry.getValue());
        }

        return ColorUtil.colorize(raw, plugin.getSettings());
    }

    public String getMessage(String key) {
        return getMessage(key, Map.of());
    }
}
