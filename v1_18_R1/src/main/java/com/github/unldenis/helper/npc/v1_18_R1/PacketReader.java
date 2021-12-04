package com.github.unldenis.helper.npc.v1_18_R1;
import com.github.unldenis.helper.Events;
import com.github.unldenis.helper.npc.event.RightClickNPCEvent;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.NonNull;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PacketReader extends com.github.unldenis.helper.npc.PacketReader {

    private Map<UUID, Channel> channels = new HashMap<>();

    public PacketReader(@NonNull JavaPlugin plugin) {
        super(plugin);
        Events.subscribe(PlayerQuitEvent.class).handler(e->{
            uninject(e.getPlayer());
            npcs.forEach(npc -> npc.removeNPCPacket(e.getPlayer()));
        }).bindWith(plugin);
        Events.subscribe(PlayerJoinEvent.class).handler(e->{
            inject(e.getPlayer());
            npcs.forEach(npc -> npc.addNPCPacket(e.getPlayer()));
        }).bindWith(plugin);
    }

    @Override
    public void inject(@NonNull Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        Channel channel = craftPlayer.getHandle().b.a.k;
        channels.put(player.getUniqueId(), channel);

        if(channel.pipeline().get(PacketReader.PACKET_INJECTOR_NAME)!=null)
            return;
        channel.pipeline().addAfter("decoder", PacketReader.PACKET_INJECTOR_NAME, new MessageToMessageDecoder<PacketPlayInUseEntity>() {
            @Override
            protected void decode(ChannelHandlerContext channel, PacketPlayInUseEntity packet, List<Object> arg) {
                arg.add(packet);
                readPacket(player, packet);
            }
        });
    }

    @Override
    public void uninject(@NonNull Player player) {
        Channel channel = channels.get(player.getUniqueId());
        if(channel != null && channel.pipeline().get(PacketReader.PACKET_INJECTOR_NAME)!=null)
            channel.pipeline().remove(PacketReader.PACKET_INJECTOR_NAME);
    }

    private void readPacket(@NonNull Player player, @NonNull Packet<?> packet) {
        if(packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity")) {

            String actionName;
            switch (getValue(packet, "b").getClass().getName()) {
                case "net.minecraft.network.protocol.game.PacketPlayInUseEntity$e": {
                    actionName = "INTERACT_AT";
                    break;
                }
                case "net.minecraft.network.protocol.game.PacketPlayInUseEntity$d": {
                    actionName = "INTERACT";
                    break;
                }
                case "net.minecraft.network.protocol.game.PacketPlayInUseEntity$1": {
                    actionName =  "ATTACK";
                    break;
                }
                default:
                    throw new IllegalStateException("Unexpected value: " + getValue(packet, "b").getClass().getName());
            };

            int id = (int) getValue(packet, "a");
            if(actionName.equals("ATTACK")) {
                npcs
                        .stream()
                        .filter(npc -> npc.getId()==id)
                        .findFirst()
                        .ifPresent(npc -> {
                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    Bukkit.getPluginManager().callEvent(new RightClickNPCEvent(player, npc));
                                }
                            }.runTask(plugin);
                        });
            }
        }
    }

}
