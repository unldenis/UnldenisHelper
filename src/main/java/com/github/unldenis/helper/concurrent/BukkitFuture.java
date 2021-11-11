package com.github.unldenis.helper.concurrent;

import lombok.NonNull;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class BukkitFuture {

    /**
     * Returns a new CompletableFuture that is asynchronously completed by Bukkit schedule with the value obtained by calling the given Supplier.
     * @param plugin the reference to the plugin scheduling task
     * @param supplier a function returning the value to be used to complete the returned CompletableFuture
     * @param <T> the function's return type
     * @return
     */
    public static <T> CompletableFuture<T> supplyAsync(@NonNull JavaPlugin plugin, @NonNull Supplier<T> supplier) {
        CompletableFuture<T> future = new CompletableFuture<>();
        new BukkitRunnable() {
            @Override
            public void run() {
                future.complete(supplier.get());
            }
        }
        .runTaskAsynchronously(plugin);
        return future;
    }

    /**
     * Returns a new CompletableFuture that is asynchronously completed by Bukkit schedule after it runs the given action.
     * @param plugin the reference to the plugin scheduling task
     * @param runnable the action to run before completing the returned CompletableFuture
     * @return
     */
    public static CompletableFuture<Void> runAsync(@NonNull JavaPlugin plugin, @NonNull Runnable runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
                future.complete(null);
            }
        }
        .runTaskAsynchronously(plugin);
        return future;
    }
}
