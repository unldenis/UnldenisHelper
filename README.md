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
## Commands
```java
Commands.create("hi").handler(((sender, args) -> {
    if(sender instanceof Player player && player.hasPermission("plugin.admin")) {
        player.sendMessage("Hi " + player.getName());
    }
})).bindWith(plugin);
```
## Asynchronous API
```java
public CompletableFuture<String> findPlayer(JavaPlugin plugin) {
    return BukkitFuture.supplyAsync(plugin, ()-> {
        // load name from database
        return "unldenis";
    });
}
public CompletableFuture<Integer> getPlayTime(JavaPlugin plugin, String user) {
    return BukkitFuture.supplyAsync(this, ()-> {
        // load stat from database
        return 1;
    });
}

findPlayer(plugin)
.thenApply(user -> getPlayTime(plugin, user))
.thenAccept(userPlaytime -> {
    // perform actions with response
    System.out.println("This player has " + userPlaytime + " hours played");
})
.exceptionally(throwable -> {
    // something has terribly gone wrong!
    // handle exception
    return null;
});
```
