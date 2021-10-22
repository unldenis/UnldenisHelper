package com.github.unldenis.helper;

import org.bukkit.event.Event;

import javax.annotation.Nonnull;

public class Events {

    public static <T extends Event> CEvent<T> subscribe(@Nonnull Class<T> clazz) {
        return new CEvent<T>(clazz);
    }
}
