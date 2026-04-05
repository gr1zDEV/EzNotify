package com.ezinnovations.eznotify.service;

import com.ezinnovations.eznotify.EzNotifyPlugin;
import com.ezinnovations.eznotify.config.PluginSettings;
import com.ezinnovations.eznotify.model.NotificationTemplate;
import com.ezinnovations.eznotify.util.ColorUtil;
import com.ezinnovations.eznotify.util.FoliaTaskUtil;
import com.ezinnovations.eznotify.util.PlaceholderUtil;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Player;

/**
 * Responsible for delivering notification sections to players.
 */
public class NotificationSender {

    private final EzNotifyPlugin plugin;
    private final PluginSettings settings;
    private final PlaceholderUtil placeholderUtil;

    public NotificationSender(EzNotifyPlugin plugin, PluginSettings settings, PlaceholderUtil placeholderUtil) {
        this.plugin = plugin;
        this.settings = settings;
        this.placeholderUtil = placeholderUtil;
    }

    public void sendTemplate(Player player, NotificationTemplate template) {
        FoliaTaskUtil.runForPlayer(plugin, player, () -> deliver(player, template));
    }

    public void sendOneOff(Player player, String message) {
        NotificationTemplate defaults = settings.oneOffDefaults();
        NotificationTemplate oneOff = new NotificationTemplate(
                "one-off",
                new NotificationTemplate.ChatSection(true, message),
                new NotificationTemplate.ActionBarSection(
                        defaults.actionBar().enabled(),
                        defaults.actionBar().message().replace("%message%", message)
                ),
                defaults.sound(),
                new NotificationTemplate.TitleSection(
                        defaults.title().enabled(),
                        defaults.title().title().replace("%message%", message),
                        defaults.title().subtitle().replace("%message%", message),
                        defaults.title().fadeIn(),
                        defaults.title().stay(),
                        defaults.title().fadeOut()
                )
        );
        sendTemplate(player, oneOff);
    }

    private void deliver(Player player, NotificationTemplate template) {
        if (template.chat().enabled()) {
            String parsed = prepare(player, template.chat().message());
            player.sendMessage(parsed);
        }

        if (template.actionBar().enabled()) {
            String parsed = prepare(player, template.actionBar().message());
            player.sendActionBar(Component.text(parsed));
        }

        if (template.title().enabled()) {
            String title = prepare(player, template.title().title());
            String subtitle = prepare(player, template.title().subtitle());
            player.sendTitle(title, subtitle, template.title().fadeIn(), template.title().stay(), template.title().fadeOut());
        }

        if (template.sound().enabled()) {
            NamespacedKey key = NamespacedKey.fromString(template.sound().sound());
            if (key == null) {
                plugin.getLogger().warning("Invalid sound key in template '" + template.id() + "': " + template.sound().sound());
                return;
            }
            Sound.Type soundType = Registry.SOUNDS.get(key);
            if (soundType == null) {
                plugin.getLogger().warning("Unknown sound in template '" + template.id() + "': " + template.sound().sound());
                return;
            }
            player.playSound(Sound.sound(soundType, Sound.Source.MASTER, template.sound().volume(), template.sound().pitch()));
        }
    }

    private String prepare(Player player, String input) {
        String parsedPlaceholders = placeholderUtil.parse(player, input);
        return ColorUtil.colorize(parsedPlaceholders, settings);
    }
}
