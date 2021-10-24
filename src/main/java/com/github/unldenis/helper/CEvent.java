package com.github.unldenis.helper;
import org.bukkit.Bukkit;
import org.bukkit.event.*;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.function.Consumer;
import java.util.function.Predicate;


public final class CEvent<T extends Event> implements Listener {

    private Class clazz;
    private Consumer consumer;
    private HashSet<Predicate> predicates = new HashSet<>();

    public <T extends Event> CEvent(@Nonnull Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Method used to avoid events
     * @param filter the predicate to apply to the event
     * @return the same object
     */
    public CEvent<T> filter(@Nonnull Predicate<T> filter) {
        predicates.add(filter);
        return this;
    }

    /**
     * Method that is executed when the event is triggered after the filters.
     * @param consumer what will be done
     * @return the same object
     */
    public CEvent<T> handler(@Nonnull Consumer<T> consumer) {
        this.consumer = consumer;
        return this;
    }

    /**
     * Method used to register the event
     * @param plugin main class of a plugin
     */
    public void bindWith(@Nonnull JavaPlugin plugin) {
        bindWith(plugin, EventPriority.NORMAL);
    }

    /**
     * Method used to register the event
     * @param plugin main class of a plugin
     * @param eventPriority event priority
     */
    public void bindWith(@Nonnull JavaPlugin plugin, @Nonnull EventPriority eventPriority) {
        Bukkit.getServer().getPluginManager().registerEvent(clazz, this, eventPriority, (listener, event) -> {
            boolean v = true;
            for(Predicate predicate: predicates) {
                if (!predicate.test(event)) {
                    v = false;
                    break;
                }
            }
            if(v)
                consumer.accept(event);
        }, plugin);
    }
}
