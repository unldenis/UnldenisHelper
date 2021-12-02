package com.github.unldenis.helper.game;

import com.github.unldenis.helper.util.ChatUtil;
import lombok.NonNull;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class GamePlayer {

    protected final Player p;
    protected final ItemStack[] contents;
    protected final ItemStack[] armorContents;


    public GamePlayer(@NonNull Player player) {
        this.p = player;
        contents = p.getInventory().getContents().clone();
        armorContents = p.getInventory().getArmorContents().clone();
    }

    /**
     * Method that rollback gameplayer inventory
     */
    public void rollback() {
        p.getInventory().setContents(contents);
        p.getInventory().setArmorContents(armorContents);
    }

    public void sendMessage(@NonNull String m) {
        p.sendMessage(ChatUtil.color(m));
    }

    public void sendTitle(@NonNull String title, String subTitle) {
        p.sendTitle(ChatUtil.color(title), ChatUtil.color(subTitle), 1, 20, 1);
    }

    public void sendActionBar(@NonNull String message) {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatUtil.color(message)));
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
