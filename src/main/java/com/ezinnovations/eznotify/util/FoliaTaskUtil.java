package com.ezinnovations.eznotify.util;

import com.ezinnovations.eznotify.EzNotifyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Utility for executing actions on the player's scheduler when possible, with Bukkit fallback.
 */
public final class FoliaTaskUtil {

    private FoliaTaskUtil() {
    }

    public static void runForPlayer(EzNotifyPlugin plugin, Player player, Runnable runnable) {
        try {
            player.getScheduler().run(plugin, task -> runnable.run(), null);
        } catch (Throwable ignored) {
            Bukkit.getScheduler().runTask(plugin, runnable);
        }
    }
}
