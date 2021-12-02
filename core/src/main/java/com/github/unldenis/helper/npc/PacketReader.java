package com.github.unldenis.helper.npc;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public abstract class PacketReader {

    public static String PACKET_INJECTOR_NAME = "PacketInjector";



    protected final JavaPlugin plugin;
    protected Set<NPC> npcs = new HashSet<>();

    public PacketReader(@NonNull JavaPlugin plugin) {
        this.plugin = plugin;
    }


    public void addNPC(@NonNull NPC npc) {
        npcs.add(npc);
    }

    public void removeNPC(@NonNull NPC npc) {
        npcs.remove(npc);
    }

    public abstract void inject(@NonNull Player player);

    public abstract void uninject(@NonNull Player player);



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
