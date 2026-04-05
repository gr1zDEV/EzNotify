package com.ezinnovations.eznotify.util;

import com.ezinnovations.eznotify.config.PluginSettings;
import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Converts configured color formats to Minecraft section-based colors.
 */
public final class ColorUtil {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    private ColorUtil() {
    }

    public static String colorize(String input, PluginSettings settings) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        String output = input;
        if (settings.parseHexColors()) {
            output = applyHex(output);
        }

        if (settings.parseLegacyColors()) {
            output = ChatColor.translateAlternateColorCodes('&', output);
        }

        return output;
    }

    private static String applyHex(String input) {
        Matcher matcher = HEX_PATTERN.matcher(input);
        StringBuilder builder = new StringBuilder();

        while (matcher.find()) {
            String hex = matcher.group(1);
            StringBuilder replacement = new StringBuilder("§x");
            for (char character : hex.toCharArray()) {
                replacement.append('§').append(character);
            }
            matcher.appendReplacement(builder, replacement.toString());
        }

        matcher.appendTail(builder);
        return builder.toString();
    }
}
