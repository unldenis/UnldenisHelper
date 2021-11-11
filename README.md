# UnldenisHelper

Library that increases productivity in the production of spigot plugins.
## How to install
### Maven
Add the JitPack repository to your build file:
<br>
```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```
Add the dependency:
<br>
```xml
<dependency>
    <groupId>com.github.unldenis</groupId>
    <artifactId>UnldenisHelper</artifactId>
    <version>master-SNAPSHOT</version>
</dependency>
```
## Events
Classic use of the Spigotmc-api: 
```java
//imports
public class PlayerListeners implements Listener {
    @EventHandler
    public void onQuit(PlayerMoveEvent event) {
        if(!e.getPlayer().hasPermission("plugin.admin")) return;
        if(e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getTo().getZ()) return;
        event.getPlayer().sendMessage("You moved an entire block");
    }
}

/*
    And in the main class:
*/
getServer().getPluginManager().registerEvents(new PlayerListeners(), this);
```
<br><br>
Using my library, these few lines of code will suffice for you to insert into any method!
```java
Events.subscribe(PlayerMoveEvent.class)
.filter(e -> !e.getPlayer().hasPermission("plugin.admin"))
.filter(e -> e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getTo().getZ())
.handler(event -> {
    event.getPlayer().sendMessage("You moved an entire block");
}).bindWith(plugin);
```
## Asynchronous API
To handle asynchronous events the Bukkit API provides nothing less than a runnable to execute tasks asynchronously. <a href="https://github.com/unldenis/UnldenisHelper/blob/master/src/main/java/com/github/unldenis/helper/concurrent/BukkitFuture.java">BukkitFuture</a> allows you to return a new CompletableFuture that is asynchronously completed by Bukkit schedule, which <a href="https://github.com/unldenis/UnldenisHelper/blob/74b861606b4eb09cbfdc98300ba7565c094c4c33/src/main/java/com/github/unldenis/helper/concurrent/BukkitFuture.java#L19">allows</a> you to return a value at the end of a task. Moreover, thanks to <a href="https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html">CompletableFuture</a> you will be able to execute more tasks sequentially.
```java
findPlayer(plugin)
.thenCompose(player -> getPlayTime(plugin, player))
.thenAccept(pPlaytime -> {
    // perform actions with response
.exceptionally(throwable -> {
    // something has terribly gone wrong!
    // handle exception
    return null;
});
```
```java
public CompletableFuture<String> findPlayer(JavaPlugin plugin) {
    return BukkitFuture.supplyAsync(plugin, ()-> {
        // load name from database
        return "unldenis";
    });
}
public CompletableFuture<Integer> getPlayTime(JavaPlugin plugin, String player) {
    return BukkitFuture.supplyAsync(plugin, ()-> {
        if(player==null || player.isEmpty()) throw new IllegalArgumentException("Player is invalid");
        // load stat from database
        return 1;
    });
}
```
## Commands
Creating commands also allows you to avoid boilerplate code.
```java
Commands.create("hi").handler(((sender, args) -> {
    if(sender instanceof Player player && player.hasPermission("plugin.admin")) {
        player.sendMessage("Hi " + player.getName());
    }
})).bindWith(plugin);
```
