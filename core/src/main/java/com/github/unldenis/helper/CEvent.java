package com.github.unldenis.helper;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.function.Consumer;
import java.util.function.Predicate;


public final class CEvent<T extends Event> implements Listener {

    private Class clazz;
    private String eventName;
    private Consumer consumer;
    private HashSet<Predicate> predicates = new HashSet<>();

    public <T extends Event> CEvent(@NonNull Class<T> clazz) {
        this.clazz = clazz;
        eventName = clazz.getSimpleName();
    }

    /**
     * Method used to avoid events
     * @param filter the predicate to apply to the event
     * @return the same object
     */
    public CEvent<T> filter(@NonNull Predicate<T> filter) {
        predicates.add(filter);
        return this;
    }

    /**
     * Method that is executed when the event is triggered after the filters.
     * @param consumer what will be done
     * @return the same object
     */
    public CEvent<T> handler(@NonNull Consumer<T> consumer) {
        this.consumer = consumer;
        return this;
    }

    /**
     * Method used to register the event
     * @param plugin main class of a plugin
     */
    public void bindWith(@NonNull JavaPlugin plugin) {
        bindWith(plugin, EventPriority.NORMAL);
    }

    /**
     * Method used to register the event
     * @param plugin main class of a plugin
     * @param eventPriority event priority
     */
    public void bindWith(@NonNull JavaPlugin plugin, @NonNull EventPriority eventPriority) {
        Bukkit.getServer().getPluginManager().registerEvent(clazz, this, eventPriority, (listener, event) -> {
            if(event.getEventName().equals(eventName)) {  //EntityDeathEvent conflict with PlayerDeathEvent fix
                boolean v = true;
                for(Predicate predicate: predicates) {
                    if (!predicate.test((T) event)) {
                        v = false;
                        break;
                    }
                }
                if(v)
                    consumer.accept((T) event);
            }
        }, plugin);
    }
}
