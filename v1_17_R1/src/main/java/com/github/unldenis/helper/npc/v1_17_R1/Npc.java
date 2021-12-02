package com.github.unldenis.helper.npc.v1_17_R1;

import com.github.unldenis.helper.npc.NPC;
import com.mojang.authlib.GameProfile;
import lombok.NonNull;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.UUID;

public class Npc extends NPC {

    private EntityPlayer npc;

    public Npc(@NonNull Location location, @NonNull UUID playerSkin, @NonNull ArrayList<String> lines) {
        super(location, playerSkin, lines);
    }

    @Override
    public void spawn() {
        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld)location.getWorld()).getHandle(); // Change "world" to the world the NPC should be spawned in.
        GameProfile gameProfile = new GameProfile(playerSkin, ""); // Change "playername" to the name the NPC should have, max 16 characters.
        npc = new EntityPlayer(nmsServer, nmsWorld, gameProfile); // This will be the EntityPlayer (NPC) we send with the sendNPCPacket method.
        npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        //remove nametag
        Team team = null;
        if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam("NPCS") == null) {
            team = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("NPCS");
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        }else{
            team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("NPCS");
            team.addPlayer(npc.getBukkitEntity());
        }

        //show hologram
        lines.show();

    }


    @Override
    public void addNPCPacket(@NonNull Player player) {
        sendPacket(player, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, npc)); // "Adds the player data for the client to use when spawning a player" - https://wiki.vg/Protocol#Spawn_Player
        sendPacket(player, new PacketPlayOutNamedEntitySpawn(npc)); // Spawns the NPC for the player client.
        sendPacket(player, new PacketPlayOutEntityHeadRotation(npc, (byte) (location.getYaw() * 256 / 360))); // Correct head rotation when spawned in player look direction.
        sendPacket(player, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, npc)); //remove from tablist
    }

    @Override
    protected void removeNPCPacket(@NonNull Player player) {
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
