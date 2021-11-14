# UnldenisHelper

Library that increases productivity in the production of spigot plugins.
## Table of contents
- <a href="#install">Install</a>
- <a href="#events">Events</a>
- <a href="#concurrent-api">Concurrent API</a>
- <a href="#commands">Commands</a>
<a id="install">
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
<a id="events">
## Events
Classic use of the Spigotmc-api: 
```java
//imports
public class PlayerListeners implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
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
Using my library, these few lines of code will suffice for you to insert into any method!
```java
Events.subscribe(PlayerMoveEvent.class)
.filter(e -> !e.getPlayer().hasPermission("plugin.admin"))
.filter(e -> e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getTo().getZ())
.handler(event -> {
    event.getPlayer().sendMessage("You moved an entire block");
}).bindWith(plugin);
```
<a id="concurrent-api">
## Concurrent API
To handle async and sync events the Bukkit API provides nothing less than a runnable. <a href="https://github.com/unldenis/UnldenisHelper/blob/master/src/main/java/com/github/unldenis/helper/concurrent/BukkitFuture.java">BukkitFuture</a> allows you to return a new CompletableFuture that is completed by Bukkit schedule, which will return a value at the end of a task. Moreover, thanks to <a href="https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html">CompletableFuture</a> you will be able to execute more tasks sequentially.<br>
The method <a href="https://github.com/unldenis/UnldenisHelper/blob/01e1857f9009ac83fb6c28267ffee133920798b8/src/main/java/com/github/unldenis/helper/concurrent/BukkitFuture.java#L112">sync</a> it is used to execute the biconsumer of the whenComplete synchronously so to be able to use the bukkit API.
```java
//The bindWith method must be called once to register your plugin instance.
BukkitFuture.bindWith(plugin);

findPlayer()
.thenCompose(playerName -> getPlayTime(playerName))
.whenComplete(sync((playtime, t) -> {
    if(t!=null) {
        // handle possible errors
        Bukkit.broadcastMessage(t.getMessage());
        return;
    }
    Bukkit.broadcastMessage("Playtime sync is " + playtime);
}));
```
```java
public CompletableFuture<String> findPlayer() {
    return BukkitFuture.supplyAsync(()-> {
        // load name from database
        return "unldenis";
    });
}
public CompletableFuture<Integer> getPlayTime(String player) {
    return BukkitFuture.supplyAsync(()-> {
        if(player==null || player.isEmpty()) throw new IllegalArgumentException("Player is invalid");
        // load stat from database
        return 0;
    });
}
```
<a id="commands">
## Commands
Creating commands also allows you to avoid boilerplate code.
```java
Commands.create("hi").handler(((sender, args) -> {
    if(sender instanceof Player player && player.hasPermission("plugin.admin")) {
        player.sendMessage("Hi " + player.getName());
    }
})).bindWith(plugin);
```
