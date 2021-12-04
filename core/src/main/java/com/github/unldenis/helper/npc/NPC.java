package com.github.unldenis.helper.npc;


import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Abstract class representing your npc.
 * @author unldenis
 */
public abstract class NPC {

    protected final PacketReader packetReader;
    protected final Location location;
    protected final String playerSkin;
    protected final NPCHolo lines;

    protected final String[] texture;
    /**
     * Complete constructor of the NPC class
     * @param location the location where to spawn the npc
     * @param playerSkin the skin of the player to be assigned to the npc
     * @param lines the text above the npc
     * @deprecated Use the {@link #builder()} method because it allows you to automatically instantiate the npc of the server version
     */
    @Deprecated
    public NPC(
            @NonNull PacketReader packetReader,
            @NonNull Location location,
            @NonNull String playerSkin,
            @NonNull ArrayList<String> lines
    ) {
        this.packetReader = packetReader;
        this.location = location;
        this.playerSkin = playerSkin;
        this.lines = new NPCHolo(location.clone(), lines);
        this.texture = loadTexture();
    }

    @NonNull
    public NPCHolo lines() {
        return this.lines;
    }

    /**
     * Destroy the NPC. The npc will not be removed from the PacketReader in this method.
     */
    public void destroy() {
        lines.hide();
        removeNPCPacket();
    }

    public void addNPCPacket() {
        Bukkit.getOnlinePlayers().forEach(this::addNPCPacket);
    }

    private void removeNPCPacket() {
        Bukkit.getOnlinePlayers().forEach(this::removeNPCPacket);
    }

    @Nullable
    private String[] loadTexture() {
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL(String.format("https://api.ashcon.app/mojang/v2/user/%s", playerSkin)).openConnection();
            if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                ArrayList<String> lines = new ArrayList<>();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                reader.lines().forEach(lines::add);

                String reply = String.join(" ",lines);
                int indexOfValue = reply.indexOf("\"value\": \"");
                int indexOfSignature = reply.indexOf("\"signature\": \"");
                String skin = reply.substring(indexOfValue + 10, reply.indexOf("\"", indexOfValue + 10));
                String signature = reply.substring(indexOfSignature + 14, reply.indexOf("\"", indexOfSignature + 14));

                return new String[] { skin , signature};
            }

            else {
                Bukkit.getConsoleSender().sendMessage("Connection could not be opened when fetching player skin (Response code " + connection.getResponseCode() + ", " + connection.getResponseMessage() + ")");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public abstract void spawn();

    public abstract void addNPCPacket(@NonNull Player player);

    public abstract void removeNPCPacket(@NonNull Player player);

    protected abstract void sendPacket(@NonNull Player player, @NonNull Object packet);

    public abstract int getId();

    @NonNull
    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {
        private Location location;
        private String skin;
        private ArrayList<String> lines = new ArrayList<>();

        public Builder location(@NonNull Location location) {
            this.location = location;
            return this;
        }

        public Builder skin(@NonNull String skin) {
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
        public NPC build(@NonNull PacketReader packetReader) {
            if(location == null || lines.isEmpty())
                throw new IllegalArgumentException("Location or at least one line is missing");
            if(skin==null)
                skin = "md_5";
            try {
                Class<?> clazz = Class.forName("com.github.unldenis.helper.npc."+PacketReader.Builder.VERSION+".Npc");
                NPC npc =  (NPC) clazz.getConstructor(PacketReader.class, Location.class, String.class, ArrayList.class).newInstance(packetReader, location, skin, lines);
                packetReader.addNPC(npc);
                return npc;
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | ClassNotFoundException e) {
                throw new IllegalStateException(e);
            }
        }
    }

}
