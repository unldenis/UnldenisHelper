package com.github.unldenis.helper.util;

import lombok.NonNull;
import org.bukkit.ChatColor;

public class ChatUtil {

    /**
     * Method that returns a message colored by &
     * @param text original message
     * @return converted message
     */
    public static String color(@NonNull String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }


}
