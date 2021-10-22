package com.github.unldenis.helper.util;

import org.bukkit.ChatColor;

import javax.annotation.Nonnull;

public class ChatUtil {

    /**
     * Method that returns a message colored by &
     * @param text original message
     * @return converted message
     */
    public static String color(@Nonnull String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }


}
