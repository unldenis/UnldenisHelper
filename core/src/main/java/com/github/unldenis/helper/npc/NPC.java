package com.github.unldenis.helper.npc;


import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.UUID;


public abstract class NPC {

    protected final Location location;
    protected final UUID playerSkin;
    protected final NPCHolo lines;

    public NPC(
            @NonNull Location location,
            @NonNull UUID playerSkin,
            @NonNull ArrayList<String> lines
    ) {
        this.location = location;
        this.playerSkin = playerSkin;
        this.lines = new NPCHolo(location.clone(), lines);
    }

    @NonNull
    public NPCHolo lines() {
        return this.lines;
    }

    public void destroy() {
        lines.hide();
        Bukkit.getOnlinePlayers().forEach(this::removeNPCPacket);
    }

    public abstract void spawn();

    public abstract void addNPCPacket(@NonNull Player player);

    protected abstract void removeNPCPacket(@NonNull Player player);

    protected abstract void sendPacket(@NonNull Player player, @NonNull Object packet);

    public abstract int getId();

    @NonNull
    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {
        private Location location;
        private UUID skin;
        private ArrayList<String> lines = new ArrayList<>();

        public Builder location(@NonNull Location location) {
            this.location = location;
            return this;
        }

        public Builder skin(@NonNull UUID skin) {
            this.skin = skin;
            return this;
        }

        public Builder addLine(@NonNull String line) {
            this.lines.add(line);
            return this;
        }

        public Builder lines(@NonNull ArrayList<String> lines) {
            this.lines = lines;
            return this;
        }

        @NonNull
        public NPC build() {
            if(location==null || lines.isEmpty())
                throw new IllegalArgumentException("Location or at least one line is missing");
            if(skin==null)
                skin = UUID.randomUUID();
            try {
                Class<?> clazz = Class.forName("com.github.unldenis.helper.npc."+PacketReader.Builder.VERSION+".Npc");
                return  (NPC) clazz.getConstructor(Location.class, UUID.class, ArrayList.class).newInstance(location, skin, lines);
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | ClassNotFoundException e) {
                throw new IllegalStateException(e);
            }
        }
    }

}
