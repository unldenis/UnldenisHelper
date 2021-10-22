package com.github.unldenis.helper;

import org.bukkit.event.Event;

import javax.annotation.Nonnull;

public class Events {

    /**
     * Method used to create a new event
     * @param clazz class of type org.bukkit.event.Event
     * @return CEvent type object
     */
    public static <T extends Event> CEvent<T> subscribe(@Nonnull Class<T> clazz) {
        return new CEvent<T>(clazz);
    }
}
