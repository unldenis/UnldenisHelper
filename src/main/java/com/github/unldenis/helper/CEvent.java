package com.github.unldenis.helper;
import org.bukkit.Bukkit;
import org.bukkit.event.*;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;


public class CEvent<T extends Event> implements Listener {

    private Class clazz;
    private Consumer consumer;
    private ArrayList<Predicate> predicates = new ArrayList<>();

    public <T extends Event> CEvent(@Nonnull Class<T> clazz) {
        this.clazz = clazz;
    }

    public CEvent<T> filter(@Nonnull Predicate<T> filter) {
        predicates.add(filter);
        return this;
    }

    public CEvent<T> handler(@Nonnull Consumer<T> consumer) {
        this.consumer = consumer;
        return this;
    }

    public void bind(@Nonnull JavaPlugin plugin) {
        bind(plugin, EventPriority.NORMAL);
    }

    public void bind(@Nonnull JavaPlugin plugin, @Nonnull EventPriority eventPriority) {
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
