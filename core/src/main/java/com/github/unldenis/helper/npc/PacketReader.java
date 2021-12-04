package com.github.unldenis.helper.npc;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public abstract class PacketReader {

    public static String PACKET_INJECTOR_NAME = "PacketInjectorHelper";

    @Getter protected final JavaPlugin plugin;
    protected Set<NPC> npcs = new HashSet<>();

    public PacketReader(@NonNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void addNPC(@NonNull NPC npc) {
        this.npcs.add(npc);
    }

    public void removeNPC(@NonNull NPC npc) {
        this.npcs.remove(npc);
    }

    public Object getValue(Object instance, String name) {
        Object result = null;
        try {
            Field field = instance.getClass().getDeclaredField(name);
            field.setAccessible(true);
            result = field.get(instance);
            field.setAccessible(false);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public abstract void inject(@NonNull Player player);

    public abstract void uninject(@NonNull Player player);

    @NonNull
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        public static String VERSION;
        static {
            String OBC_PREFIX = Bukkit.getServer().getClass().getPackage().getName();
            VERSION = OBC_PREFIX.replace("org.bukkit.craftbukkit", "").replace(".", "");
        }

        private JavaPlugin plugin;


        public Builder plugin(@NonNull JavaPlugin plugin) {
            this.plugin = plugin;
            return this;
        }


        @NonNull
        public PacketReader build() {
            if(plugin==null)
                throw new IllegalArgumentException("Plugin not set");
            try {
                Class<?> clazz = Class.forName("com.github.unldenis.helper.npc."+VERSION+".PacketReader");
                return (PacketReader) clazz.getConstructor(JavaPlugin.class).newInstance(plugin);
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | ClassNotFoundException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
