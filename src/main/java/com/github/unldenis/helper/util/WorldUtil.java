package com.github.unldenis.helper.util;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import javax.annotation.Nonnull;

public class WorldUtil {
    /**
     * Unloading maps, to rollback maps. Will delete all player builds until last server save
     * @param mapname name of the world
     */
    public static void unloadMap(@Nonnull String mapname){
        if(Bukkit.getServer().unloadWorld(Bukkit.getServer().getWorld(mapname), false)){
            Bukkit.getLogger().info("Successfully unloaded " + mapname);
        }else{
            Bukkit.getLogger().severe("COULD NOT UNLOAD " + mapname);
        }
    }

    /**
     * Loading maps (MUST BE CALLED AFTER UNLOAD MAPS TO FINISH THE ROLLBACK PROCESS)
     * @param mapname name of the world
     */
    public static void loadMap(@Nonnull String mapname){
        World w = Bukkit.getServer().createWorld(new WorldCreator(mapname));
        w.setAutoSave(false);
    }

    /**
     * Maprollback method, because were too lazy to type 2 lines
     * @param mapname name of the world
     */
    public static void rollback(@Nonnull String mapname){
        unloadMap(mapname);
        loadMap(mapname);
    }
}
