package com.github.unldenis.helper.npc.v1_17_R1;

import com.github.unldenis.helper.npc.NPC;
import com.github.unldenis.helper.npc.PacketReader;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.NonNull;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.scores.ScoreboardTeam;
import net.minecraft.world.scores.ScoreboardTeamBase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class Npc extends NPC {

    private EntityPlayer npc;

    public Npc(@NonNull PacketReader packetReader, @NonNull Location location, @NonNull String playerSkin, @NonNull ArrayList<String> lines) {
        super(packetReader, location, playerSkin, lines);
    }

    @Override
    protected void spawn() {
        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld)location.getWorld()).getHandle(); // Change "world" to the world the NPC should be spawned in.
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), ""); // Change "playername" to the name the NPC should have, max 16 characters.
        npc = new EntityPlayer(nmsServer, nmsWorld, gameProfile); // This will be the EntityPlayer (NPC) we send with the sendNPCPacket method.
        npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        //show hologram
        lines.show();

        //add packets to all players
        addNPCPacket();

    }


    @Override
    public void addNPCPacket(@NonNull Player player) {
        if(super.texture!=null) {
            npc.getProfile().getProperties().put("textures", new Property("textures", texture[0], texture[1]));
            // The client settings.
            DataWatcher watcher = npc.getDataWatcher();
            watcher.set(new DataWatcherObject<>(17, DataWatcherRegistry.a), (byte)127);
            PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(npc.getId(), watcher, true);

            sendPacket(player, packet);
        }

        sendPacket(player, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, npc)); // "Adds the player data for the client to use when spawning a player" - https://wiki.vg/Protocol#Spawn_Player
        sendPacket(player, new PacketPlayOutNamedEntitySpawn(npc)); // Spawns the NPC for the player client.
        sendPacket(player, new PacketPlayOutEntityHeadRotation(npc, (byte) (location.getYaw() * 256 / 360))); // Correct head rotation when spawned in player look direction.
        sendPacket(player, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, npc)); //remove from tablist


        //remove nametag packets
        ScoreboardTeam team = new ScoreboardTeam(((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(), player.getName());
        team.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.b);

        PacketPlayOutScoreboardTeam score = PacketPlayOutScoreboardTeam.a(team);
        PacketPlayOutScoreboardTeam score1 = PacketPlayOutScoreboardTeam.a(team, true);
        PacketPlayOutScoreboardTeam score2 = PacketPlayOutScoreboardTeam.a(team, npc.getName(), PacketPlayOutScoreboardTeam.a.a);

        sendPacket(player, score);
        sendPacket(player, score1);
        sendPacket(player, score2);
    }



    @Override
    public void removeNPCPacket(@NonNull Player player) {
        sendPacket(player, new PacketPlayOutEntityDestroy(npc.getId()));
    }

    @Override
    protected void sendPacket(@NonNull Player player, @NonNull Object packet) {
        PlayerConnection connection = ((CraftPlayer)player).getHandle().b;
        connection.sendPacket((Packet<?>) packet);
    }

    @Override
    public int getId() {
        return npc.getId();
    }


}
