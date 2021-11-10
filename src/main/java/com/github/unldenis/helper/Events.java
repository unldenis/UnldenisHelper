package com.github.unldenis.helper;

import lombok.NonNull;
import org.bukkit.event.Event;
import java.util.Arrays;

public class Events {

    /**
     * Method used to create a new event
     * @param clazz class of type org.bukkit.event.Event
     * @return CEvent type object
     */
    public static <T extends Event> CEvent<T> subscribe(@NonNull Class<T> clazz) {
        return new CEvent<T>(clazz);
    }
}
