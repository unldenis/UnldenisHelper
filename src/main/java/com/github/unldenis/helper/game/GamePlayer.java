package com.github.unldenis.helper.game;

import lombok.NonNull;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class GamePlayer {

    protected final Player p;
    private PlayerInventory backupInventory;

    public GamePlayer(@NonNull Player player) {
        this.p = player;
        backupInventory = player.getInventory();

    }

    /**
     * Method that rollback gameplayer inventory
     */
    public void rollback() {
        p.getInventory().setContents(backupInventory.getContents());
        p.getInventory().setArmorContents(backupInventory.getArmorContents());
    }


    public void sendMessage(@NonNull String m) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', m));
    }

    public void sendTitle(@NonNull String title, String subTitle) {
        p.sendTitle(ChatColor.translateAlternateColorCodes('&', title), ChatColor.translateAlternateColorCodes('&', subTitle), 1, 20, 1);
    }

    public void sendActionBar(@NonNull String message) {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', message)));
    }

    public void playSound(@NonNull Sound sound) {
        p.playSound(p.getLocation(), sound, 1.0f, 1.0f);
    }

    public void teleport(@NonNull Location location) {
        p.teleport(location);
    }

    public void resetPlayer() {
        p.getInventory().clear();
        p.getInventory().setHelmet(new ItemStack(Material.AIR));
        p.getInventory().setBoots(new ItemStack(Material.AIR));
        p.getInventory().setChestplate(new ItemStack(Material.AIR));
        p.getInventory().setLeggings(new ItemStack(Material.AIR));
        p.setHealth(20.0D);
        p.setFoodLevel(20);
        p.setFireTicks(0);
        p.setGameMode(GameMode.ADVENTURE);
        p.setFlying(false);
        p.setAllowFlight(false);
    }

    public @NonNull Player getPlayer() {
        return p;
    }


}
