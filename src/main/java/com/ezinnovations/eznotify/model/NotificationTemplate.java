package com.ezinnovations.eznotify.model;

import org.bukkit.configuration.ConfigurationSection;

/**
 * Parsed template model for notifications.
 */
public record NotificationTemplate(
        String id,
        ChatSection chat,
        ActionBarSection actionBar,
        SoundSection sound,
        TitleSection title
) {

    public static NotificationTemplate fromSection(ConfigurationSection section, String id) {
        if (section == null) {
            return empty(id);
        }

        ConfigurationSection chatSection = section.getConfigurationSection("chat");
        ConfigurationSection actionBarSection = section.getConfigurationSection("actionbar");
        ConfigurationSection soundSection = section.getConfigurationSection("sound");
        ConfigurationSection titleSection = section.getConfigurationSection("title");

        ChatSection chat = new ChatSection(
                getBool(chatSection, "enabled", true),
                getString(chatSection, "message", "")
        );

        ActionBarSection actionBar = new ActionBarSection(
                getBool(actionBarSection, "enabled", false),
                getString(actionBarSection, "message", "")
        );

        SoundSection sound = new SoundSection(
                getBool(soundSection, "enabled", false),
                getString(soundSection, "sound", "minecraft:block.note_block.pling"),
                getFloat(soundSection, "volume", 1.0f),
                getFloat(soundSection, "pitch", 1.0f)
        );

        TitleSection title = new TitleSection(
                getBool(titleSection, "enabled", false),
                getString(titleSection, "title", ""),
                getString(titleSection, "subtitle", ""),
                getInt(titleSection, "fade-in", 10),
                getInt(titleSection, "stay", 40),
                getInt(titleSection, "fade-out", 10)
        );

        return new NotificationTemplate(id, chat, actionBar, sound, title);
    }

    public static NotificationTemplate empty(String id) {
        return new NotificationTemplate(
                id,
                new ChatSection(false, ""),
                new ActionBarSection(false, ""),
                new SoundSection(false, "minecraft:block.note_block.pling", 1.0f, 1.0f),
                new TitleSection(false, "", "", 10, 40, 10)
        );
    }

    private static boolean getBool(ConfigurationSection section, String path, boolean def) {
        return section != null ? section.getBoolean(path, def) : def;
    }

    private static String getString(ConfigurationSection section, String path, String def) {
        String value = section != null ? section.getString(path, def) : def;
        return value == null ? def : value;
    }

    private static int getInt(ConfigurationSection section, String path, int def) {
        return section != null ? section.getInt(path, def) : def;
    }

    private static float getFloat(ConfigurationSection section, String path, float def) {
        return section != null ? (float) section.getDouble(path, def) : def;
    }

    public record ChatSection(boolean enabled, String message) { }

    public record ActionBarSection(boolean enabled, String message) { }

    public record SoundSection(boolean enabled, String sound, float volume, float pitch) { }

    public record TitleSection(boolean enabled, String title, String subtitle, int fadeIn, int stay, int fadeOut) { }
}
