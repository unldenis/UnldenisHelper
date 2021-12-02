package com.github.unldenis.helper.npc;

import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;

public class NPCHolo {

    private final Location location;
    private final ArrayList<String> lines;
    private ArrayList<ArmorStand> holoEntities = new ArrayList<>();

    public NPCHolo(
            @NonNull Location location,
            @NonNull ArrayList<String> lines
    ) {
        this.location = location;
        this.lines = lines;
    }

    public void show() {
      ArmorStand headerStand;
      Location cloned = location.clone().subtract(0, 0.56, 0);
      for(int j=lines.size()-1; j>=0; j--) {
        headerStand = (ArmorStand)cloned.getWorld().spawnEntity(cloned.add(0.0, 0.28, 0), EntityType.ARMOR_STAND);
        headerStand.setCustomName(lines.get(j));
        headerStand.setCustomNameVisible(true);
        headerStand.setGravity(false);
        headerStand.setVisible(false);
        this.holoEntities.add(headerStand);
      }
    }

    public void hide() {
      this.holoEntities.forEach(Entity::remove);
    }


    public void setLine(int line, @NonNull String text) {
      ArmorStand lineStand = this.holoEntities.get(this.holoEntities.size()-1-line);
      lineStand.setCustomName(text);
      lines.set(line, text);
    }

    public void removeLine(int line) {
      lines.remove(line); //remove string from list
      this.update();
    }

    public String getLine(int line) {
    return this.lines.get(line);
  }


    private void update() {
      hide();
      show();
    }

}