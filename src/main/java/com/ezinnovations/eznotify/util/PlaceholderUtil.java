package com.ezinnovations.eznotify.util;

import com.ezinnovations.eznotify.EzNotifyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;

/**
 * Optional PlaceholderAPI integration with reflection to avoid hard dependency.
 */
public class PlaceholderUtil {

    private final EzNotifyPlugin plugin;
    private final boolean enabledInConfig;
    private final boolean papiInstalled;

    public PlaceholderUtil(EzNotifyPlugin plugin, boolean enabledInConfig) {
        this.plugin = plugin;
        this.enabledInConfig = enabledInConfig;
        Plugin papi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        this.papiInstalled = papi != null && papi.isEnabled();
    }

    public String parse(Player target, String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        if (!enabledInConfig || !papiInstalled) {
            return input;
        }

        try {
            Class<?> papiClass = Class.forName("me.clip.placeholderapi.PlaceholderAPI");
            Method setPlaceholders = papiClass.getMethod("setPlaceholders", Player.class, String.class);
            return (String) setPlaceholders.invoke(null, target, input);
        } catch (Exception ex) {
            if (plugin.getSettings().debug()) {
                plugin.getLogger().warning("PlaceholderAPI parsing failed: " + ex.getMessage());
            }
            return input;
        }
    }
}
